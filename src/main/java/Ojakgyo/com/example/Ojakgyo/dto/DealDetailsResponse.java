package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DealDetailsResponse {
    private Long contactId;
    private Long lockerId;
    private String lockerAddress;
    private String sellerName;
    private String sellerPhone;
    private String buyerName;
    private String buyerPhone;
    private String bank;
    private String account;
    private Long price;
    private String itemName;
    private String condition;
    private Boolean depositStatus;
    private String lockerPassword;
    private LocalDateTime createLockerPwdAt;
    private String dealStatus;

}
