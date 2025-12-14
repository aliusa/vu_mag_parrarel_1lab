package lab3.v11;

public class LongestCommonSubsequence {

    // Skaičiuoja ilgiausią bendrą posekį (LCS)
    public static String lcs(String a, String b) {
        int n = a.length(), m = b.length();
        if (n == 0 || m == 0) return "";

        // dp[i][j] - LCS ilgis tarp a[0..i-1] ir b[0..j-1]
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                if (ca == b.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                else
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        // Atkuria vieną LCS iš dp lentelės
        StringBuilder sb = new StringBuilder();
        int i = n, j = m;
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                sb.append(a.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        String a, b;

        if (args.length >= 2) {
            a = args[0];
            b = args[1];
            System.out.println("Naudojami argumentai iš komandinės eilutės.");
        } else {
            a = "AGGTAB";
            b = "GXTXAYB";
            System.out.println("Naudojimas: java LongestCommonSubsequence <str1> <str2>");
            System.out.println("Nėra argumentų — paleidžiamas pavyzdys:");
        }

        long start = System.nanoTime();
        String result = lcs(a, b);
        long end = System.nanoTime();

        System.out.println("String A: " + a);
        System.out.println("String B: " + b);
        System.out.println("LCS length: " + result.length());
        System.out.println("One LCS    : " + result);
        System.out.printf("Time: %.6f s%n", (end - start) / 1_000_000_000.0);
    }
}
