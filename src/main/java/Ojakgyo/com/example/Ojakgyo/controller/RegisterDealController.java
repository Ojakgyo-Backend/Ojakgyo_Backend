package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.config.auth.PrincipalDetails;
import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealDto;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealHistoryDto;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealerDto;
import Ojakgyo.com.example.Ojakgyo.dto.SearchLockerDto;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.NoSuchDataException;
import Ojakgyo.com.example.Ojakgyo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import Ojakgyo.com.example.Ojakgyo.controller.UserController;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class RegisterDealController {
    private final RegisterDealService registerDealService;
    private final SearchLockerService searchLockerService;
    private final SearchDealerService searchDealerService;
    private final SearchDealHistoryService searchDealHistoryService;
    private final UserService userService;
    private final UserController userController;

    // 락커 id로 조회
    @GetMapping("/deal/search-locker")
    public SearchLockerDto.Response searchLocker(Authentication auth, SearchLockerDto.Request requestLockerId){
        try {
            Long lockerId = requestLockerId.getLockerId();
            Optional<Locker> findLocker = searchLockerService.findById(lockerId);
            // 검색한 락커 아이디가 없을 경우 에러 처리
            if (findLocker == null) {
                throw new NoSuchDataException(ErrorCode.LOCKER_NOT_EXIST);
            }
            SearchLockerDto.Response response = new SearchLockerDto.Response(findLocker.get().getId(), findLocker.get().getAddress());

            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    // email (아이디)로 거래 대상자 조회
    @GetMapping("/deal/search-dealer")
    public SearchDealerDto.Response searchDealer(Authentication auth, SearchDealerDto.Request requestEmail) {
        try {
            String email = requestEmail.getEmail();
            User findDealer = searchDealerService.findByEmail(email);
            if (findDealer == null) {
                throw new NoSuchDataException(ErrorCode.USER_NOT_EXIST);
            }
            SearchDealerDto.Response response = new SearchDealerDto.Response(findDealer);
//            response.setName(findDealer.getName());
//            response.setPhone(findDealer.getPhone());

            return response;
        } catch (Exception e) {
            throw e;
        }
    }

    // 거래 대상자의 거래 내역 간단 조회
    @GetMapping("deal/search-dealer/history")
    public SearchDealHistoryDto.Response showDealHistory(Authentication auth, SearchDealHistoryDto.Request requestEmail) {
        try {
            String email = requestEmail.getEmail();
            Deal findDealHistory = searchDealHistoryService.findByEmail(email);

            if (findDealHistory == null) {
                throw new NoSuchDataException(ErrorCode.USER_NOT_EXIST);
            }

            SearchDealHistoryDto.Response response = new SearchDealHistoryDto.Response(findDealHistory.getDealStatus(), findDealHistory.getItem(), findDealHistory.getUpdateAt());
//            response.setDealStatus(findDealHistory.getDealStatus());
//            response.setItem(findDealHistory.getItem());
//            response.setUpdateAt(findDealHistory.getUpdateAt());

            return response;
        } catch (Exception e){
            throw e;
        }
    }

    // 거래 등록
    @PostMapping
    public Object registerDeal(Authentication authentication, RegisterDealDto.Request request, SearchLockerDto.Response locker, SearchDealerDto.Response responseDealer) throws IOException {
        try {
            User user = getPrincipalUser(authentication);
            User dealer = responseDealer.getDealer();
            if (request.getRegistrant() == "is_buyer"){
                request.setBuyer(user);
                request.setSeller(dealer);
            } else {
                request.setBuyer(dealer);
                request.setSeller(user);
            }
            registerDealService.registerDeal(request, locker);
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
