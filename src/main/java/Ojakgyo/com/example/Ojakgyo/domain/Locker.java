package Ojakgyo.com.example.Ojakgyo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "locker")
public class Locker {
    @Id
    @Column(name = "locker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 락커 상태
    @Enumerated(EnumType.STRING)
    private LockerStatus lockerStatus; //[IN_USE,NOT_IN_USE]

    //락커 주소
    private String address;

    //락커 비밀번호
    private String password;

    // 락커 비밀번호 생성 시간
    private LocalDateTime createLockerPwdAt;


    public String updatePassword(){
        Random rnd =new Random();
        StringBuffer buf =new StringBuffer();
        for(int i=0;i<6;i++) {
            if (rnd.nextBoolean()) {
                buf.append((char) ((int) (rnd.nextInt(4)) + 65));
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }
        this.password = buf.toString();
        return this.password;
    }
}
