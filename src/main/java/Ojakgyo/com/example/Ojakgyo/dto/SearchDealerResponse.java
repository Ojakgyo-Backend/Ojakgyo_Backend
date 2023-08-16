package Ojakgyo.com.example.Ojakgyo.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchDealerResponse {
    private String email;
    private String name;
    private String phone;
    private List<DealListInterface> dealLists;
}
