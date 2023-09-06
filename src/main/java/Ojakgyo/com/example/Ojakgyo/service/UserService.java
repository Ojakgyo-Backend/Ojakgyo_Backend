package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id){return userRepository.findById(id).get();}

    public void update(User user, String password) {
        user.update(password);
    }

    public void delete(String email) {
        User user = userRepository.findByEmail(email);
        user.updateStatus("F"); // 회원탈퇴 = F
    }

    // 이름, 전화번호로 아이디 찾기
    public User findByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone);
    }

    // 아이디, 전화번호로 비밀번호 찾기
    public User findByEmailAndPhone(String email, String phone) {
        return userRepository.findByEmailAndPhone(email, phone);
    }
}
