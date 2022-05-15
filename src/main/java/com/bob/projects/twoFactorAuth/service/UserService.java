package com.bob.projects.twoFactorAuth.service;

import com.bob.projects.twoFactorAuth.exception.*;
import com.bob.projects.twoFactorAuth.model.Role;
import com.bob.projects.twoFactorAuth.model.User;
import com.bob.projects.twoFactorAuth.portabls.UserInput;
import com.bob.projects.twoFactorAuth.repository.RoleRepository;
import com.bob.projects.twoFactorAuth.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@Log
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TotpManager totpManager;

    public String loginUser(String username, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByUsername(username).get();
        if (user.isTwoFactorEnabled()) {
            return "";
        }
        return jwtUtil.generateToken(authentication);
    }

    public String verify(String username, String code) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("username %s", username)));
        if (!totpManager.verifyCode(code, user.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }
        return Optional.of(user)
                .map(InstaUserDetails::new)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .map(jwtUtil::generateToken)
                .orElseThrow(() ->
                        new InternalServerException("unable to generate access token"));
    }

    public String userRegistration(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role("USER");
        roleRepository.save(role);
        user.setRoles(new HashSet<>() {{
            add(role);
        }});
        if (user.isTwoFactorEnabled()) {
            user.setSecret(totpManager.generateSecret());
        }
        userRepository.save(user);
        String qrCode = totpManager.getUriForImage(user.getSecret());
        return qrCode;
    }


    public String userCreation(UserInput signUpRequest) {
        User user = User
                .builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .isTwoFactorEnabled(signUpRequest.getIsTwoFactorEnabled())
                .build();
        String qrCode = userRegistration(user);
        return qrCode;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
