package com.example.university.confirmationToken.model;


import com.example.university.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiryTimestamp;

    @OneToOne(mappedBy = "confirmationToken")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfirmationToken)) return false;
        ConfirmationToken that = (ConfirmationToken) o;
        return getId().equals(that.getId()) && getToken().equals(that.getToken()) && getExpiryTimestamp().equals(that.getExpiryTimestamp()) && getUser().equals(that.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getExpiryTimestamp(), getUser());
    }
}
