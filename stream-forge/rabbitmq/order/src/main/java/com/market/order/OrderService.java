package com.market.order;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${message.queue.product}")
    public String queueProduct;

    @Value("${message.queue.payment}")
    public String queuePayment;

    private final RabbitTemplate rabbitTemplate;

    public void createOrder(String orderId){
        rabbitTemplate.convertAndSend(queueProduct, orderId);
        rabbitTemplate.convertAndSend(queuePayment, orderId);
    }
}
