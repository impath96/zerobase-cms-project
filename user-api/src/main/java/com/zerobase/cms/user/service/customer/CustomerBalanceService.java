package com.zerobase.cms.user.service.customer;

import com.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import com.zerobase.cms.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.cms.user.domain.repository.CustomerRepository;
import com.zerobase.cms.user.dto.ChangeBalanceForm;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
    private final CustomerRepository customerRepository;

    // 해당 Exception 발생했을 때 rollback 처리하겠다.
    @Transactional(rollbackFor = {CustomException.class})
    public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form)
        throws CustomException {
        CustomerBalanceHistory balanceHistory =
            customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(
                    customerId)
                .orElse(CustomerBalanceHistory.builder()
                    .changeMoney(0)
                    .currentMoney(0)
                    .customer(customerRepository.findById(customerId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)))
                    .build());

        if (balanceHistory.getChangeMoney() + form.getMoney() < 0) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_BALANCE);
        }

        balanceHistory = CustomerBalanceHistory.builder()
            .changeMoney(form.getMoney())
            .currentMoney(balanceHistory.getCurrentMoney() + form.getMoney())
            .description(form.getMessage())
            .fromMessage(form.getFrom())
            .customer(balanceHistory.getCustomer())
            .build();

        balanceHistory.getCustomer().setBalance(balanceHistory.getCurrentMoney());
        return customerBalanceHistoryRepository.save(balanceHistory);
    }
}
