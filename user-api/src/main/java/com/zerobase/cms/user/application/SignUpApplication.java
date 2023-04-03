package com.zerobase.cms.user.application;

import com.zerobase.cms.user.domain.model.Customer;
import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.dto.SendMailForm;
import com.zerobase.cms.user.dto.SignUpForm;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import com.zerobase.cms.user.service.EmailSendService;
import com.zerobase.cms.user.service.customer.SignUpCustomerService;
import com.zerobase.cms.user.service.seller.SellerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/mailgun.properties")
public class SignUpApplication {

    @Value("${mailgun.domain}")
    private String domain;

    private final EmailSendService emailSendService;
    private final SignUpCustomerService signUpCustomerService;
    private final SellerService sellerService;

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }

    public void sellerVerify(String email, String code) {
        sellerService.verifyEmail(email, code);
    }

    public String customerSignUp(SignUpForm form) {

        // 이미 존재하는 이메일인지 체크 - 중복 가입 검사
        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }

        Customer customer = signUpCustomerService.signUp(form);

        // 이메일 인증 코드 생성
        String code = generateRandomCode();
        System.out.println("code : " + code);
        // 여기서 이메일 Form을 만드는거 맞는건가?
        SendMailForm sendMailForm = SendMailForm.builder()
            .from("test-account@" + domain)
            .to(form.getEmail())
            .subject("Verification Email")
            .text(getVerificationEmailBody(form.getEmail(), form.getName(), "customer", code))
            .build();

        emailSendService.sendEmail(sendMailForm);
        signUpCustomerService.changeCustomerValidateEmail(customer.getId(), code);
        return "회원 가입에 성공하였습니다.";

    }

    public String sellerSignUp(SignUpForm form) {

        // 이미 존재하는 이메일인지 체크 - 중복 가입 검사
        if (sellerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }

        Seller seller = sellerService.signUp(form);

        // 이메일 인증 코드 생성
        String code = generateRandomCode();

        // 여기서 이메일 Form을 만드는거 맞는건가?
        SendMailForm sendMailForm = SendMailForm.builder()
            .from("test-account@" + domain)
            .to(form.getEmail())
            .subject("Verification Email")
            .text(getVerificationEmailBody(form.getEmail(), form.getName(), "seller", code))
            .build();

        emailSendService.sendEmail(sendMailForm);
        sellerService.changeSellerValidateEmail(seller.getId(), code);
        return "회원 가입에 성공하였습니다.";

    }

    private String generateRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    // 이메일 발송 시 사용할 템플릿 메시지
    private String getVerificationEmailBody(String email, String name, String type, String code) {
        StringBuilder builder = new StringBuilder();

        return builder.append("Hello ").append(name)
            .append("! Please click the link for verification.\n\n")
            .append("http://localhost:8081/signup/" + type + "/verify?email=")
            .append(email)
            .append("&code=")
            .append(code).toString();
    }
}
