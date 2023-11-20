package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.config.auth.PrincipalDetails;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.*;
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
    private final ContractService contractService;

    @GetMapping(value ="/lockers", produces = "application/json; charset=UTF-8")
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

    //거래 파기
    @DeleteMapping("/delete")
    public Object deleteUser(Authentication authentication, @RequestParam long dealId) throws IOException {
        contractService.deleteContract(dealId);
        dealService.deleteDeal(dealId);
        return Map.of("result", "거래 파기 성공");
    }

    @GetMapping(value ="/search-dealer", produces = "application/json; charset=UTF-8")
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
