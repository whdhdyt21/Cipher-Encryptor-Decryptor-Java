package org.chiper.kriptografi;

import java.util.Arrays;

public class CipherUtils {

    // Vigenere Cipher
    public static String vigenereEncrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                result.append((char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A'));
                j = ++j % key.length();
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String vigenereDecrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase();
        key = key.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                result.append((char) ((c - key.charAt(j) + 26) % 26 + 'A'));
                j = ++j % key.length();
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Playfair Cipher
    private static char[][] generatePlayfairMatrix(String key) {
        key = key.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder sb = new StringBuilder();
        boolean[] used = new boolean[26];

        for (char c : key.toCharArray()) {
            if (!used[c - 'A']) {
                sb.append(c);
                used[c - 'A'] = true;
            }
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') continue;
            if (!used[c - 'A']) {
                sb.append(c);
                used[c - 'A'] = true;
            }
        }

        char[][] matrix = new char[5][5];
        for (int i = 0, k = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = sb.charAt(k++);
            }
        }

        return matrix;
    }

    public static String playfairEncrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        char[][] matrix = generatePlayfairMatrix(key);
        StringBuilder result = new StringBuilder();

        if (text.length() % 2 != 0) text += "X";

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(matrix, a);
            int[] posB = findPosition(matrix, b);

            if (posA[0] == posB[0]) {
                result.append(matrix[posA[0]][(posA[1] + 1) % 5]);
                result.append(matrix[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) {
                result.append(matrix[(posA[0] + 1) % 5][posA[1]]);
                result.append(matrix[(posB[0] + 1) % 5][posB[1]]);
            } else {
                result.append(matrix[posA[0]][posB[1]]);
                result.append(matrix[posB[0]][posA[1]]);
            }
        }

        return result.toString();
    }

    public static String playfairDecrypt(String text, String key) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        char[][] matrix = generatePlayfairMatrix(key);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(matrix, a);
            int[] posB = findPosition(matrix, b);

            if (posA[0] == posB[0]) {
                result.append(matrix[posA[0]][(posA[1] + 4) % 5]);
                result.append(matrix[posB[0]][(posB[1] + 4) % 5]);
            } else if (posA[1] == posB[1]) {
                result.append(matrix[(posA[0] + 4) % 5][posA[1]]);
                result.append(matrix[(posB[0] + 4) % 5][posB[1]]);
            } else {
                result.append(matrix[posA[0]][posB[1]]);
                result.append(matrix[posB[0]][posA[1]]);
            }
        }

        return result.toString();
    }

    private static int[] findPosition(char[][] matrix, char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) return new int[]{i, j};
            }
        }
        return null;
    }

    private static int[][] createMatrix(String key) {
        if (key.length() != 16) {
            throw new IllegalArgumentException("Kunci harus memiliki panjang 16 karakter untuk matriks 4x4");
        }
        int[][] matrix = new int[4][4];
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = key.charAt(k++) - 'A';
            }
        }
        return matrix;
    }

    public static String hillEncrypt(String message, String key) {
        int[][] matrix = createMatrix(key);
        String paddedMessage = padMessage(message);
        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < paddedMessage.length(); i += 4) {
            int[] vector = new int[4];
            for (int j = 0; j < 4; j++) {
                vector[j] = paddedMessage.charAt(i + j) - 'A';
            }
            int[] result = multiplyMatrixByVector(matrix, vector);
            for (int value : result) {
                encrypted.append((char) ((value % 26 + 26) % 26 + 'A'));
            }
        }
        return encrypted.toString();
    }

    public static String hillDecrypt(String message, String key) {
        int[][] matrix = createMatrix(key);
        int[][] inverseMatrix = inverseMatrix(matrix, 4);
        StringBuilder decrypted = new StringBuilder();

        for (int i = 0; i < message.length(); i += 4) {
            int[] vector = new int[4];
            for (int j = 0; j < 4; j++) {
                vector[j] = message.charAt(i + j) - 'A';
            }
            int[] result = multiplyMatrixByVector(inverseMatrix, vector);
            for (int value : result) {
                decrypted.append((char) ((value % 26 + 26) % 26 + 'A'));
            }
        }
        return decrypted.toString();
    }

    private static int[] multiplyMatrixByVector(int[][] matrix, int[] vector) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
            result[i] = ((result[i] % 26) + 26) % 26;  // Ensure positive result
        }
        return result;
    }

    private static String padMessage(String message) {
        StringBuilder paddedMessage = new StringBuilder(message.toUpperCase());
        while (paddedMessage.length() % 4 != 0) {
            paddedMessage.append('X');
        }
        return paddedMessage.toString();
    }

    private static int[][] inverseMatrix(int[][] matrix, int n) {
        int det = determinant(matrix, n);
        int modDet = ((det % 26) + 26) % 26;
        int invDet = modInverse(modDet, 26);
        if (invDet == -1) {
            throw new IllegalArgumentException("Matriks tidak memiliki invers modulo 26");
        }
        int[][] adj = adjoint(matrix, n);
        int[][] inv = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = ((adj[i][j] * invDet) % 26 + 26) % 26;
            }
        }
        return inv;
    }

    private static int[][] adjoint(int[][] matrix, int n) {
        int[][] adj = new int[n][n];
        if (n == 1) {
            adj[0][0] = 1;
            return adj;
        }

        int sign = 1;
        int[][] temp = new int[n-1][n-1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                getCofactor(matrix, temp, i, j, n);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = (sign * (determinant(temp, n-1) + 26)) % 26;
            }
        }

        return adj;
    }

    private static void getCofactor(int[][] matrix, int[][] temp, int p, int q, int n) {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = matrix[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    private static int determinant(int[][] matrix, int n) {
        int det = 0;
        if (n == 1) {
            return matrix[0][0];
        }
        int[][] temp = new int[n-1][n-1];
        int sign = 1;
        for (int f = 0; f < n; f++) {
            getCofactor(matrix, temp, 0, f, n);
            det += sign * matrix[0][f] * determinant(temp, n-1);
            sign = -sign;
        }
        return ((det % 26) + 26) % 26;
    }

    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }
}