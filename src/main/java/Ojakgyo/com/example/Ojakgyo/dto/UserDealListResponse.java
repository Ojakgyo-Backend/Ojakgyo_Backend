package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDealListResponse {
    private DealStatus dealStatus;
    private String item;
    private Long price;
    private String sellerName;
    private String buyerName;
    private LocalDateTime createAt;
}