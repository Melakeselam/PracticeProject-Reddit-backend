package com.example.redditclone.model;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotBlank(message = "username must have alpha numeric characters")
    @Size(min = 3, max = 15, message = "username size must be between  3 and 15 characters")
    private String username;
    @Email
    @NotBlank
    private String email;
//    @Pattern(regexp ="((?=.*[a-z])(?=.*d)(?=.*[@#$%-])(?=.*[A-Z]).{6,16})", message = "at least one lowercase letter\nat least one digit i.e. 0-9\nat least one special character (@#$%-)\nat least one capital letter\nthe length of password from minimum 6 letters to maximum 16 letters")
    @NotBlank
    private String password;
    @NotNull
    private Instant created;
    @NotNull
    @Getter(AccessLevel.NONE)
    private Boolean enabled;

    public Boolean isEnabled(){
        return enabled;
    }

}
