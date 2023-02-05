package task1;

public class Funicular {
    public static void goUp() {
        try {
            System.out.println("<- <- <- <- <- <- <-");
            System.out.println("I am going up");
            System.out.println("-> -> -> -> -> -> ->");
            Thread.sleep(2500);
            System.out.println("I have arrived");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
