package com.rent_management_system.payment;

import com.rent_management_system.components.UserDataDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentDTOMapper {

    public static PaymentDTO toDTO(Payment payment){
        return new PaymentDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCurrency(),
                payment.getChannel(),
                payment.getTransactionDate(),
                payment.getReference(),
                new UserDataDTO(
                        payment.getUser().getId(),
                        payment.getUser().getFirstName(),
                        payment.getUser().getLastName(),
                        payment.getUser().getEmail(),
                        payment.getUser().getRole(),
                        payment.getUser().getPhone()
                )
//                new RentInfoDataDTO(
//                        payment.getRentInfo().getId(),
//                        payment.getRentInfo().getStatus(),
//                        payment.getRentInfo().getPaymentMethod(),
//                        payment.getRentInfo().getUpdatedAt(),
//                        payment.getRentInfo().getUpdatedAt()
//                )
        );
    }

    public List<PaymentDTO> paymentDTOList(List<Payment>payments){
        return payments.stream()
                .map(PaymentDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

}
