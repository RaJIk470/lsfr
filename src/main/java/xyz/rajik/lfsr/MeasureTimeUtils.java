package xyz.rajik.lfsr;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class MeasureTimeUtils {
    public static Long measureTime(Supplier<?> supplier) {
        long start = System.currentTimeMillis();

        supplier.get();

        long end = System.currentTimeMillis();
        long result = end - start;

        return result;
    }
}
