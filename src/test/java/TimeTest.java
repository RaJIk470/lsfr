import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.rajik.lfsr.LFSR;
import xyz.rajik.lfsr.LFSRByteEncoder;
import xyz.rajik.lfsr.MeasureTimeUtils;

import java.time.Instant;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class TimeTest {
    private static LFSRByteEncoder lfsrByteEncoder;

    @BeforeAll
    public static void before() {
        lfsrByteEncoder = new LFSRByteEncoder();
    }

    @Test
    public void firstTime() {
        Random random = new Random();
        int arraySize = 2_000_000_0;
        byte[] bytes = new byte[arraySize];
        int[] ints = random.ints().limit(arraySize).toArray();
        for (int i = 0; i < arraySize; i++) {
            bytes[i] = (byte) ints[i];
        }


        Supplier<Void> supplier = () -> {
            byte[] newMsg = new byte[arraySize];
            for (int i = 0; i < bytes.length; i++) {
                for (int j = 0; j < 8; j++) {
                    int temp = bytes[i] & (1 << j);
                    if (temp > 0) {
                        newMsg[i] |= (128 >> j);
                    }
                }
            }
            lfsrByteEncoder.encode(newMsg);

            newMsg = new byte[arraySize];
            for (int i = 0; i < bytes.length; i++) {
                for (int j = 0; j < 8; j++) {
                    int temp = bytes[i] & (1 << j);
                    if (temp > 0) {
                        newMsg[i] |= (128 >> j);
                    }
                }
            }
            return null;
        };

        System.out.println(MeasureTimeUtils.measureTime(supplier) / 1000.0);
    }

    @Test
    public void secondTime() {
        firstTime();
    }
}
