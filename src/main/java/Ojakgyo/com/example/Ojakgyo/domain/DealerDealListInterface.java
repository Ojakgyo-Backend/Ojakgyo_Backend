package Ojakgyo.com.example.Ojakgyo.domain;

import java.time.LocalDateTime;

public interface DealerDealListInterface {
     DealStatus getDealStatus();
     String getItem();
     LocalDateTime getUpdateAt();
}
