import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Pw42 {

    public static void main(String[] args) {
        long globalStartTime = System.nanoTime();

        // Генеруємо послідовність з 20 дійсних чисел асинхронно
        CompletableFuture<double[]> sequenceFuture = CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            double[] sequence = new double[20];
            Random random = new Random();
            for (int i = 0; i < sequence.length; i++) {
                sequence[i] = random.nextDouble() * 100; // Генеруємо випадкові дійсні числа від 0 до 100
            }
            System.out.println("Generated sequence of 20 numbers:");
            printArray(sequence);
            printExecutionTime(startTime);
            return sequence;
        });

        // Обчислюємо (a2 - a1) * (a3 - a2) * ... * (an - an-1)
        CompletableFuture<Double> resultFuture = sequenceFuture.thenApplyAsync(sequence -> {
            long startTime = System.nanoTime();
            double result = 1.0;
            for (int i = 1; i < sequence.length; i++) {
                double difference = sequence[i] - sequence[i - 1];
                System.out.printf("(%.2f - %.2f) = %.2f\n", sequence[i], sequence[i - 1], difference);
                result *= difference;
            }
            System.out.println("\nComputed result of (a2 - a1) * (a3 - a2) * ... * (an - an-1):");
            System.out.printf("Result: %.5f\n", result);
            printExecutionTime(startTime);
            return result;
        });

        // Виведення фінального результату
        resultFuture.thenAcceptAsync(result -> {
            System.out.println("\nFinal result of the computation:");
            System.out.printf("%.5f\n", result);
        }).thenRunAsync(() -> {
            long globalEndTime = System.nanoTime();
            long duration = TimeUnit.NANOSECONDS.toMillis(globalEndTime - globalStartTime);
            System.out.println("\nAll tasks completed successfully!");
            System.out.println("Total time taken for all asynchronous operations: " + duration + " ms");
        });

        // Чекаємо завершення всіх асинхронних задач перед завершенням програми
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Метод для виведення масиву дійсних чисел
    private static void printArray(double[] array) {
        for (double value : array) {
            System.out.printf("%.2f ", value);
        }
        System.out.println();
    }

    // Метод для виведення часу виконання задачі
    private static void printExecutionTime(long startTime) {
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("Time taken: " + duration + " ms\n");
    }
}
