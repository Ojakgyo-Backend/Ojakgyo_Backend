package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import Ojakgyo.com.example.Ojakgyo.dto.RegisterDealDto;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealHistoryDto;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealerDto;
import Ojakgyo.com.example.Ojakgyo.dto.SearchLockerDto;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterDealService {
    private final DealRepository dealRepository;

    public Deal registerDeal(RegisterDealDto.Request request, SearchLockerDto.Response locker){
        Deal deal = createDeal(request, locker);
        return dealRepository.save(deal);
    }

    private Deal createDeal(RegisterDealDto.Request request, SearchLockerDto.Response locker) {
        Deal deal = Deal.builder()
                .dealStatus(DealStatus.DEALING)
                .depositStatus(0)
                .condition(request.getCondition())
                .item(request.getItem())
                .price(request.getPrice())
                .seller(request.getSeller())
                .buyer(request.getBuyer())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .lockerId(locker.getLockerId()).build();
        return deal;
    }

}