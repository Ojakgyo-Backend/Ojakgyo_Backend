package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.config.auth.PrincipalDetails;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealRequest;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealerResponse;
import Ojakgyo.com.example.Ojakgyo.dto.SearchLockerResponse;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.NoSuchDataException;
import Ojakgyo.com.example.Ojakgyo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class RegisterDealController {
    private final DealService dealService;
    private final LockerService lockerService;
    private final UserService userService;

    // 락커 id로 조회
    @GetMapping("/search-locker")
    public SearchLockerResponse searchLocker(Authentication auth, Long lockerId){
        try {
            Locker findLocker = lockerService.findById(lockerId);
            // 검색한 락커 아이디가 없을 경우 에러 처리
            if (findLocker == null) {
                throw new NoSuchDataException(ErrorCode.LOCKER_NOT_EXIST);
            }
            SearchLockerResponse searchLockeresponse= SearchLockerResponse.builder()
                    .lockerId(lockerId)
                    .address(findLocker.getAddress())
                    .build();

            return searchLockeresponse;
        } catch (Exception e) {
            throw e;
        }
    }

//    // email (아이디)로 거래 대상자 조회
//    @GetMapping("/search-dealer")
//    public SearchDealerResponse searchDealer(Authentication auth, String email) {
//        try {
//            User findDealer = userService.findByEmail(email);
//            if (findDealer == null) {
//                throw new NoSuchDataException(ErrorCode.USER_NOT_EXIST);
//            }
//
//            return findDealer;
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    // 거래 등록
    @PostMapping
    public Object registerDeal(Authentication authentication, RegisterDealRequest request) throws IOException {
        try {
            User user = getPrincipalUser(authentication);
            dealService.registerDeal(request, user);
            return Map.of("result", "성공");
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping
    public User getUser(org.springframework.security.core.Authentication authentication) {
        return getPrincipalUser(authentication);
    }

    private User getPrincipalUser(org.springframework.security.core.Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getUser();
    }

}
