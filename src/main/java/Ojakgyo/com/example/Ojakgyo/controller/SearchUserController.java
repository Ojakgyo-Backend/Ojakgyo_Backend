package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.NoSuchDataException;
import Ojakgyo.com.example.Ojakgyo.service.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SearchUserController {
    private final SearchUserService searchUserService;

    // 아이디 찾기
    @PostMapping("/login/getId")
    public Long searchId(@RequestParam String name, String phone) throws IOException {
        try {
            User findUser = searchUserService.findByNameAndPhone(name, phone);
            Long findUserId = findUser.getId();
            if (findUserId == null) {
                throw new NoSuchDataException(ErrorCode.USER_NOT_EXIST);
            }
            return findUserId;
        } catch (Exception e) {
            throw e;
        }
    }


    // 비밀번호 찾기
    @PostMapping("/login/getPassword")
    public String searchPassword(@RequestParam Long id, String phone) throws IOException {
        try {
            User findUser = searchUserService.findByIdAndPhone(id, phone);
            String findUserPwd = findUser.getPassword();
            if (findUserPwd == null){
                throw new NoSuchDataException(ErrorCode.USER_NOT_EXIST);
            }
            return findUserPwd;
        } catch (Exception e) {
            throw e;
        }
    }

}
