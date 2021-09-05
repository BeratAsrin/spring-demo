package practiceupto6.codes;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("practiceupto6/applicationContext.xml");
        Car car = context.getBean("mustangCar", Car.class);
        System.out.println(car.getEngineSound());
        System.out.println(car.getEngineVolume());
        System.out.println(car.getModel());
        context.close();
    }
}
