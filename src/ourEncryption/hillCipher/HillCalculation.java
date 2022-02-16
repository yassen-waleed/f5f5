package ourEncryption.hillCipher;

import java.util.List;

public class HillCalculation {
    private static final int N = 3;
    private static String key = "MoathAl-Jardat";


    private static int getInverseOfDeterminant(int determinant) {
        if (determinant < 0) {
            determinant = getReminder(determinant);
        }
        for (int i = 1; i < 256; i++)
            if (((determinant % 256) * (i % 256)) % 256 == 1)
                return i;
        return 1;
    }

    private static int getReminder(int number) {
        int r;
        if (number >= 0) {
            return (number % 256);
        } else {
            int x = -1 * number;
            if (x == 256) {
                return 0;
            } else if (x < 256) {
                return (256 - x);

            } else {
                double g = x / 256.0;
                int y = (int) Math.ceil(g);
                int a = y * 256;
                return a - x;
            }
        }
    }

    private static void getCofactor(int A[][], int temp[][], int p, int q, int n) {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = A[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    private static int determinant(int A[][], int n) {
        int D = 0;
        if (n == 1)
            return A[0][0];
        int[][] temp = new int[N][N];
        int sign = 1;

        for (int f = 0; f < n; f++) {
            getCofactor(A, temp, 0, f, n);
            D += sign * A[0][f] * determinant(temp, n - 1);
            sign = -sign;
        }
        return D;
    }

    private static int[][] getKeyInverse(int[][] keyMatrix) {
        int d = determinant(keyMatrix, N);
        int[][] keyInverse = new int[3][3];
        int sign = 1;
        int[][] temp = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                getCofactor(keyMatrix, temp, i, j, N);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                keyInverse[j][i] = getReminder(((((sign) * (determinant(temp, N - 1)))) * getInverseOfDeterminant(d)));
            }
        }
        return keyInverse;
    }

    private static int[][] generateKeyMatrix(String key) {
        int[][] keyMatrix = new int[3][3];
        int x = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                keyMatrix[i][j] = key.charAt(x);
                x++;
            }
        }
        return keyMatrix;
    }

    public static List<Byte> setPlainText(List<Byte> plainText) {
        int length = plainText.size();
        int reminder = length % 3;
        if (reminder % 3 == 0) {
            return plainText;
        } else {
            int x = length + 3;
            int y = x - reminder;
            int z = y - length;
            for (int i = 0; i < z; i++) {
                plainText.add((byte)('x'));
            }
        }
        return plainText;
    }

    private static int[][] generatePlainTextMatrix(List<Byte> subPlainText) {
        int[][] plainTextMatrix = new int[3][1];
        for (int i = 0; i < 3; i++) {
            byte c = subPlainText.get(i);
            plainTextMatrix[i][0] = c;

        }
        return plainTextMatrix;
    }

    private static int[][] encrypt(int[][] keyMatrix, int[][] plaintTextVector) {
        int x, i, j;
        int[][] cipherMatrix = new int[3][1];
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 1; j++) {
                cipherMatrix[i][j] = 0;
                for (x = 0; x < 3; x++) {
                    cipherMatrix[i][j] +=
                            keyMatrix[i][x] * plaintTextVector[x][j];
                }
                cipherMatrix[i][j] = getReminder(cipherMatrix[i][j] % 256);
            }
        }
        return cipherMatrix;
    }

    private static StringBuilder generateCipherTextFromCipherMatrix(int[][] cipherMatrix) {
        StringBuilder cipherText = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cipherText.append((char) (cipherMatrix[i][0]));
        }
        return cipherText;
    }

    public static StringBuilder hillCipher(List<Byte> plainText, HillType type) {

        plainText = setPlainText(plainText);
        int[][] keyMatrix = generateKeyMatrix(key);
        if (type == HillType.DECRYPT) {
            keyMatrix = getKeyInverse(keyMatrix);
        }
        StringBuilder cipherText = new StringBuilder();
        List<Byte> subPlainText;
        int[][] plainTextMatrix;
        int[][] cipherMatrix;
        for (int i = 0; i < plainText.size(); i += 3) {
            if (i > (plainText.size() - 2)) {
                return cipherText;
            }
            subPlainText = plainText.subList(i, i + 3);
            plainTextMatrix = generatePlainTextMatrix(subPlainText);
            cipherMatrix = encrypt(keyMatrix, plainTextMatrix);
            cipherText.append(generateCipherTextFromCipherMatrix(cipherMatrix));
        }
        return cipherText;
    }


}