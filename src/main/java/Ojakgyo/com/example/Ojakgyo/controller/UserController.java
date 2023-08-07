package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.config.auth.PrincipalDetails;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.MainDto;
import Ojakgyo.com.example.Ojakgyo.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static java.util.regex.Pattern.matches;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    //메인
    @GetMapping("/home")
    public MainDto home(Authentication auth) {
        MainDto mainDto = null;
        if (auth != null) {
            User loginUser = userService.findByEmail(auth.getName());
            if (loginUser != null) {
                mainDto = MainDto.builder()
                        .username(loginUser.getName()).build();
            }
        }
        return mainDto;
    }

    //비밀번호 변경
    @GetMapping("/user/update")
    public Object updateUser(@ModelAttribute String password, Authentication authentication) {
        User user = getPrincipalUser(authentication);
        userService.update(user,password);
        return Map.of("result", "비밀번호 변경 성공");
    }

    //회원탈퇴
    @DeleteMapping("/user/delete")
    public Object deleteUser(Authentication authentication) {
        userService.delete(getPrincipalUser(authentication).getEmail());
        return Map.of("result", "회원 탈퇴 성공성공");
    }

    @GetMapping
    public User getUser(Authentication authentication) {
        return getPrincipalUser(authentication);
    }

    private User getPrincipalUser(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getUser();
    }
}
