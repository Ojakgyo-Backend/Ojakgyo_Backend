package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.*;
import Ojakgyo.com.example.Ojakgyo.dto.*;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static Ojakgyo.com.example.Ojakgyo.domain.Utils.changeDateFormat;

@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository dealRepository;
    private final UserService userService;
    private final LockerService lockerService;

    public Long createDeal(RegisterDealRequest request, User user){
        User[] users = isRole(user, findUser(request.getDealerId()), request.getIsSeller());

        Deal deal = Deal.builder()
                .dealStatus(DealStatus.BEFORE)
                .bank(request.getBank())
                .account(request.getAccount())
                .depositStatus(DepositStatus.BUYER_DEPOSIT_BEFORE)
                .itemCondition(request.getCondition())
                .item(request.getItemName())
                .price(request.getPrice())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .seller(users[0])
                .buyer(users[1])
                .locker(findLocker(request.getLockerId()))
                .build();
        return dealRepository.save(deal).getId();
    }

    public void deleteDeal(long id){
        validateDealId(id);
        dealRepository.deleteById(id);
    }

    private void validateDealId(long dealId) {
        Optional<Deal> deal = dealRepository.findById(dealId);
        if (!deal.isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 dealId");
        }
    }

    public SearchDealerResponse getDealerDealList(String email){
        User dealer = userService.findByEmail(email);
        List<DealerDealListInterface> dealLists = new ArrayList<>();
        dealLists.addAll(dealRepository.findDealerDealListBySellerId(dealer.getId()));
        dealLists.addAll(dealRepository.findDealerDealListByBuyerId(dealer.getId()));

        //날짜 순으로 정렬
        dealLists.sort(Comparator.comparing((DealerDealListInterface dealerDealListInterface) -> dealerDealListInterface.getUpdateAt()).reversed());

        List<DealerDealList> dealerDealLists = new ArrayList<>();
        for (DealerDealListInterface dealListInterface : dealLists) {
            DealerDealList dealerDealList = DealerDealList.builder()
                    .item(dealListInterface.getItem())
                    .dealStatus(dealListInterface.getDealStatus())
                    .updateAt(changeDateFormat(dealListInterface.getUpdateAt()))
                    .build();
            dealerDealLists.add(dealerDealList);
        }

        return SearchDealerResponse.builder()
                .dealerId(dealer.getId())
                .email(dealer.getEmail())
                .name(dealer.getName())
                .phone(dealer.getPhone())
                .dealLists(dealerDealLists).build();
    }


    public MainResponse getMainRes(User user){
        return MainResponse.builder()
                .user(user)
                .userDealLists(getUserDealList(user.getId())).build();
    }


    private List<UserDealList> getUserDealList(Long userId){
        List<Deal> dealList = dealRepository.findAllByUserId(userId);
        List<UserDealList> userDealList = new ArrayList<>();
        for(Deal deal : dealList){
            UserDealList userDeal = UserDealList.builder()
                    .dealId(deal.getId())
                    .dealStatus(deal.getDealStatus())
                    .item(deal.getItem())
                    .price(deal.getPrice())
                    .sellerName(deal.getSeller().getName())
                    .buyerName(deal.getBuyer().getName())
                    .createAt(changeDateFormat(deal.getCreateAt())).build();
            userDealList.add(userDeal);
        }
        userDealList.sort(Comparator.comparing(UserDealList::getCreateAt).reversed());
        return userDealList;
    }

    public Deal findById(long dealId){
        return dealRepository.findDealById(dealId);
    }

    private Locker findLocker(Long lockerId){
        return lockerService.findById(lockerId);
    }

    private User findUser(Long userId){
        return userService.findById(userId);
    }

    private User[] isRole(User user, User dealer, boolean isSeller){
        User[] users = new User[2];
        if(isSeller){
            users[0] = user;
            users[1] = dealer;
        }
        else {
            users[0] = dealer;
            users[1] = user;
        }
        return users;
    }


}