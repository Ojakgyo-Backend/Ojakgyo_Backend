package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.DealStatus;

import java.time.LocalDateTime;

public interface DealListInterface {
     DealStatus getDealStatus();
     String getItem();
     LocalDateTime getUpdateAt();
}
