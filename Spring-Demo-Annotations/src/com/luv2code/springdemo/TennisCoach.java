package com.luv2code.springdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component //("thatSillyCoach") Default bean id is tennisCoach if it is not changed by hand
@Scope("singleton")
public class TennisCoach implements Coach{

    @Autowired
    @Qualifier("randomFortuneService")
    FortuneService fortuneService;

    /*
    @Autowired
    public TennisCoach(FortuneService fortuneService){
        this.fortuneService = fortuneService;
    }
    */

    public TennisCoach(){
        System.out.println("TennisCoach Constructor.");
    }

    // Define init method
    @PostConstruct
    private void testPostConstruct(){
        System.out.println("PostConstruct is working fine.");
    }

    //Define destroy method
    @PreDestroy
    private void testPreDestroy(){
        System.out.println("PreDestroy is working fine.");
    }

    @Override
    public String getDailyWorkout() {
        return "Practice your backhand volley";
    }

    @Override
    public String getFortune() {
        return fortuneService.getFortune();
    }
    /*
    @Autowired
    public void setFortune(FortuneService fortuneService) {
        System.out.println("TennisCoach setFortune method.");
        this.fortuneService = fortuneService;
    }

    */
}
