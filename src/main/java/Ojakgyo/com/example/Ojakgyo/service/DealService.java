package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import Ojakgyo.com.example.Ojakgyo.domain.Locker;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealRequest;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository dealRepository;
    private final UserService userService;
    private final LockerService lockerService;

    public Deal registerDeal(RegisterDealRequest request, User user){
        User users[] = isRole(user, findUser(request.getDealerId()), request.isSeller());

        Deal deal = Deal.builder()
                .dealStatus(DealStatus.DEALING)
                .depositStatus(0)
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