package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class RegisterDealRequest{
    private String bank;
    private String account;
    private Long price;
    private String itemName;
    private String condition;
    private Long dealerId;
    private Long lockerId;
    private boolean isSeller;   //판매자면 True, 구매자면 False
}