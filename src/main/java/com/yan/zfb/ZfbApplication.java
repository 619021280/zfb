package com.yan.zfb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ZfbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZfbApplication.class, args);
    }

}
