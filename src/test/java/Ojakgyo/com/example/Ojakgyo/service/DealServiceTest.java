package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.LockerStatus;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.LoginRequest;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealRequest;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealerResponse;
import Ojakgyo.com.example.Ojakgyo.repository.LockerRepository;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootTest
class DealServiceTest {

    @Autowired
    DealService dealService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LockerRepository lockerRepository;

    private User createUser(String email, String phone){
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

    private RegisterDealRequest createRequest(Long deallerId){
        RegisterDealRequest request = RegisterDealRequest.builder()
                .bank("하나")
                .account("12453")
                .price(124L)
                .itemName("맥북")
                .condition("좋음")
                .dealerId(deallerId)
                .lockerId(1L)
                .isSeller(true)
                .build();
        return request;
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
    void 거래대상자_조회_테스트(){
        //given
        User user = createUser("aaa", "01011112232");
        User dealer = createUser("bbb","01000011131");
        Locker locker = Locker.builder()
                .address("부산 광역시 용소로 45")
                .lockerStatus(LockerStatus.NOT_IN_USE)
                .password("1234")
                .createLockerPwdAt(LocalDateTime.now()).build();
        userRepository.save(user);
        userRepository.save(dealer);
        lockerRepository.save(locker);
        for(int i=0 ; i < 3; i++){
            dealService.createDeal(createRequest(dealer.getId()),user);
        }

        //when
        SearchDealerResponse searchDealerResponse = dealService.getDealerDealList(dealer.getEmail());

        //then
        Assertions.assertThat(dealer.getEmail()).isEqualTo(searchDealerResponse.getEmail());
    }

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
        Deal deal = dealService.findById(dealService.createDeal(request,user));

        //then
        Assertions.assertThat(user.getId()).isEqualTo(deal.getSeller().getId());
        Assertions.assertThat(dealer.getId()).usingRecursiveComparison().isEqualTo(deal.getBuyer().getId());
    }

    @Test
    void Json변환() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\n" +
                "    \"username\" : \"yj8200@naver.com\"\n" +
                "}";
        LoginRequest request = mapper.readValue(json,LoginRequest.class);

        System.out.println("request.getPassword() = " + request.getPassword());
        System.out.println("request.getUsername() = " + request.getUsername());
    }
}