package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class SearchLockerResponse {
    private Long lockerId;
    private String address;
}
