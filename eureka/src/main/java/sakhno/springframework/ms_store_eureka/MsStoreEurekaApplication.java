package sakhno.springframework.ms_store_eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Hello world!
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class MsStoreEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsStoreEurekaApplication.class, args);
    }
}
