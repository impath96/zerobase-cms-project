package com.zerobase.cms.user.service.customer;


import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.dto.SignUpForm;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpCustomerService {

    private final CustomerRepository customerRepository;

    // 전달받은 회원 정보 DTO를 통해 DB에 회원 정보 저장
    public Customer signUp(SignUpForm form) {
        return customerRepository.save(Customer.from(form));
    }

    // 존재하는 이메일인지 확인
    // 1) 이메일은 unique
    // 2) 1)의 이유로 이미 회원가입이 되어 있는지 체크하는 것과 동일
    public boolean isEmailExist(String email) {

        return customerRepository.existsByEmail(email.toLowerCase(Locale.ROOT));

    }

    @Transactional
    public void changeCustomerValidateEmail(Long customerId, String verificationCode) {

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 이 2개를 Customer 도메인 객체에 파라미터로 받아서 수행하는 메서드를 하나 만드는 것도 좋을 듯
        customer.setVerificationCode(verificationCode);
        customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

    }

    @Transactional
    public void verifyEmail(String email, String verificationCode) {

        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (customer.isVerify()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED);
        }

        if (!customer.getVerificationCode().equals(verificationCode)) {
            throw new CustomException(ErrorCode.WRONG_VERIFICATION);
        }

        if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_CODE);
        }

        customer.setVerify(true);

    }
}
