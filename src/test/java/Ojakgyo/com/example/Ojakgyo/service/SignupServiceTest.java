package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.UserSignupDto;
import Ojakgyo.com.example.Ojakgyo.exception.SignupException;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SignupServiceTest {
    @Autowired
    SignupService signupService;
    @Autowired
    UserRepository userRepository;

    public UserSignupDto createDto(){
        UserSignupDto userSignupDto = UserSignupDto.builder()
                .email("ljj020304@gmail.com")
                .password("abc12341234")
                .phone("01011112222")
                .name("홍길동").build();
        return userSignupDto;
    }

    @Test
    void signupTest() {
        UserSignupDto userSignupDto = createDto();

        signupService.signup(userSignupDto);
        User savedUser = userRepository.findByEmail(userSignupDto.getEmail());

        Assertions.assertThat(savedUser.getEmail()).isEqualTo(userSignupDto.getEmail());
    }

    @Test
    void checkPhoneUnsuccessfulTest() {
        //given
        UserSignupDto userSignupDto = createDto();
        signupService.signup(userSignupDto);

        //then
        Assertions.assertThatThrownBy(() -> signupService.checkDuplicatePhone(userSignupDto.getPhone()))
                .isInstanceOf(SignupException.class);

    }
    @Test
    void checkPhoneSuccessfulTest() {
        //given
        UserSignupDto userSignupDto = createDto();

        //when
        signupService.checkDuplicatePhone(userSignupDto.getPhone());
    }

    @Test
    void checkEmailUnsuccessfulTest() {
        //given
        UserSignupDto userSignupDto = createDto();
        signupService.signup(userSignupDto);

        //then
        Assertions.assertThatThrownBy(() -> signupService.checkDuplicateEmail(userSignupDto.getEmail()))
                .isInstanceOf(SignupException.class);
    }

    @Test
    void checkEmailSuccessfulTest() {
        //given
        UserSignupDto userSignupDto = createDto();

        //when
        signupService.checkDuplicateEmail(userSignupDto.getEmail());
    }
}