package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.*;
import Ojakgyo.com.example.Ojakgyo.dto.DealDetailsResponse;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static Ojakgyo.com.example.Ojakgyo.domain.Utils.changeDateFormat;

@Service
@RequiredArgsConstructor
public class DealDetailService {
    private final DealRepository dealRepository;
    private final LockerService lockerService;
    private final UserService userService;

    public void completeBuyerDeal(Long dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDealStatus(DealStatus.COMPLETED);

        // 시연을 위한 락커 비밀 번호 고정 거래 완료하면 다시 첫번쨰 비밀 번호로
        Locker locker = deal.getLocker();
        locker.setPassword("95AC02");
        lockerService.saveLocker(locker);
        // 시연 코드 끝

        dealRepository.save(deal);
    }

    public String changePassword(Long dealID) {
        Deal deal = dealRepository.findDealById(dealID);
        Locker locker = lockerService.findById(deal.getLocker().getId());
//        String changedPassword = locker.updatePassword();
        String changedPassword = "434D11";  //시연을 위한 비번 고정
        lockerService.saveLocker(locker);
        return changedPassword;
    }

    public void changeDealing(Long dealId){
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDealStatus(DealStatus.DEALING);
        dealRepository.save(deal);
    }

    public void buyerCompleteDeposit(Long dealId){
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDepositStatus(DepositStatus.BUYER_DEPOSIT_COMPLETE);
        dealRepository.save(deal);
    }

    public void sellerCheckDeposit(Long dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDepositStatus(DepositStatus.SELLER_DEPOSIT_CHECK);
        dealRepository.save(deal);
    }

    public DealDetailsResponse getDealDetails(Long dealId){
        Deal deal = dealRepository.findDealById(dealId);
        Locker locker = lockerService.findById(deal.getLocker().getId());
        User seller = findUser(deal.getSeller().getId());
        User buyer = findUser(deal.getBuyer().getId());
        return DealDetailsResponse.builder()
                .contactId(findContract(deal.getContract()))
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
                .condition(deal.getItemCondition())
                .depositStatus(String.valueOf(deal.getDepositStatus()))
                .lockerPassword(locker.getPassword())
                .createLockerPwdAt(changeDateFormat(locker.getCreateLockerPwdAt()))
                .createAtDeal(changeDateFormat(deal.getCreateAt()))
                .dealStatus(String.valueOf(deal.getDealStatus()))
                .build();
    }

    private Long findContract(Contract contract){
        Long contractId;
        if(contract == null){
            contractId = -1L;
        }
        else{
            contractId = contract.getId();
        }
        return contractId;
    }

    private User findUser(Long userId){
        return userService.findById(userId);
    }



}
