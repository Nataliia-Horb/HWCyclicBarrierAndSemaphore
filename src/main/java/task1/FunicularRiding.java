package task1;

import com.github.javafaker.Faker;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 * Создать модель поведения с применением CyclicBarrier:
 * Имеется фуникулер, который поднимает людей на вершину холма. Он может перевести
 * ограниченное количество людей, например 5. Люди приходят на платформу, ждут, и когда наберется нужное количество (5),
 * фуникулер перевозит их. В конечном итоге каждый человек должен будет подняться на вершину холма с помощью фуникулера.
 */
public class FunicularRiding {
    private static final CyclicBarrier BARRIER = new CyclicBarrier(2, Funicular::goUp);

    public static void main(String[] args) {
        Faker faker = new Faker();
        Runnable mainTask = () -> {
            try {
                new Person(faker.name().firstName(), faker.random().nextInt(5, 85)).run();
                BARRIER.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        };

        new Thread(mainTask).start();
        new Thread(mainTask).start();
        new Thread(mainTask).start();
        new Thread(mainTask).start();
        new Thread(mainTask).start();
        new Thread(mainTask).start();
        new Thread(mainTask).start();
    }
}