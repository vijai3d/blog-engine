package com.vijai.blog.controller;

import com.vijai.blog.exception.AppException;
import com.vijai.blog.model.*;
import com.vijai.blog.payload.ApiResponse;
import com.vijai.blog.payload.JwtAuthenticationResponse;
import com.vijai.blog.payload.LoginRequest;
import com.vijai.blog.payload.SignUpRequest;
import com.vijai.blog.repository.ConfirmationTokenRepository;
import com.vijai.blog.repository.RoleRepository;
import com.vijai.blog.repository.UserRepository;
import com.vijai.blog.security.JwtTokenProvider;
import com.vijai.blog.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfirmationTokenRepository tokenRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestHeader("domain") Domain domain,
                                              @Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestHeader("domain") Domain domain,
                                          @Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByDomainAndUsername(domain, signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByDomainAndEmail(domain, signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword(), domain);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken(domain, user);

        tokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("viktor.antipin@gmail.com");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:3000/confirm-account?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully. Check mail to confirm your registration"));
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {

        Optional<ConfirmationToken> token = confirmationTokenRepository
                .findByConfirmationToken(confirmationToken);

        if(token.isPresent()){
            User user = userRepository.findByDomainAndEmail(token.get().getDomain(), token.get().getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true, "User registration completed successfully!"));
        } else {
            return new ResponseEntity(new ApiResponse(false, "Authorization link is broken or expired"), HttpStatus.UNAUTHORIZED);
        }
    }
}
