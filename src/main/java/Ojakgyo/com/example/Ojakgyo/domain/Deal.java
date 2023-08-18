package Ojakgyo.com.example.Ojakgyo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deal")
public class Deal {
    @Id
    @Column(name = "deal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 거래 상태 [BEFORE,DEALING,COMPLETED,CANCELED]
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealStatus dealStatus;

    @Column(nullable = false)
    String bank;

    @Column(nullable = false)
    String account;

    // 입금 현황
    @Column(nullable = false)
    private Boolean depositStatus;

    // 물품 상태
    @Column(length = 300)
    private String condition;

    @Column(length = 300, nullable = false)
    private String item;

    @Column(nullable = false)
    private Long price;

    // 판매자 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    // 구매자 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    // 락커 아이디 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")
    private Locker locker;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    /**
     * 비지니스 로직
     * **/

    //
    public void updateContract(Contract contract){
        this.contract = contract;
    }

    // 입금 현황 수정
    public void updateDepositStatus(Boolean depositStatus){

        this.depositStatus = depositStatus;
    }

    // 거래 상태 수정
    public void updateDealStatus(DealStatus dealStatus){

        this.dealStatus = dealStatus;
    }
}
