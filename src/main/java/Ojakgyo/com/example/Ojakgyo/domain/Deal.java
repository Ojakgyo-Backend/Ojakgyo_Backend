package Ojakgyo.com.example.Ojakgyo.domain;

import lombok.*;
import net.bytebuddy.asm.Advice;

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

    // 거래 상태 [DEALING,COMPLETED,CANCELED]
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealStatus dealStatus;

    // 입금 현황 [0,1]
    @Column(nullable = false)
    private Integer depositStatus;

    // 물품 상태
    @Column(length = 300)
    private String condition;

    @Column(length = 300, nullable = false)
    private String item;

    @Column(nullable = false)
    private Long price;

    // 판매자 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @Column(length = 100, nullable = false)
    private User seller;

    // 구매자 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @Column(length = 100, nullable = false)
    private User buyer;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    // 락커 아이디 (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")
    @Column(nullable = false)
    private Locker lockerId;


    /**
     * 비지니스 로직
     * **/


    // 입금 현황 수정
    public void updateDepositStatus(Integer depositStatus){
        this.depositStatus = depositStatus;
    }

    // 거래 상태 수정
    public void updateDealStatus(DealStatus dealStatus){
        this.dealStatus = dealStatus;
    }
}
