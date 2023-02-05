package task2;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.SynchronousQueue;

public class Customer {
    String name;

    public Customer(String name) {
        this.name = name;
    }
    public void come(){
        System.out.println(name+" came to the restaurant");
    }
    public void maceAnOrder(BlockingQueue<Menu> queue1, BlockingQueue<Menu> queue2, BlockingQueue<Menu> queue3) {
        try {
            queue1.put(Menu.SOUP);
            queue2.put(Menu.SALAD);
            queue3.put(Menu.BASIC_FOOD);
            System.out.println("The customer " + name + " made an order");
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void eatFoodAndGo(){
        System.out.println(name+" is eating naw");
        System.out.println((name+ " go home"));
    }
}
