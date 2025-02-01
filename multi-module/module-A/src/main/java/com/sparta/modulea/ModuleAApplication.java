package com.sparta.modulea;

import com.sparta.modulecommon.dto.KafkaDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ModuleAApplication {

    public static void main(String[] args) {
        new KafkaDto();
        SpringApplication.run(ModuleAApplication.class, args);
    }
}
