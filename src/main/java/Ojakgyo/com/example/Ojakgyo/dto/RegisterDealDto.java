package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class RegisterDealDto {
    @Getter
    @Builder
    @Setter
    public static class Request {
        private String registrant; // 거래 등록자
        private String bank;
        private String account;
        private Long price;
        private String item;
        private String condition;
        private User buyer;  // 판매자
        private User seller; // 구매자
    }

    static class Response {

    }

}