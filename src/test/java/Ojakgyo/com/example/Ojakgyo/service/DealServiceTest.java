package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.User;
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

    public User createUser(String email, String phone){
        User user = User.builder()
                .id(1L)
                .email(email)
                .password("a12345678")
                .phone(phone)
                .name("personA")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status("A").build();
        return user;
    }

    @Test
    void isRoleTest() {
        //given
        User user = createUser("zzz", "01011112222");
        User dealer = createUser("qqq","01000011101");
        boolean isSeller = true;

        //when
        User users[] = dealService.isRole(user,dealer,isSeller);

        Assertions.assertThat(user).usingRecursiveComparison().isEqualTo(users[0]);
    }
}