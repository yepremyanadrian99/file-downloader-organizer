package am.adrianyepremyan.filedownloaderorganizer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressBarUtil {

    public static void print(int total, int current) {
        double percent = ((double) current / total * 100);
        printBorder();
        System.out.print('|');
        int progress = 0;
        for (int i = 0; i < percent; ++i) {
            if (i % 4 == 0) {
                System.out.print('#');
                ++progress;
            }
        }
        for (int i = 0; i < 25 - progress; ++i) {
            System.out.print('.');
        }
        System.out.print('|');
        System.out.printf("%05.2f%%\n", percent);
        printBorder();
    }

    private static void printBorder() {
        System.out.println("|=========================|");
    }
}
