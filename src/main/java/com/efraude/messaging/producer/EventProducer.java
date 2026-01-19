package com.efraude.messaging.producer;

import com.efraude.messaging.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProducer {

    private static final String USER_REGISTERED_TOPIC = "user.registered";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserRegistered(UserRegisteredEvent event) {
        log.info("Publishing user registered event for user: {}", event.getEmail());
        kafkaTemplate.send(USER_REGISTERED_TOPIC, event.getUserId().toString(), event);
    }
}
