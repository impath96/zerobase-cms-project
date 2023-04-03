package com.zerobase.cms.user.service.seller;

import com.zerobase.cms.user.domain.model.Seller;
import com.zerobase.cms.user.domain.repository.SellerRepository;
import com.zerobase.cms.user.dto.SignUpForm;
import com.zerobase.cms.user.exception.CustomException;
import com.zerobase.cms.user.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;


    public  Optional<Seller> findByIdAndEmail(Long id, String email) {
        return sellerRepository.findByIdAndEmail(id, email);
    }

    public Optional<Seller> findValidSeller(String email, String password) {
        return sellerRepository.findByEmailAndPasswordAndAndVerifyIsTrue(email, password);
    }

    // 이번에는 SignUpService를 따로 만들지 않고 여기에다가 구현해보자
    public Seller signUp(SignUpForm form) {
        return sellerRepository.save(Seller.from(form));
    }

    // repository에서 find를 한 후 isPresent()를 하는게 나을까?
    // 머가 나을까?
    // 더 나은걸 어떤 기준으로 잡지? 쿼리 속도?
    public boolean isEmailExist(String email) {
        return sellerRepository.existsByEmail(email);
    }

    @Transactional
    public void verifyEmail(String email, String verificationCode) {

        Seller seller = sellerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (seller.isVerify()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED);
        }

        if (!seller.getVerificationCode().equals(verificationCode)) {
            throw new CustomException(ErrorCode.WRONG_VERIFICATION);
        }

        if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_CODE);
        }

        seller.setVerify(true);
    }

    @Transactional
    public void changeSellerValidateEmail(Long customerId, String verificationCode) {

        Seller seller = sellerRepository.findById(customerId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 이 2개를 Customer 도메인 객체에 파라미터로 받아서 수행하는 메서드를 하나 만드는 것도 좋을 듯
        seller.setVerificationCode(verificationCode);
        seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));

    }
}
