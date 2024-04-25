package com.example.university.user.service;

import com.example.university.confirmationToken.model.ConfirmationToken;
import com.example.university.confirmationToken.repository.ConfirmationTokenRepository;
import com.example.university.confirmationToken.service.ConfirmationTokenService;
import com.example.university.email.EmailService;
import com.example.university.exception.ResourceNotFoundException;
import com.example.university.exception.UserNotFoundException;
import com.example.university.role.model.entity.Role;
import com.example.university.role.model.enums.RoleEnum;
import com.example.university.role.repository.RoleRepository;
import com.example.university.user.model.dto.PasswordDto;
import com.example.university.user.model.dto.UserDto;
import com.example.university.user.model.dto.UserEmailDto;
import com.example.university.user.model.dto.UserRequest;
import com.example.university.user.model.entity.ChangePasswordRequest;
import com.example.university.user.model.entity.User;
import com.example.university.user.model.mapper.UserMapper;
import com.example.university.user.model.response.UserResponse;
import com.example.university.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    private final ModelMapper modelMapper;

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponses(users);
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    public UserResponse insert(UserDto userDto) {
        return saveUser(userDto);
    }

    public UserResponse addUser(User user) throws MessagingException, UnsupportedEncodingException {
        String decodedPassword = user.getPassword();
        String password = user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : null;
        user.setPassword(password);
        user.setConfirmationToken(null);
        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Role " + RoleEnum.USER + " not found"));
        user.setRoles(Set.of(adminRole));

        user = userRepository.saveAndFlush(user);
        emailService.sendAdminMail(user.getEmail(), user.getUsername(), decodedPassword);

        return userMapper.toResponse(user);
    }

    public UserResponse update(UserDto userDto) {
        if (!userRepository.existsById(userDto.getId())) {
            throw new UserNotFoundException(userDto.getId());
        }
        return saveUser(userDto);
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(user.getEmail());
        user.setUsername(user.getUsername());

        user = userRepository.saveAndFlush(user);
        return userMapper.toResponse(user);

    }

    private UserResponse saveUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            setDefaultUserRole(user);
        }
        String password = user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : null;
        user.setPassword(password);

        user = userRepository.saveAndFlush(user);

        return userMapper.toResponse(user);
    }

    private void setDefaultUserRole(User updatedUser) {
        Role userRole = roleRepository.findByName(RoleEnum.USER.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Role " + RoleEnum.USER + " not found"));
        updatedUser.setRoles(Set.of(userRole));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public void changePassword(ChangePasswordRequest request, Long id) {
        User user1 = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user1.getPassword())) {
            throw new IllegalStateException("Wrong password!");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same!");
        }
        user1.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user1);

    }

    @Transactional
    public void setTokenForUser(User user, ConfirmationToken confirmationToken) {
        user.setConfirmationToken(confirmationToken);
        userRepository.save(user);
    }

    @Transactional
    public UserDto savePassword(PasswordDto passwordDto) throws AccessDeniedException {
        confirmationTokenService.validateToken(passwordDto.getToken());
        User user = userRepository.findByConfirmationToken_Token(passwordDto.getToken())
                .orElseThrow(EntityNotFoundException::new);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(passwordDto.getPassword()));
        userRepository.save(user);

        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(passwordDto.getToken()).orElseThrow(EntityNotFoundException::new);
        confirmationTokenService.invalidateToken(confirmationToken);

        return modelMapper.map(user, UserDto.class);
    }


    public boolean sendPasswordResetEmail(UserEmailDto emailDTO) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository
                .findByEmail(emailDTO.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        if (user.getConfirmationToken() != null) {
            ConfirmationToken confirmationToken = confirmationTokenRepository
                    .findByToken(user.getConfirmationToken().getToken()).orElseThrow(EntityNotFoundException::new);
            confirmationTokenService.invalidateToken(confirmationToken);
        }

        ConfirmationToken confirmationToken = confirmationTokenService.createPasswordResetToken();
        setTokenForUser(user, confirmationToken);

        emailService.sendPasswordChangeMail(user.getEmail(), confirmationToken.getToken());

        return true;
    }


}
