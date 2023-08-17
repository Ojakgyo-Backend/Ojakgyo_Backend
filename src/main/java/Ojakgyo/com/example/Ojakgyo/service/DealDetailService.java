package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.DealDetailsResponse;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DealDetailService {
    private final DealRepository dealRepository;
    private final LockerService lockerService;
    private final UserService userService;

    public void completeBuyerDeal(Long dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDealStatus(DealStatus.COMPLETED);
        dealRepository.save(deal);
    }

    public String changePassword(Long dealID) {
        Deal deal = dealRepository.findDealById(dealID);
        Locker locker = lockerService.findById(deal.getLocker().getId());
        String changedPassword = locker.updatePassword();
        lockerService.saveLocker(locker);
        return changedPassword;
    }

    public void changeDealing(Long dealId){
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDealStatus(DealStatus.DEALING);
        dealRepository.save(deal);
    }

    public void completeBuyerDeposit(Long dealId){
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDepositStatus(Boolean.TRUE);
        dealRepository.save(deal);
    }

    public DealDetailsResponse getDealDetails(Long dealId){
        Deal deal = dealRepository.findDealById(dealId);
        Locker locker = lockerService.findById(deal.getLocker().getId());
        User seller = findUser(deal.getSeller().getId());
        User buyer = findUser(deal.getBuyer().getId());
        DealDetailsResponse dealDetailsResponse = DealDetailsResponse.builder()
                .lockerId(locker.getId())
                .lockerAddress(locker.getAddress())
                .sellerName(seller.getName())
                .sellerPhone(seller.getPhone())
                .buyerName(buyer.getName())
                .buyerPhone(buyer.getPhone())
                .bank(deal.getBank())
                .account(deal.getAccount())
                .price(deal.getPrice())
                .itemName(deal.getItem())
                .condition(deal.getCondition())
                .depositStatus(deal.getDepositStatus())
                .lockerPassword(locker.getPassword())
                .createLockerPwdAt(locker.getCreateLockerPwdAt())
                .dealStatus(String.valueOf(deal.getDealStatus()))
                .build();
        return dealDetailsResponse;
    }

    private User findUser(Long userId){
        return userService.findById(userId);
    }


}
