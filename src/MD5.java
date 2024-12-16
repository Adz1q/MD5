public class MD5 {
    private static final int[] S = {
            7, 12, 17, 22,
            5, 9, 14, 20,
            4, 11, 16, 23,
            6, 10, 15, 21
    };

    private static final int[] K = new int[64];

    static {
        for (int i = 0; i < 64; i++) {
            K[i] = (int) ((long) (Math.abs(Math.sin(i + 1)) * (1L << 32)) & 0xFFFFFFFFL);
        }
    }

    public static String hash(String input) {
        byte[] paddedData = padInput(input.getBytes());

        int A = 0x67452301;
        int B = 0xEFCDAB89;
        int C = 0x98BADCFE;
        int D = 0x10325476;

        for (int i = 0; i < paddedData.length / 64; i++) {
            int[] M = toIntArray(paddedData, i * 64);

            int AA = A;
            int BB = B;
            int CC = C;
            int DD = D;

            for (int j = 0; j < 64; j++) {
                int F;
                int g;
                if (j < 16) {
                    F = (B & C) | (~B & D);
                    g = j;
                } else if (j < 32) {
                    F = (D & B) | (~D & C);
                    g = (5 * j + 1) % 16;
                } else if (j < 48) {
                    F = B ^ C ^ D;
                    g = (3 * j + 5) % 16;
                } else {
                    F = C ^ (B | ~D);
                    g = (7 * j) % 16;
                }

                F = F + A + K[j] + M[g];
                A = D;
                D = C;
                C = B;
                B = B + Integer.rotateLeft(F, S[(j / 16) * 4 + (j % 4)]);
            }

            A += AA;
            B += BB;
            C += CC;
            D += DD;
        }

        return toHexString(A) + toHexString(B) + toHexString(C) + toHexString(D);
    }

    private static byte[] padInput(byte[] input) {
        int originalLength = input.length;
        int paddingLength = (56 - (originalLength + 1) % 64 + 64) % 64;
        byte[] padded = new byte[originalLength + paddingLength + 8];

        System.arraycopy(input, 0, padded, 0, originalLength);

        padded[originalLength] = (byte) 0x80;

        long bitLength = (long) originalLength * 8;
        for (int i = 0; i < 8; i++) {
            padded[padded.length - 8 + i] = (byte) (bitLength >>> (8 * i));
        }

        return padded;
    }

    private static int[] toIntArray(byte[] data, int offset) {
        int[] result = new int[16];
        for (int i = 0; i < 16; i++) {
            result[i] = (data[offset + i * 4] & 0xFF) |
                    ((data[offset + i * 4 + 1] & 0xFF) << 8) |
                    ((data[offset + i * 4 + 2] & 0xFF) << 16) |
                    ((data[offset + i * 4 + 3] & 0xFF) << 24);
        }
        return result;
    }

    private static String toHexString(int value) {
        StringBuilder hex = new StringBuilder(8);
        for (int i = 0; i < 4; i++) {
            String part = Integer.toHexString((value >> (i * 8)) & 0xFF);
            if (part.length() == 1) hex.append('0');
            hex.append(part);
        }
        return hex.toString();
    }
}