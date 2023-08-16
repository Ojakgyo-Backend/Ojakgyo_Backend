package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.LockerStatus;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealRequest;
import Ojakgyo.com.example.Ojakgyo.repository.LockerRepository;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DealServiceTest {

    @Autowired
    DealService dealService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LockerRepository lockerRepository;

    public User createUser(String email, String phone){
        User user = User.builder()
                .email(email)
                .password("a12345678")
                .phone(phone)
                .name("personA")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status("A").build();
        return user;
    }

//    @Test
//    void isRoleTest() {
//        //given
//        User user = createUser("zzz", "01011112222");
//        User dealer = createUser("qqq","01000011101");
//        boolean isSeller = true;
//
//        //when
//        User users[] = dealService.isRole(user,dealer,isSeller);
//
//        Assertions.assertThat(user).usingRecursiveComparison().isEqualTo(users[0]);
//    }

    @Test
    void 거래등록_테스트(){
        //given
        User user = createUser("zzz", "01011112222");
        User dealer = createUser("qqq","01000011101");
        Locker locker = Locker.builder()
                .address("부산 광역시 용소로 45")
                .lockerStatus(LockerStatus.NOT_IN_USE)
                .password("1234")
                .createLockerPwdAt(LocalDateTime.now()).build();
        userRepository.save(user);
        userRepository.save(dealer);
        lockerRepository.save(locker);
        RegisterDealRequest request = RegisterDealRequest.builder()
                .bank("하나")
                .account("12453")
                .price(124L)
                .itemName("맥북")
                .condition("좋음")
                .dealerId(dealer.getId())
                .lockerId(1L)
                .isSeller(true)
        .build();

        //when
        Deal deal = dealService.registerDeal(request,user);

        //then
        Assertions.assertThat(user.getId()).isEqualTo(deal.getSeller().getId());
        Assertions.assertThat(dealer.getId()).usingRecursiveComparison().isEqualTo(deal.getBuyer().getId());
    }
}