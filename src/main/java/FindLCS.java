public class FindLCS {
    public int[][] findLCS(String stationName, String standardName) {
        int stationLen = stationName.length();
        int standardLen = standardName.length();
        char[] stationArr = stationName.toCharArray();
        char[] standardArr = standardName.toCharArray();

        //
        int[][] dp = new int[stationLen + 1][standardLen + 1];
        int flag = 0;
        for (int i = 0; i <= stationLen; i++) {
            dp[i][0] = 0;
        }

        for (int j = 0; j <= standardLen; j++) {
            dp[0][j] = 0;
        }
        //子序列最长长度
        for (int i = 1; i <= stationLen; i++) {
            for (int j = 1; j <= standardLen; j++) {
                if (stationArr[i - 1] == standardArr[j - 1]) {
                    //当当前两个字符串相等,那么当前最大长度为前一字符长度+1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp;
    }
    public String FindLCSstr(String input, String standard){
        String LCSstr = "";
        //最长子序列
        int [][]dp = findLCS(input,standard);
        int i = input.length(), j = standard.length();
        char []inputArr = input.toCharArray();
        char []standardArr = standard.toCharArray();
        while(i >= 1  && j >= 1){
            if(inputArr[i-1] == standardArr[j-1]){
                LCSstr = inputArr[i-1] + LCSstr;
                i--;
                j--;
            }
            else if(dp[i-1][j] >= dp[i][j-1]){
                i--;
            }
            else{
                j--;
            }
        }
        return LCSstr;
    }
}
