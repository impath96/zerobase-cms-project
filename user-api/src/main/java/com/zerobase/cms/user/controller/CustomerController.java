package com.zerobase.cms.user.controller;

import com.zerobase.cms.user.dto.ChangeBalanceForm;
import com.zerobase.cms.user.dto.CustomerDto;
import com.zerobase.cms.user.service.customer.CustomerBalanceService;
import com.zerobase.cms.user.service.customer.CustomerService;
import com.zerobase.domain.common.UserVo;
import com.zerobase.domain.configuration.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomerService customerService;
    private final CustomerBalanceService customerBalanceService;

    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {

        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

        return ResponseEntity.ok(customerService.findById(userVo.getId()));
    }

    @PostMapping("/balance")
    public ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN") String token,
        @RequestBody ChangeBalanceForm form) {

        UserVo user = jwtAuthenticationProvider.getUserVo(token);

        return ResponseEntity.ok(
            customerBalanceService.changeBalance(user.getId(), form).getCurrentMoney());
    }
}
