import java.util.*;
import java.util.concurrent.*;

public class Main {
    static int maxSizeOfAllRows;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Future> futures = new ArrayList<>();
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }


        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            Callable logic = () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return maxSize;
            };
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future task = executor.submit(logic);
            futures.add(task);
            executor.shutdown();
        }


        for (Future task : futures) {
            int size = (int) task.get();
            if (size > maxSizeOfAllRows) {
                maxSizeOfAllRows = size;
            }
        }

        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Maximum Size Of All Rows - " + maxSizeOfAllRows + ".");
        System.out.println("Time: " + (endTs - startTs) + "ms");
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
