package Ojakgyo.com.example.Ojakgyo.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class BlockChainContract {
    public BlockChainContract(){
    }

    private Long dealId;
    private String repAndRes;
    private String note;
    private Long price;

}
