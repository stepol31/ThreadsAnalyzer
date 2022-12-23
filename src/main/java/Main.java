import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);
    public static int AMOUNT_OF_TEXTS = 10_000;
    public static int TEXT_LENGTH = 100_000;
    public static String LETTERS = "abc";

    public static void main(String[] args) throws InterruptedException {
        Thread textGenerate = new Thread(() -> {
            for (int i = 0; i < AMOUNT_OF_TEXTS; i++) {
                String text = generateText(LETTERS, TEXT_LENGTH);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        textGenerate.start();

        Thread a = new Thread(() -> {
            char letter = 'a';
            int maxA;
            maxA = maxChar(queueA, letter);
            System.out.println("Максимальное количество буквы " + letter + " во всех строках: " + maxA);
        });
        a.start();

        Thread b = new Thread(() -> {
            char letter = 'b';
            int maxB;
            maxB = maxChar(queueB, letter);
            System.out.println("Максимальное количество буквы " + letter + " во всех строках: " + maxB);
        });
        b.start();

        Thread c = new Thread(() -> {
            char letter = 'c';
            int maxC;
            maxC = maxChar(queueC, letter);
            System.out.println("Максимальное количество буквы " + letter + " во всех строках: " + maxC);
        });
        c.start();

        a.join();
        b.join();
        c.join();


    }

    public static int maxChar(BlockingQueue<String> queue, char letter) {
        int count = 0;
        int max = 0;
        String text;
        try {
            for (int i = 0; i < AMOUNT_OF_TEXTS; i++) {
                text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == letter) {
                        count++;
                    }
                }

                if (count > max) {
                    max = count;
                }
                count = 0;
            }
        } catch (InterruptedException e) {
            return -1;
        }
        return max;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}


