package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.*;
import Ojakgyo.com.example.Ojakgyo.dto.DealDetailsResponse;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import Ojakgyo.com.example.Ojakgyo.repository.LockerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static Ojakgyo.com.example.Ojakgyo.domain.Utils.changeDateFormat;

@Service
@RequiredArgsConstructor
public class DealDetailService {
    private final DealRepository dealRepository;
    private final LockerRepository lockerRepository;
    private final LockerService lockerService;
    private final UserService userService;

    // 비밀번호 리스트 정의
    public static List<String> passwordList = Arrays.asList("95AC02", "434D11", "329D15", "2BA392", "92CC07", "A8A261", "BA3205", "AD2242", "DD3043", "109B17");
    public static int currentPasswordIndex = 0;

    public void completeBuyerDeal(Long dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        deal.updateDealStatus(DealStatus.COMPLETED);
        dealRepository.save(deal);
    }

    public String changePassword(Long dealID) {
        Deal deal = dealRepository.findDealById(dealID);
        Locker locker = lockerService.findById(deal.getLocker().getId());
//        String changedPassword = locker.updatePassword();
        /** 시연을 위한 락커 비밀 번호 고정 , 원래는 위 주석 풀면됨**/
        String changedPassword = passwordList.get(currentPasswordIndex);
        locker.setPassword(changedPassword);

        // 비밀번호 인덱스 업데이트
        currentPasswordIndex = (currentPasswordIndex + 1) % passwordList.size();

        /** 시연 코드 끝**/

        lockerRepository.save(locker);
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
