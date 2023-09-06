package Ojakgyo.com.example.Ojakgyo.dto;

import lombok.Data;

@Data
public class SignatureRequest {
    private Boolean isSeller;
    private String signature;
    private Long contractId;
}
