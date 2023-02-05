package task1;


import java.util.Random;

public class Person {
    String name;
    int age;
    Random random = new Random();

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void run() {
        try {
            Thread.sleep(1500 + random.nextInt(2500));
            System.out.println(name + " bought a ticket");
            Thread.sleep(1500 + random.nextInt(4000));
            System.out.println(name + " got in line");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

