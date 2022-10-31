package com.parasuram.spring.async.parasuramspringasync.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import static com.parasuram.spring.async.parasuramspringasync.web.constants.Constants.HELLO_WORLD;

@RestController
public class HelloWorldCompletableFutureControllerTest {



    @GetMapping("/tenant/{tenantId}/testCompletableFuture/")
    public CompletableFuture<String> echoHelloWorldWithTenant(@PathVariable String tenantId){
        return CompletableFuture.supplyAsync(
                ()->{
                    randomDelay();
                    return String.format("%s--%s",HELLO_WORLD,tenantId);
                }
        );
    }

    private static void randomDelay() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
