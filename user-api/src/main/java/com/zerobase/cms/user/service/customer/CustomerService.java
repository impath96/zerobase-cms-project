package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.dto.CustomerDto;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDto findById(Long id) {
        return CustomerDto.from(customerRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)));
    }

    // 이메일, 비밀번호를 일치하도록 넣어 놨는데 verify가 false 라는 건 아직 이메일 인증이 완료되지 않았다는
    // 의미니까 따로 처리하는건 별론가?
    public Customer findValidCustomer(String email, String password) {

        // 1. 먼저 이메일과 비밀번호 제대로 입력했는지 체크
        Customer customer = customerRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_CHECK_FAIL));

        // 2. verify가 false일 경우 이메일 인증을 완료해주세요 메시지 출력
        if (!customer.isVerify()) {
            throw new CustomException(ErrorCode.CHECK_VERIFY);
        }

        return customer;
    }

    public boolean hasCustomer(Long id, String email) {
        return customerRepository.existsByIdAndEmail(id, email);
    }

}
