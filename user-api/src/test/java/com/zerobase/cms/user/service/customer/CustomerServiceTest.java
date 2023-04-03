package com.zerobase.cms.user.service.customer;

import static com.zerobase.cms.user.exception.ErrorCode.CHECK_VERIFY;
import static com.zerobase.cms.user.exception.ErrorCode.LOGIN_CHECK_FAIL;
import static com.zerobase.cms.user.exception.ErrorCode.NOT_FOUND_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.dto.CustomerDto;
import com.zerobase.cms.user.exception.CustomException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @Test
    @DisplayName("회원 찾기 - 성공")
    void findById_회원찾기_성공() {
        // given

        Customer customer = Customer.builder()
            .id(1L)
            .build();

        given(customerRepository.findById(anyLong()))
            .willReturn(Optional.of(customer));

        // when
        CustomerDto customerDto = customerService.findById(customer.getId());
        // then
        assertEquals(customer.getId(), customerDto.getId());

    }

    @Test
    @DisplayName("해당 회원 없음 - 회원 찾기 실패")
    void findById_회원찾기_실패() {
        // given

        given(customerRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        // when
        CustomException customException = assertThrows(CustomException.class,
            () -> customerService.findById(anyLong()));

        // then
        assertEquals(NOT_FOUND_USER, customException.getErrorCode());

    }

    @Test
    @DisplayName("")
    void findValidCustomer_성공() {
        // given
        Customer customer = Customer.builder()
            .id(1L)
            .email("test@naver.com")
            .password("password")
            .verify(true)
            .build();

        given(customerRepository.findByEmailAndPassword(anyString(), anyString()))
            .willReturn(Optional.of(customer));

        // when
        Customer validCustomer = customerService.findValidCustomer(customer.getEmail(),
            customer.getPassword());

        // then
        assertEquals(customer.getId(), validCustomer.getId());
        assertEquals(customer.getEmail(), validCustomer.getEmail());
        assertEquals(customer.getPassword(), validCustomer.getPassword());
        assertTrue(validCustomer.isVerify());

    }

    // 이렇게 하면 내부 구현을 무조건 알아야 하는건가?
    @Test
    @DisplayName("")
    void findValidCustomer_잘못된_회원정보() {

        // repository에서 이메일과 비밀번호 조건으로 찾아오기 때문에 empty()로 가정
        given(customerRepository.findByEmailAndPassword(anyString(), anyString()))
            .willReturn(Optional.empty());

        // when
        CustomException customException = assertThrows(CustomException.class,
            () -> customerService.findValidCustomer(anyString(), anyString()));

        assertEquals(LOGIN_CHECK_FAIL, customException.getErrorCode());

    }

    @Test
    @DisplayName("")
    void findValidCustomer_회원_VERIFY_FALSE() {

        Customer customer = Customer.builder()
            .id(1L)
            .email("test@naver.com")
            .password("password")
            .verify(false)
            .build();

        // repository에서 이메일과 비밀번호 조건으로 찾아오기 때문에 empty()로 가정
        given(customerRepository.findByEmailAndPassword(anyString(), anyString()))
            .willReturn(Optional.of(customer));

        // when
        CustomException customException = assertThrows(CustomException.class,
            () -> customerService.findValidCustomer(anyString(), anyString()));

        assertEquals(CHECK_VERIFY, customException.getErrorCode());

    }


}