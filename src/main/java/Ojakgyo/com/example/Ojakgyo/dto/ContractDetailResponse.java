package Ojakgyo.com.example.Ojakgyo.dto;

import Ojakgyo.com.example.Ojakgyo.domain.Deal;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
public class ContractDetailResponse {
    private String repAndRes;
    private String note;
    private String sellerSignature;
    private String buyerSignature;
}
