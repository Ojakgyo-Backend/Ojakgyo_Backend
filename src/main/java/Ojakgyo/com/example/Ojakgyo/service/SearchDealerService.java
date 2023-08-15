package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealerDto;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchDealerService {
    private final UserRepository userRepository;

    // 회원 이메일(아이디)로 거래 대상자 찾기
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
