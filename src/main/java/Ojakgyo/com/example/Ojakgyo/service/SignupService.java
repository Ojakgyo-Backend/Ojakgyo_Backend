package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.UserSignupDto;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.SignupException;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;


    public User signup(UserSignupDto request) {
        checkDuplicateEmail(request.getEmail());
        User user = creatUser(request);
        return userRepository.save(user);
    }


    public void checkPhone(String phone) {
        Optional<User> user = Optional.ofNullable(userRepository.findByPhone(phone));
        if (user.isEmpty()) {
            throw new SignupException(ErrorCode.NOT_VERIFIED_PHONE);
        }
    }

    public void checkDuplicateEmail(String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isEmpty()) {
            throw new SignupException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    private User creatUser(UserSignupDto request) {
        User user = User.builder()
                .email((request.getEmail()))
                .password(request.getPassword())
                .phone(request.getPhone())
                .name(request.getName())
                .createAt(LocalDateTime.now())
                .status("A").build();
        return user;
    }
}
