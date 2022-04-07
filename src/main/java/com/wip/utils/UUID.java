package com.wip.utils;


import java.util.Random;

/**
 * Encapsulating UUID
 */
public abstract class UUID {

    static Random r = new Random();

    /**
     * According to a range, generate a random integer
     * @param min   Minimum (including)
     * @param max   Maximum (including)
     * @return  random number
     */
    public static int random(int min, int max) {
        return r.nextInt(max - min + 1) + min;
    }

    private static final char[] _UU64 = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] _UU32 = "0123456789abcdefghijklmnopqrstuv".toCharArray();

    /**
     * UUID in compact format in 64 Radix
     * @return
     */
    public static String UU64() {
        return UU64(java.util.UUID.randomUUID());
    }


    /**
     * Back a UUID, which is converted into a compact string in 64 base, with the content of [\ \ - 0-9a-zA-Z \
     * <p>
     * For example, a UUID similar to the following:
     *
     * <pre>
     * a6c5c51c-689c-4525-9bcd-c14c1e107c80
     * A total of 128 bits, divided into L64 and R64, divided into two 64 bits (two long)
     * *    > L = uu.getLeastSignificantBits();
     * *    > UUID = uu.getMostSignificantBits();
     * *And a 64 base number is 6 bits, so the order of our values is
     * *1. Take 10 times from L64 bit and take 6 bits each time
     * *2. Take the last 4 bits from L64 + the first 2 bits of R64 and put them together
     * *3. Take 10 times from R64 bit and take 6 bits each time
     * *4. The last two
     * *In this way, a 22 length string can be used to represent a 32 length UUID, compressing 1 / 3
     * </pre>
     *
     * @param uu
     *            *UUID object
     * @return UUID in compact format in 64 Radix
     */
    public static String UU64(java.util.UUID uu) {
        int index = 0;
        char[] cs = new char[22];
        long L = uu.getMostSignificantBits();
        long R = uu.getLeastSignificantBits();
        long mask = 63;
        //Take 10 times from L64 bit and take 6 bits each time
        for (int off = 58; off >= 4; off -= 6) {
            long hex = (L & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // Take the last 4 bits from L64 + the first 2 bits of R64 and put them together
        int l = (int) (((L & 0xF) << 2) | ((R & (3 << 62)) >>> 62));
        cs[index++] = _UU64[l];
        // Take 10 times from R64 bit and take 6 bits each time
        for (int off = 56; off >= 2; off -= 6) {
            long hex = (R & (mask << off)) >>> off;
            cs[index++] = _UU64[(int) hex];
        }
        // The remaining two are the last
        cs[index++] = _UU64[(int) (R & 3)];
        // Back string
        return new String(cs);
    }
    public static String UU32(java.util.UUID uuid) {
        StringBuilder sb = new StringBuilder();
        long m = uuid.getMostSignificantBits();
        long l = uuid.getLeastSignificantBits();
        for (int i = 0; i < 13; i++) {
            sb.append(_UU32[(int) (m >> ((13 - i - 1) * 5)) & 31]);
        }
        for (int i = 0; i < 13; i++) {
            sb.append(_UU32[(int) (m >> ((13 - i - 1) * 5)) & 31]);
        }
        return sb.toString();
    }

    public static String UU32() {
        return UU32(java.util.UUID.randomUUID());
    }


}
