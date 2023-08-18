package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.dto.UserSignupDto;
import Ojakgyo.com.example.Ojakgyo.repository.UserRepository;
import Ojakgyo.com.example.Ojakgyo.service.SignupService;
import Ojakgyo.com.example.Ojakgyo.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchUserControllerTest {
    @Autowired
    SearchUserController searchUserController;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SignupService signupService;

    public UserSignupDto createDto(){
        UserSignupDto userSignupDto = UserSignupDto.builder()
                .email("ljj020304@gmail.com")
                .password("abc12341234")
                .phone("01011112222")
                .name("홍길동").build();
        return userSignupDto;
    }

    @Test
    void searchIdSuccess() {
        //given
        UserSignupDto userSignupDto = createDto();
        signupService.signup(userSignupDto);

        //then
        Assertions.assertThat(userService.findByNameAndPhone(userSignupDto.getName(),userSignupDto.getPhone()));
    }

    @Test
    void searchIdUnSuccess() {
        //given
        UserSignupDto userSignupDto = createDto();

        //then
        Assertions.assertThatThrownBy(() -> searchUserController.searchId(userSignupDto.getName(),userSignupDto.getPhone()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void searchPasswordSuccess() {
        //given
        UserSignupDto userSignupDto = createDto();
        signupService.signup(userSignupDto);

        //then
        Assertions.assertThat(userService.findByEmailAndPhone(userSignupDto.getEmail(),userSignupDto.getPhone()));
    }
    @Test
    void searchPasswordUnSuccess() {
        //given
        UserSignupDto userSignupDto = createDto();

        //then
        Assertions.assertThatThrownBy(() -> searchUserController.searchPassword(userSignupDto.getEmail(),userSignupDto.getPhone()))
                .isInstanceOf(NullPointerException.class);
    }
}