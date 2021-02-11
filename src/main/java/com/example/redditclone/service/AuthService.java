package com.example.redditclone.service;

import com.example.redditclone.config.ApplicationProperties;
import com.example.redditclone.dto.AuthenticationResponse;
import com.example.redditclone.dto.LoginRequest;
import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.User;
import com.example.redditclone.model.VerificationToken;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VerificationTokenRepository;
import com.example.redditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final  UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;

    private final ApplicationProperties applicationProperties;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest registerRequest){

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(Instant.now())
                .enabled(false)
                .build();

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendEmail(new NotificationEmail(
                "Please Activate Your Account!",
                user.getEmail(),
                "Thank you for signing up to Spring Reddit Clone, please click on the address below to activate your account."+
                        applicationProperties.getAccountVerificationUrl()+"/"+token));

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .build();
//        VerificationToken storedVerificationToken =
        verificationTokenRepository.save(verificationToken);

//        token += "-"+storedVerificationToken.getId();

        return token;
    }

    public String verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        try {
            verificationToken.orElseThrow(()-> new SpringRedditException("Invalid Token"));
            fetchUserAndEnable(verificationToken.get());
        }catch (SpringRedditException e){
            return "0:"+e.getMessage();
        }

        return "1:User Activated Successfully";
    }

    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
        @NotBlank(message = "Username is required") String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User not found!"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticated = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authenticated);

        String token = jwtProvider.generateToken(authenticated);
        return new AuthenticationResponse(
                token,
                loginRequest.getUsername());

    }
//    public String verifyAccount(String token) {
//        int idIndex = token.lastIndexOf("-")+1;
//        Long id = Long.parseLong(token.substring(idIndex));
//        VerificationToken storedVerificationToken = verificationTokenRepository.findById(id).orElseThrow();
//        System.out.println(storedVerificationToken.getToken());
//        if(storedVerificationToken.getToken().equals(token.substring(0,idIndex-1))){
//            storedVerificationToken.getUser().setEnabled(true);
//            return "1:User Activated";
//        }else return "0:Registration not verified";
//
//
//    }
}
