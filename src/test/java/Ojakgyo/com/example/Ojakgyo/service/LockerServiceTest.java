package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.repository.LockerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class LockerServiceTest {
    @Autowired
    LockerRepository lockerRepository;
    @Autowired
    LockerService lockerService;


    @Test
    void 주소찾기_trst(){
        String adress = "부산";
        Locker locker = lockerService.createLocker(adress,"1234");
        lockerRepository.save(locker);
        Locker findLocker = lockerService.findByAddress(adress);

        Assertions.assertThat(findLocker.getAddress()).isEqualTo(adress);

    }
}