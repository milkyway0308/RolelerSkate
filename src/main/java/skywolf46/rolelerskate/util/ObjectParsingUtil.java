package skywolf46.rolelerskate.util;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class ObjectParsingUtil {

    public static SimplePair<Boolean, Double> isDouble(String x) {
        try {
            double dx = Double.parseDouble(x);
            return new SimplePair<>(true, dx);
        } catch (Exception ignored) {

        }
        return new SimplePair<>(false, 0d);
    }

    public static SimplePair<Boolean, Integer> isInteger(String x) {
        try {
            int dx = Integer.parseInt(x);
            return new SimplePair<>(true, dx);
        } catch (Exception ignored) {

        }
        return new SimplePair<>(false, 0);
    }

    public static SimplePair<Boolean, Boolean> isBoolean(String x) {
        if (x.equals("true") || x.equals("false"))
            return new SimplePair<>(true, Boolean.parseBoolean(x));
        return new SimplePair<>(false, false);
    }

    public static Object parse(String x) {
        SimplePair<Boolean, Boolean> bxx = isBoolean(x);
        if (bxx.getK())
            return bxx.getV();
        SimplePair<Boolean, Integer> bxx_ = isInteger(x);
        if (bxx_.getK())
            return bxx_.getV();
        SimplePair<Boolean, Double> bxx__ = isDouble(x);
        if (bxx__.getK())
            return bxx__.getV();
        return x;
    }

    public static Object[] parseArray(String x, BiConsumer<Integer, String> sideEffects) {
        if (x.length() == 0)
            return new Object[0];
        String[] xy = processSplit(x);
        Object[] arr = new Object[xy.length];
        for (int i = 0; i < arr.length; i++) {
            Object ox = parse(xy[i]);
//            if (ox == null) {
//                sideEffects.accept(i, xy[i]);
//                return null;
//            }
            arr[i] = ox;
        }

        return arr;
    }

    public static String[] processSplit(String x) {
        String[] xi = x.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        ;
        for (int i = 0; i < xi.length; i++) {
            xi[i] = xi[i].trim();
            if (xi[i].startsWith("\"") && xi[i].endsWith("\""))
                xi[i] = xi[i].substring(1, xi[i].length() - 1);
        }
        return xi;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(
                processSplit("Hello, \"World, Again, Now!\" , Yeah")
        ));
    }


}
