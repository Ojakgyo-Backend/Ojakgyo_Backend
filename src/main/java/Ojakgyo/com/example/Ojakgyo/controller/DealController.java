package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.config.auth.PrincipalDetails;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.*;
import Ojakgyo.com.example.Ojakgyo.exception.ErrorCode;
import Ojakgyo.com.example.Ojakgyo.exception.NoSuchDataException;
import Ojakgyo.com.example.Ojakgyo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;
    private final LockerService lockerService;

    // 락커 id로 조회
    @GetMapping("/search-locker")
    public SearchLockerResponse searchLocker(Authentication auth, @RequestParam Long lockerId){
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

    @GetMapping("/lockers")
    public List<SearchLockerResponse> getLockers(Authentication authentication){
        return lockerService.findAll();
    }


    // 거래 등록
    @PostMapping
    public Long registerDeal(Authentication authentication,@RequestBody RegisterDealRequest request) throws IOException {
        try {
            User user = getPrincipalUser(authentication);
            Long DealId = dealService.createDeal(request, user);
            return DealId;
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/search-dealer")
    public SearchDealerResponse searchDealer(Authentication authentication, @RequestParam String dealerEmail) throws IOException{
        try {
            System.out.println("dealerEmail = " + dealerEmail);
            SearchDealerResponse searchDealerResponse = dealService.getDealerDealList(dealerEmail);
            return searchDealerResponse;
        } catch (Exception e) {
            throw e;
        }
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
