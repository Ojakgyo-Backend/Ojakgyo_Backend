package Ojakgyo.com.example.Ojakgyo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contract")
public class Contract {
    @Id
    @Column(name="contract_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //배상 및 책임
    @Column(columnDefinition = "TEXT")
    private String repAndRes;

    //기타 사항
    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(columnDefinition = "TEXT")
    private String sellerSignature;

    @Column(columnDefinition = "TEXT")
    private String buyerSignature;

    @OneToOne(mappedBy = "contract")
    private Deal deal;

    // 서명 저장
    public void setSignature(Boolean isSeller,String signature){
        if(isSeller){
            this.sellerSignature = signature;
        }
        else{
            this.buyerSignature = signature;
        }
    }
}
