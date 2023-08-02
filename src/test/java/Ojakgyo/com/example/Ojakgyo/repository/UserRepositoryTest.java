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

    public User createUser(){
        User user = User.builder()
                .id(1L)
                .email("personA@naver.com")
                .password("a12345678")
                .phone("01012341234")
                .name("personA")
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .status("A").build();
        return user;
    }

    @Test
    public void saveMemberTest() {
        User user = createUser();

        String savedEmail = userRepository.save(user).getEmail();

//        Assertions.assertThat(findMember).isEqualTo(user); //JPA 엔티티 동일성 보장
    }

    @Test
    public void findByEmailTest() {
        User user = createUser();

        String savedEmail = userRepository.save(user).getEmail();
        User findMember = userRepository.findByEmail(savedEmail);
        Assertions.assertThat(findMember.getEmail()).isEqualTo(user.getEmail());

//        Assertions.assertThat(findMember).isEqualTo(user); //JPA 엔티티 동일성 보장
    }

    @Test
    public void findByPhoneTest() {
        User user = createUser();

        String savedPhone = userRepository.save(user).getPhone();
        User findMember = userRepository.findByPhone(savedPhone);
        Assertions.assertThat(findMember.getPhone()).isEqualTo(user.getPhone());

//        Assertions.assertThat(findMember).isEqualTo(user); //JPA 엔티티 동일성 보장
    }

    @Test
    public void deleteUserTest() {
        User user = createUser();
        Long savedId = userRepository.save(user).getId();

        userRepository.deleteById(savedId);



//        Assertions.assertThat(findMember).isEqualTo(user); //JPA 엔티티 동일성 보장
    }
}