package com.secor.apicompositor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer
{
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    //@Qualifier("analyticsDataCounter")
    //@Autowired
    //Counter analytics_counter;

    //@Qualifier("offerCounter")s
    // @Autowired
    //Counter offer_counter;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @KafkaListener(topics = "sub-events", groupId = "bingeit-sub-events-consumer-service")
    public void consumeSubEvents(String message) throws IOException
    {
        //analytics_counter.increment();
        ObjectMapper mapper  = new ObjectMapper();
        SubDatum datum =  mapper.readValue(message, SubDatum.class);

        logger.info(String.format("#### -> Consumed message -> %s", message));

        if(datum.getType().equalsIgnoreCase("update"))
        {
            logger.info("Checking if a new subscription is active");
            if(datum.getStatus().equalsIgnoreCase("active"))
            {
                logger.info("New subscription is active, so updating the cache");
                redisTemplate.opsForValue().getAndDelete("subs");
            }
        }

    }




}

