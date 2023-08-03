package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static java.util.regex.Pattern.matches;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;



    // 로그인
    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Object> login(@RequestBody Map<String, String> request, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(request.get("email"));
        if (!matches(request.get("password"), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return new ResponseEntity<>(Map.of("result", "로그인 성공"), HttpStatus.OK);
    }

    //비밀번호 변경
    @GetMapping("/user/update")
    public Object updateUser(@ModelAttribute String password, String email) {
        User loginMember = userService.findByEmail(email);
        userService.update(email,password);
        return Map.of("result", "비밀번호 변경 성공");
    }

    //회원탈퇴
    @DeleteMapping
    public Object deleteUser(String email) {
        userService.delete(email);
        return Map.of("result", "회원 탈퇴 성공성공");
    }

}
