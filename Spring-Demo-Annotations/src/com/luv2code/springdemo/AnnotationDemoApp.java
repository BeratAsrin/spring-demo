package com.luv2code.springdemo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AnnotationDemoApp {
    public static void main(String[] args) {

        // Read Spring Config File
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Get the Bean From Spring Container
        Coach theCoach = context.getBean("tennisCoach",Coach.class);

        // Call a methods on the bean
        System.out.println(theCoach.getDailyWorkout());
        System.out.println(theCoach.getFortune());

        // Close the Context
        context.close();
    }
}
