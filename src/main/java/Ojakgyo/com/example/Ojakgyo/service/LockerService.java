package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.LockerStatus;
import Ojakgyo.com.example.Ojakgyo.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LockerService {
    private final LockerRepository lockerRepository;

    public Locker createLocker(String address, String password){
        Locker locker = Locker.builder()
                .lockerStatus(LockerStatus.NOT_IN_USE)
                .address(address)
                .password(password)
                .createLockerPwdAt(LocalDateTime.now()).build();
        return lockerRepository.save(locker);
    }

    // 락커 아이디로 락커 찾기
    public Locker findById(Long lockerId) {
        return lockerRepository.findById(lockerId).get();
    }
}
