package com.luv2code.springdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties")
//@ComponentScan("com.luv2code.springdemo")"
public class SportConfig {

    // Define bean for our saf fortune service
    @Bean
    public FortuneService sadFortuneService(){
        return new SadFortuneService();
    }

    @Bean
    public FortuneService fortuneFromFile(){
        return new FortuneFromFile();
    }

    // Define bean for our swim coach and inject dependency
    @Bean
    public Coach swimCoach(){
        return  new SwimCoach(fortuneFromFile());
        //return new SwimCoach(sadFortuneService());
    }

}
