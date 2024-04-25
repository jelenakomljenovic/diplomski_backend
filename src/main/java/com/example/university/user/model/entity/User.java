package com.example.university.user.model.entity;

import com.example.university.confirmationToken.model.ConfirmationToken;
import com.example.university.role.model.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotNull
    private String firstName;

    @Size(max = 50)
    @NotNull
    private String lastName;

    @Size(max = 50)
    @NotNull
    private String username;

    @Size(max = 50)
    @Email
    @NotNull
    private String email;

    @Size(max = 500)
    private String password;

    @OneToOne
    @JoinColumn(name = "confirmation_token_id", referencedColumnName = "id")
    private ConfirmationToken confirmationToken;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
