package com.example.university.user.controller;

import com.example.university.confirmationToken.service.ConfirmationTokenService;
import com.example.university.user.model.dto.PasswordDto;
import com.example.university.user.model.dto.UserDto;
import com.example.university.user.model.dto.UserEmailDto;
import com.example.university.user.model.dto.UserRequest;
import com.example.university.user.model.entity.ChangePasswordRequest;
import com.example.university.user.model.entity.User;
import com.example.university.user.model.response.UserResponse;
import com.example.university.user.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        UserResponse user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponse> insert(@RequestBody UserDto userDto) {
        UserResponse user = userService.insert(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<UserResponse> add(@RequestBody User user) throws MessagingException, UnsupportedEncodingException {
        UserResponse userResponse = userService.addUser(user);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@RequestBody UserDto userDto) {
        UserResponse user = userService.update(userDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest, @PathVariable Long userId) {
        UserResponse user = userService.updateUser(userId, userRequest);
        return ResponseEntity.ok(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }


    @GetMapping("/run-python")
    public String runPython(@RequestParam(value = "args", required = false) String[] arguments) {
        try {
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add("C://Users//OSD-student1//Desktop//python//test.py");
            if (arguments != null) {
                for (String arg : arguments) {
                    command.add(arg);
                }
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return output.toString();
            } else {
                return "Greška pri izvršavanju Python skripte";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Greška u sistemu";
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, @PathVariable Long id) {
        userService.changePassword(request, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Boolean> sendPasswordResetEmail(@RequestBody UserEmailDto emailDTO) throws UnsupportedEncodingException, MessagingException {
        return ResponseEntity.ok(userService.sendPasswordResetEmail(emailDTO));
    }

    @GetMapping("/validate-request")
    public ResponseEntity<Void> validateRequest(@RequestParam("token") String token) throws AccessDeniedException {
        confirmationTokenService.validateToken(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password")
    public ResponseEntity<UserDto> savePassword(@RequestBody PasswordDto passwordDto) throws AccessDeniedException {
        return ResponseEntity.ok(userService.savePassword(passwordDto));
    }



}
