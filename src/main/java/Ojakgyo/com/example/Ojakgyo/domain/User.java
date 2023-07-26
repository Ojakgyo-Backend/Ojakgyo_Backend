package Ojakgyo.com.example.Ojakgyo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //id
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    //psw
    @Column(length = 300, nullable = false)
    private String password;

    // 전화번호
    @Column(length = 100, nullable = false, unique = true)
    private String phone;

    // 닉네임
    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    // 비밀번호 수정
    public void updatePassword(String password){
        this.password = password;
    }

    // 회원정보 수정
    public void update(String password){
        this.password = password;
        this.updateAt = LocalDateTime.now();
    }

    // 회원 상태 수정
    public void updateStatus(String status){
        if(status.equals("A")){    //활동 중
            this.status = "A";
        } else if (status.equals("B")) {    //비활성화
            this.status = "B";
        } else if (status.equals("F")) { //회원 탈퇴
            this.status = "F";
        }
    }


}