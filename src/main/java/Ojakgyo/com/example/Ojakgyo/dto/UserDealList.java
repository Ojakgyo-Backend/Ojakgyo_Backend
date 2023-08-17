package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDealList {
    private Long dealId;
    private DealStatus dealStatus;
    private String item;
    private Long price;
    private String sellerName;
    private String buyerName;
    private LocalDateTime createAt;
}