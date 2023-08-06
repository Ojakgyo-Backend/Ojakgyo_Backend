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
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 거래 상태
    @Column(nullable = false)
    private String dealStatus;

    // 입금 현황
    @Column(nullable = false)
    private String depositStatus;

    @Column(length = 300, nullable = false)
    private String condition;

    @Column(nullable = false)
    private LocalDateTime dealTime;

    @Column(length = 300, nullable = false)
    private String dealPlace;

    @Column(length = 300, nullable = false)
    private String item;

    @Column(nullable = false)
    private Long price;

    // 판매자 이름 (FK)
    @Column(length = 100, nullable = false)
    private String sellerName;

    // 판매자 전화번호 (FK)
    @Column(length = 100, nullable = false, unique = true)
    private String sellerCellphone;

    // 판매자 계좌번호 (FK)
    @Column(length = 100, nullable = false, unique = true)
    private String sellerAccount;

    // 판매자 계좌 은행 (FK)
    @Column(length = 100, nullable = false)
    private String sellerAccountBank;

    // 구매자 이름 (FK)
    @Column(length = 100, nullable = false)
    private String buyerName;

    // 구매자 전화번호 (fK)
    @Column(length = 100, nullable = false, unique = true)
    private String buyerCellphone;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    // 락커 아이디 (FK)
    @Column(nullable = false)
    private Long lockerId;

    // 락커 비밀번호
    @Column(length = 100, nullable = false)
    private String lockerPwd;

    // 락커 비밀번호 생성 시간
    @Column(length = 100)
    private String createLockerPwdAt;



    // 락커 비밀번호 생성 시간 수정
    public void updateCreateLockerPwdAt(String createLockerPwdAt){
        this.createLockerPwdAt = createLockerPwdAt;
    }

    // 락커 비밀번호 수정
    public void updateLockerPwd(String lockerPwd){
        this.lockerPwd = lockerPwd;
    }

    // 입금 현황 수정
    public void updateDepositStatus(String depositStatus){
        this.depositStatus = depositStatus;
    }

    // 거래 상태 수정
    public void updateDealStatus(String dealStatus){
        this.dealStatus = dealStatus;
    }
}
