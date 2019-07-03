package cn.cnnic.report.utils;

public abstract class PunycodeUtil {

    private final static int TMIN = 1;
    private final static int TMAX = 26;
    private final static int BASE = 36;
    private final static int INITIAL_N = 128;
    private final static int INITIAL_BIAS = 72;
    private final static int DAMP = 700;
    private final static int SKEW = 38;
    private final static char DELIMITER = '-';
    private final static String PUNY_PREFIX = "xn--";
    private final static char DOT = '.';
    private final static String SPLIT_DOT = "\\.";

    public static String evaluate(String txt) {
        String strResult = txt.toString().trim();
        try {
            strResult = fromPunycodeToChinese(txt.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }

    /**
     * 
     * @param input
     * @return
     * @throws Exception
     */
    public static String fromPunycodeToChineseUnit(String input) throws Exception {
        int n = INITIAL_N;
        int i = 0;
        int bias = INITIAL_BIAS;
        StringBuilder output = new StringBuilder();
        int d = input.lastIndexOf(DELIMITER);
        if (d > 0) {
            for (int j = 0; j < d; j++) {
                char c = input.charAt(j);
                if (!isBasic(c)) {
                    throw new Exception("BAD_INPUT");
                }
                output.append(c);
            }
            d++;
        } else {
            d = 0;
        }
        while (d < input.length()) {
            int oldi = i;
            int w = 1;
            for (int k = BASE;; k += BASE) {
                if (d == input.length()) {
                    throw new Exception("BAD_INPUT");
                }
                int c = input.charAt(d++);
                int digit = codepoint2digit(c);
                if (digit > (Integer.MAX_VALUE - i) / w) {
                    throw new Exception("OVERFLOW");
                }
                i = i + digit * w;
                int t;
                if (k <= bias) {
                    t = TMIN;
                } else if (k >= bias + TMAX) {
                    t = TMAX;
                } else {
                    t = k - bias;
                }
                if (digit < t) {
                    break;
                }
                w = w * (BASE - t);
            }
            bias = adapt(i - oldi, output.length() + 1, oldi == 0);
            if (i / (output.length() + 1) > Integer.MAX_VALUE - n) {
                throw new Exception("OVERFLOW");
            }
            n = n + i / (output.length() + 1);
            i = i % (output.length() + 1);
            output.insert(i, (char) n);
            i++;
        }
        return output.toString();
    }

    /**
     * 
     * @param delta
     * @param numpoints
     * @param first
     * @return
     */
    public static int adapt(int delta, int numpoints, boolean first) {
        if (first) {
            delta = delta / DAMP;
        } else {
            delta = delta / 2;
        }
        delta = delta + (delta / numpoints);
        int k = 0;
        while (delta > ((BASE - TMIN) * TMAX) / 2) {
            delta = delta / (BASE - TMIN);
            k = k + BASE;
        }
        return k + ((BASE - TMIN + 1) * delta) / (delta + SKEW);
    }

    /**
     * 
     * @param c
     * @return
     */
    public static boolean isBasic(char c) {
        return c < 0x80;
    }

    /**
     * 
     * @param d
     * @return
     * @throws Exception
     */
    public static int digit2codepoint(int d) throws Exception {
        if (d < 26) {
            // 0..25 : 'a'..'z'
            return d + 'a';
        } else if (d < 36) {
            // 26..35 : '0'..'9';
            return d - 26 + '0';
        } else {
            throw new Exception("BAD_INPUT");
        }
    }

    /**
     * 
     * @param c
     * @return
     * @throws Exception
     */
    public static int codepoint2digit(int c) throws Exception {
        if (c - '0' < 10) {
            // '0'..'9' : 26..35
            return c - '0' + 26;
        } else if (c - 'a' < 26) {
            // 'a'..'z' : 0..25
            return c - 'a';
        } else {
            throw new Exception("BAD_INPUT");
        }
    }

    /**
     * 
     * @param input
     * @return
     * @throws Exception
     */
    public static String fromPunycodeToChinese(String input) throws Exception {
        if (input == null || input.equalsIgnoreCase("")) {
            return "";
        } else if (input.indexOf(DOT) < 0) {
            if (input.startsWith(PUNY_PREFIX)) {
                return fromPunycodeToChineseUnit(input.substring(PUNY_PREFIX.length()));
            } else {
                return input;
            }

        } else if (input.indexOf(DOT) > 0) {
            String[] arr = input.split(SPLIT_DOT);
            String result = "";
            for (int index = 0; index < arr.length; index++) {
                if (arr[index].startsWith(PUNY_PREFIX)) {
                    result += fromPunycodeToChineseUnit(arr[index].substring(PUNY_PREFIX.length())) + ".";
                } else {
                    result += arr[index] + ".";
                }
            }
            return result.substring(0, result.length() - 1);
        }
        return input;
    }
}
