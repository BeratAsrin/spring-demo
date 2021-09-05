package com.luv2code.springdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FortuneFromFile implements FortuneService{

    @Value("${fortune}")
    private String fortune;

    @Override
    public String getFortune() {
        return fortune;
    }
}
