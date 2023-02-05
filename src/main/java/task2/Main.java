package task2;

import com.github.javafaker.Faker;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.BlockingQueue;

/**
 * Реализовать логику работы небольшого ресторана, который может принять только 5 человек одновременно.
 * Люди прибывают и выстраиваются в очередь на входе, ожидая освобождения столиков.
 * Все заказывают комплексный обед из 3-х блюд (суп, салат и второе).
 * В ресторане один официант и три повара, каждый из которых готовит только одно блюдо на одного человека.
 * Один повар готовит только супы, второй только салаты, третий только второе.
 * Официант собирает обед на поднос и относит посетителям.
 * Посетили едят и покидают ресторан, освобождая столы для новых посетителей.
 */
public class Main {
    private static BlockingQueue<Customer> allCustomersWhoCame = new ArrayBlockingQueue<>(5, true);
    private static BlockingQueue<Customer> allCustomersNotMakeAnOrder = new ArrayBlockingQueue<>(5, true);
    private static BlockingQueue<Customer> allCustomersWaitingForOrder = new ArrayBlockingQueue<>(5, true);
    private static BlockingQueue<Menu> queueSalad = new ArrayBlockingQueue<>(1, true);
    private static BlockingQueue<Menu> queueSoup = new ArrayBlockingQueue<>(1, true);
    private static BlockingQueue<Menu> queueBasicFood = new ArrayBlockingQueue<>(1, true);

    private static Random random = new Random();


    public static void main(String[] args) {
        Faker faker = new Faker();

        // поток генерирует клиентов и заносит их в очереди. Если более 5 человек в очереди, поток ждет
        new Thread(() -> {
            while (true) {
                try {
                    Customer customer = new Customer(faker.name().firstName());
                    allCustomersWhoCame.put(customer);
                    allCustomersNotMakeAnOrder.put(customer);
                    customer.come();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        // поток достает клиентов из очереди, каждый клиент делает заказ
        new Thread(() -> {
            while (true) {
                try {
                    Customer customer = allCustomersNotMakeAnOrder.take();
                    customer.maceAnOrder(queueSalad, queueSoup, queueBasicFood);
                    allCustomersWaitingForOrder.put(customer);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        // потоки-повара, каждый готовит свой заказ, когда заказ готов сигнализируют CyclicBarrier waiter
        new Thread(() -> takeAnOrder(queueSalad)).start();
        new Thread(() -> takeAnOrder(queueSoup)).start();
        new Thread(() -> takeAnOrder(queueBasicFood)).start();
    }

    // когда 3 потока-повара выполнили свои заказы, то официант выносит блюдо, а клиенты едят и уходят
    private static CyclicBarrier waiter = new CyclicBarrier(3,
            () -> {
                System.err.println("The waiter took out the order!");
                try {
                    allCustomersWaitingForOrder.take().eatFoodAndGo();
                    allCustomersWhoCame.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });


    // метод выполняет приготовление пищи каждым из поваров
    public static void takeAnOrder(BlockingQueue<Menu> queue) {
        while (true) {
            try {
                System.out.println(Thread.currentThread().getName() + " preparing an order " + queue.take());
                Thread.sleep(1000 + random.nextInt(3500));
                waiter.await();
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
