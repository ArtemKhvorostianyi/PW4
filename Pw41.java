import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Pw41 {

    public static void main(String[] args) {
        // Генеруємо одновимірний масив з 10 чисел асинхронно
        CompletableFuture<int[]> initialArrayFuture = CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            int[] array = new int[10];
            Random random = new Random();
            for (int i = 0; i < array.length; i++) {
                array[i] = random.nextInt(100); // Генеруємо випадкові числа від 0 до 99
            }
            System.out.println("Initial array generated: ");
            printArray(array);
            printExecutionTime(startTime);
            return array;
        });

        // Модифікуємо значення масиву: додаємо +10 до кожного елементу
        CompletableFuture<double[]> modifiedArrayFuture = initialArrayFuture.thenApplyAsync(array -> {
            long startTime = System.nanoTime();
            for (int i = 0; i < array.length; i++) {
                array[i] += 10;
            }
            System.out.println("Array after adding 10 to each element:");
            printArray(array);
            printExecutionTime(startTime);
            return array;
        }).thenApplyAsync(array -> {
            long startTime = System.nanoTime();
            double[] resultArray = new double[array.length];
            for (int i = 0; i < array.length; i++) {
                resultArray[i] = array[i] / 2.0;
            }
            System.out.println("Array after dividing each element by 2:");
            printArray(resultArray);
            printExecutionTime(startTime);
            return resultArray;
        });

        // Виведення фінального масиву з додатковим інформаційним текстом
        modifiedArrayFuture.thenAcceptAsync(finalArray -> {
            System.out.println("\nFinal result after division:");
            printArray(finalArray);
        }).thenRunAsync(() -> {
            System.out.println("\nAll tasks completed successfully!");
        });

        // Необхідно почекати завершення всіх асинхронних задач перед завершенням програми
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Метод для виведення цілочисельного масиву
    private static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    // Метод для виведення дробового масиву
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
