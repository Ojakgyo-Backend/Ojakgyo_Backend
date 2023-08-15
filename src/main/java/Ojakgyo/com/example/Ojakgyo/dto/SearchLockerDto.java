package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import lombok.*;

import java.time.LocalDateTime;


public class SearchLockerDto {
    @Getter
    public static class Request {
        private Long lockerId;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        public Response(Long lockerId, String address) {
            this.lockerId = lockerId;
            this.address = address;
        }
        private Long lockerId;
        private String address;
    }
}
