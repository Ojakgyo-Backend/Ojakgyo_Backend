package Ojakgyo.com.example.Ojakgyo.service;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import Ojakgyo.com.example.Ojakgyo.dto.SearchDealHistoryDto;
import Ojakgyo.com.example.Ojakgyo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchDealHistoryService {
    private final DealRepository dealRepository;

    // 거래자 아이디로 거래 내역 찾기
    public Deal findByEmail(String email) {
        return dealRepository.findByEmail(email);
    }
}
