package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.dto.SignUpForm;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Seller extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;       // 이메일 정보
    private String name;        // 판매자 이름
    private String password;    // 비밀번호
    private String phone;       // 휴대폰 번호
    private LocalDate birth;    // 생일

    private LocalDateTime verifyExpiredAt;  // 이메일 인증 유효 기간
    private String verificationCode;        // 이메일 인증 코드
    private boolean verify;                 // 이메일 유효성 여부

    public static Seller from(SignUpForm form) {
        return Seller.builder()
            .email(form.getEmail().toLowerCase(Locale.ROOT))    // 이메일 암호화도 고려해보자
            .password(form.getPassword())
            .name(form.getName())
            .birth(form.getBirth())
            .phone(form.getPhone())
            .verify(false)
            .build();
    }


}
