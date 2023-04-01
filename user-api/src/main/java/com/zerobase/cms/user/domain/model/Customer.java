package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.dto.SignUpForm;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 고객 PK

    @Column(unique = true)
    private String email;       // 이메일 정보
    private String name;        // 고객 이름
    private String password;    // 비밀번호
    private String phone;       // 휴대폰 번호
    private LocalDate birth;    // 생일

    private LocalDateTime verifyExpiredAt;  // 이메일 인증 유효 기간
    private String verificationCode;        // 이메일 인증 코드
    private boolean verify;                 // 이메일 유효성 여부

    public static Customer from(SignUpForm form) {
        return Customer.builder()
            .email(form.getEmail().toLowerCase(Locale.ROOT))    // 이메일 암호화도 고려해보자
            .password(form.getPassword())
            .name(form.getName())
            .birth(form.getBirth())
            .phone(form.getPhone())
            .verify(false)
            .build();
    }

}
