package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LockerService {
    private final LockerRepository lockerRepository;

    // 락커 아이디로 락커 찾기
    public Locker findById(Long lockerId) {
        return lockerRepository.findById(lockerId).get();
    }
}
