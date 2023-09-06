package Ojakgyo.com.example.Ojakgyo.controller;

import Ojakgyo.com.example.Ojakgyo.dto.DealDetailsResponse;
import Ojakgyo.com.example.Ojakgyo.service.DealDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deal-details")
public class DealDtailsController {

    private final DealDetailService dealDetailService;

//    @GetMapping("/buyer-deposit-complete")
//    public Object buyerDepositComplete(Authentication authentication, @RequestParam Long dealId){
//        dealDetailService.completeBuyerDeposit(dealId);
//        return Map.of("result", "입금 전에서 완료로 상태 변경 성공");
//    }

    @GetMapping(value ="/seller-deposit-check", produces = "application/json; charset=UTF-8")
    public String sellerDepositCheck(Authentication authentication,@RequestParam Long dealID){
        dealDetailService.completeDeposit(dealID);
        String changedPassword = dealDetailService.changePassword(dealID);
        return changedPassword;
    }

    @GetMapping(value ="/buyer-deal-complete", produces = "application/json; charset=UTF-8")
    public Object buyerDealComplete(Authentication authentication,@RequestParam Long dealId){
        dealDetailService.completeBuyerDeal(dealId);
        return Map.of("result", "거래 중에서 거래완료로 거래 상태 변경 성공");
    }

    @GetMapping(value ="/update-deal-status", produces = "application/json; charset=UTF-8")
    public Object noContract(Authentication authentication,@RequestParam Long dealId){
        dealDetailService.changeDealing(dealId);
        return Map.of("result", "거래 전에서 거래중으로 변경 성공");
    }

    @GetMapping
    public DealDetailsResponse getDealDeatails(Authentication auth, @RequestParam Long dealId){
        return dealDetailService.getDealDetails(dealId);
    }


}
