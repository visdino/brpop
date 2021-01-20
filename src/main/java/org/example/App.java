package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(App.class);
        Thread.sleep(1000 * 1000);
    }

    @Override
    public void run(String... args) throws Exception {
        // run brpop in 3 threads
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(() -> {
                while (true) {
                    String key = "key" + finalI;
                    System.out.println(LocalDateTime.now() + " : start brpop key : " + key);
                    // timeout 5s
                    String value = stringRedisTemplate.opsForList().rightPop(key, 5, TimeUnit.SECONDS);
                    // only one thread get return per timeout
                    System.out.println(LocalDateTime.now() + " : brpop timeout, key : " + key + " ,value : " + value);
                }
            }).start();
        }
    }
}
