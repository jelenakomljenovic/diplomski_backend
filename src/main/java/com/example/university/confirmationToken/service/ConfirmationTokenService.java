package com.example.university.confirmationToken.service;

import com.example.university.confirmationToken.model.ConfirmationToken;
import com.example.university.confirmationToken.repository.ConfirmationTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Transactional
    public ConfirmationToken createInitialPasswordToken() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(UUID.randomUUID().toString());
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Transactional
    public ConfirmationToken createPasswordResetToken() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(UUID.randomUUID().toString());
        confirmationToken.setExpiryTimestamp(LocalDateTime.now().plusHours(24));
        return confirmationTokenRepository.save(confirmationToken);
    }

    public void validateToken(String token) throws AccessDeniedException {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found."));

        if (confirmationToken.getExpiryTimestamp() != null) {
            checkExpiryTimestamp(confirmationToken);
        }
    }

    @Transactional
    public void invalidateToken(ConfirmationToken confirmationToken) {
        confirmationToken.setExpiryTimestamp(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
    }

    private void checkExpiryTimestamp(ConfirmationToken token) throws AccessDeniedException {
        if (token.getExpiryTimestamp().isBefore(LocalDateTime.now())) {
            throw new AccessDeniedException("Token expired.");
        }
    }
}
