package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import Ojakgyo.com.example.Ojakgyo.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class SearchDealerDto {
    @Getter
    @Builder
    public static class Request {
        private String email;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        public Response(User dealer) {
            this.dealer = dealer;
        }
        private User dealer;
    }
}
