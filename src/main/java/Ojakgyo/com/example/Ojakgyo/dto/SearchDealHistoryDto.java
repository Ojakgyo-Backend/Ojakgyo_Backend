package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import lombok.*;

import java.time.LocalDateTime;

public class SearchDealHistoryDto {
    @Getter
    @Builder
    public static class Request {
        private String Email;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        public Response(DealStatus dealStatus, String item, LocalDateTime updateAt) {
            this.dealStatus = dealStatus;
            this.item = item;
            this.updateAt = updateAt;
        }
        private DealStatus dealStatus;
        private String item;
        private LocalDateTime updateAt;
    }
}
