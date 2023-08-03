package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchUserService {
    private final UserRepository userRepository;

    // 이름, 전화번호로 아이디 찾기
    public User findByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone);
    }

    // 아이디, 전화번호로 비밀번호 찾기
    public User findByIdAndPhone(Long id, String phone) {
        return userRepository.findByIdAndPhone(id, phone);
    }
}