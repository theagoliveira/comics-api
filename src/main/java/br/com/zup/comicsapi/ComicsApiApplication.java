package br.com.zup.comicsapi;

import java.time.Clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class ComicsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComicsApiApplication.class, args);
    }

    // Mock LocalDate.now() with Clock
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
