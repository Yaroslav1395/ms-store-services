package sakhno.springframework.ms_store_notification_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class MsStoreNotificationService {


    public static void main( String[] args ) {
        SpringApplication.run(MsStoreNotificationService.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleZZZZZNotification(OrderPlacedEvent orderPlacedEvent){
        log.info("Received Notification forOrder - {}", orderPlacedEvent.getOrderNumber());

    }

}
