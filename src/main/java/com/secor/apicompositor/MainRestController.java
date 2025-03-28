package com.secor.apicompositor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class MainRestController {

    private static final Logger log = LoggerFactory.getLogger(MainRestController.class);

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Qualifier("plain-old-user-client")
    WebClient userClient;

    @Autowired
    @Qualifier("plain-old-sub-client")
    WebClient subClient;

    @Autowired
    @Qualifier("plain-old-payment-client")
    WebClient paymentClient;


    @GetMapping("get/subs/active")
    public ResponseEntity<?> getActiveSubs()
    {

        // CHECK IN THE CACHE FIRST
        if (redisTemplate.opsForValue().get("subs") != null)
        {
            log.info("Returning subs from cache");
            return ResponseEntity.ok(redisTemplate.opsForValue().get("subs"));
        }

        //COMPOSE FROM SERVICES IF NOT IN CACHE
        List<SubView> subViews = new ArrayList<>();

        List<Subscription> subResponse = subClient.get()
                .uri("/get/subs/active")
                .retrieve()
                .bodyToFlux(Subscription.class).collectList().block();

        subResponse.stream().forEach(subscription ->
        {
            SubView subView = new SubView();

             Payment payment =paymentClient.get()
                    .uri("/get/payment/" + subscription.getSubid())
                    .retrieve()
                    .bodyToMono(Payment.class).block();

             subView.setSubid(subscription.getSubid());
             subView.setPaymentid(payment.getPaymentid());
             subView.setStartdate(subscription.getStartdate());


                subscription.getUsers().getUsers().stream().forEach(user ->
                {
                    UserView userView = userClient.get()
                            .uri("/get/user/" + user)
                            .retrieve()
                            .bodyToMono(UserView.class).block();

                    subView.getUsersViews().add(userView);
                });

                subViews.add(subView);

        });

        //PUT IN TO THE CACHE
        redisTemplate.opsForValue().set("subs", subViews);
        return ResponseEntity.ok(subViews);
    }

}
