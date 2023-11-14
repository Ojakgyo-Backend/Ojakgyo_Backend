package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DealerDealList {
    private String item;
    private DealStatus dealStatus;
    private String updateAt;
}
