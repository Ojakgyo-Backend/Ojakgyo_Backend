package Ojakgyo.com.example.Ojakgyo.repository;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void testMember() {
        User user = User.builder()
                .id(1L)
                .email("personA@naver.com")
                .password("a12345678")
                .phone("01012341234")
                .name("personA")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status("A").build();

        String savedEmail = userRepository.save(user).getEmail();
        User findMember = userRepository.findByEmail(savedEmail);
        Assertions.assertThat(findMember.getId()).isEqualTo(user.getId());

        Assertions.assertThat(findMember.getName()).isEqualTo(user.getName());
//        Assertions.assertThat(findMember).isEqualTo(user); //JPA 엔티티 동일성 보장
    }
}