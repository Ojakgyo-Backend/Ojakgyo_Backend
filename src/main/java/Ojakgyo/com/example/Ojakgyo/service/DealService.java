package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.DealDetailsResponse;
import Ojakgyo.com.example.Ojakgyo.domain.DealerDealListInterface;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealRequest;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealerResponse;
import Ojakgyo.com.example.Ojakgyo.dto.UserDealListResponse;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository dealRepository;
    private final UserService userService;
    private final LockerService lockerService;

    public Deal createDeal(RegisterDealRequest request, User user){
        User users[] = isRole(user, findUser(request.getDealerId()), request.getIsSeller());

        Deal deal = Deal.builder()
                .dealStatus(DealStatus.DEALING)
                .bank(request.getBank())
                .account(request.getAccount())
                .depositStatus(Boolean.FALSE)
                .condition(request.getCondition())
                .item(request.getItemName())
                .price(request.getPrice())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .seller(users[0])
                .buyer(users[1])
                .locker(findLocker(request.getLockerId()))
                .build();
        return dealRepository.save(deal);
    }

    public SearchDealerResponse getDealerDealList(String email){
        User dealer = userService.findByEmail(email);
        List<DealerDealListInterface> dealLists = new ArrayList<>();
        dealLists.addAll(dealRepository.findDealerDealListBySellerId(dealer.getId()));
        dealLists.addAll(dealRepository.findDealerDealListByBuyerId(dealer.getId()));

        dealLists.sort(Comparator.comparing(DealerDealListInterface::getUpdateAt).reversed());

        SearchDealerResponse searchDealerResponse = SearchDealerResponse.builder()
                .email(dealer.getEmail())
                .name(dealer.getName())
                .phone(dealer.getPhone())
                .dealLists(dealLists).build();
        return searchDealerResponse;
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
                .dealStatus(String.valueOf(deal.getDealStatus()))
                .build();
        return dealDetailsResponse;
    }

    public List<UserDealListResponse> getUserDealList(Long userId){
        List<Deal> dealList = dealRepository.findAllByUserId(userId);
        List<UserDealListResponse> userDealList = new ArrayList<>();
        for(Deal deal : dealList){
            UserDealListResponse userDeal = UserDealListResponse.builder()
                    .dealStatus(deal.getDealStatus())
                    .item(deal.getItem())
                    .price(deal.getPrice())
                    .sellerName(deal.getSeller().getName())
                    .buyerName(deal.getBuyer().getName())
                    .createAt(deal.getCreateAt()).build();
            userDealList.add(userDeal);
        }
        userDealList.sort(Comparator.comparing(UserDealListResponse::getCreateAt).reversed());
        return userDealList;
    }

    private Locker findLocker(Long lockerId){
        return lockerService.findById(lockerId);
    }

    private User findUser(Long userId){
        return userService.findById(userId);
    }

    private User[] isRole(User user, User dealer, boolean isSeller){
        User users[] = new User[2];
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