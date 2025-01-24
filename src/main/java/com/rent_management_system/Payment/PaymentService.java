package com.rent_management_system.Payment;

import com.rent_management_system.Exception.NotFoundException;
import com.rent_management_system.RentInfo.RentInfo;
import com.rent_management_system.RentInfo.RentInfoRepository;
import com.rent_management_system.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentInfoRepository rentInfoRepository;
    private final PaymentDTOMapper paymentDTOMapper;
    @Value("${paystack.secret-key}")
    private String secretKey;

    @Value("${paystack.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RentInfoRepository rentInfoRepository, PaymentDTOMapper paymentDTOMapper) {
        this.paymentRepository = paymentRepository;
        this.rentInfoRepository = rentInfoRepository;
        this.paymentDTOMapper = paymentDTOMapper;
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for initializing payment process
     * @param: paymentRequest object
     * @date 21-01-2025
     * @return PaymentResponse
     */
    public PaymentResponse initializeTransaction(PaymentRequest request, Long rentInfoId) {
        String url = baseUrl + "/transaction/initialize";

        Optional<RentInfo> rentInfoOptional = rentInfoRepository.findById(rentInfoId);
        if (rentInfoOptional.isEmpty()){
            throw new NotFoundException("No rent info associated with the provided Id");
        }
        RentInfo rentInfo = rentInfoOptional.get();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("rentInfoId", rentInfo.getId());
        request.setMetadata(metadata);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        log.info("ENTITY:{}", entity);
        ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(url, entity, PaymentResponse.class);

        return response.getBody();
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method for verifying transaction status through payStack verify endpoint
     * @param: reference
     * @date 21-01-2025
     * @return Object
     */
    public Object verifyTransaction(String reference) {
        String url = baseUrl + "/transaction/verify/" + reference;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        return response.getBody();
    }

    /**
     * @auther Emmanuel Yidana
     * @description: a method to fetch all payment list
     * @date 22-01-2025
     * @return paymentDTOList Object
     */
    public List<PaymentDTO> getPaymentList(){
        List<Payment> payments = paymentRepository.findAll();
        return paymentDTOMapper.paymentDTOList(payments);
    }


    /**
     * @auther Emmanuel Yidana
     * @description: a method for verifying transaction status through payStack webHook
     * @param: data of object type to be provided by payStack
     * @date 21-01-2025
     */
    public void processSuccessfulPayment(Map<String, Object> data) {
        String reference = (String) data.get("reference");
        Integer amount = (Integer) data.get("amount");
        String currency = (String) data.get("currency");
        String status = (String) data.get("status");
        String paidAt = (String) data.get("paid_at");
        String channel = (String) data.get("channel");
        Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
        Long rentInfoId = (Long) metadata.get("rentInfoId");

        Map<String, Object> customer = (Map<String, Object>) data.get("customer");
        String email = (String) customer.get("email");

        log.info("EMAIL:{}", email);
        log.info("AMOUNT:{}", amount);
        // load rent info by rentInfoId
        Optional<RentInfo> rentInfoOptional = rentInfoRepository.findById(rentInfoId);
        if (rentInfoOptional.isEmpty()){
            throw new NotFoundException("No rentInfo associated with the provided user");
        }

        User user = rentInfoOptional.get().getUser();
        RentInfo rentInfo = rentInfoOptional.get();

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setChannel(channel);
        payment.setCurrency(currency);
        payment.setStatus("PAID");
        payment.setReference(reference);
        payment.setTransactionDate(paidAt);
        payment.setUser(user);
        payment.setRentInfo(rentInfo);
        user.setPayment(List.of(payment));
        rentInfo.setPayment(payment);

        paymentRepository.save(payment);

    }

}