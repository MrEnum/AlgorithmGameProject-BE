package com.seventeam.algoritmgameproject.service.compilerService.generatedTemplate;

public class QuestionsStr {
    public static final String code1 = "public class Sol {\n" +
            "        \n" +
            "    public String solution(String s) {\n" +
            "        int len = s.length();\n" +
            "        int idx = len / 2;\n" +
            "        return ((len & 1) == 1)?String.valueOf(s.charAt(idx)):s.substring(idx - 1, idx + 1);\n" +
            "    \n" +
            "    }\n" +
            "}";
    public static final String code2 = "public class Solion {\n" +
            "    public int solution(String y) {\n" +
            "        int answer = 0;\n" +
            "        int result = 0;\n" +
            "        boolean sign = true;\n" +
            "        \n" +
            "        for  (char i : s.toCharArray()){\n" +
            "            if(i == '-'){\n" +
            "                sign = false; \n" +
            "            }else if(i != '+'){\n" +
            "              result = result*10+(i - '0');\n" +
            "            }\n" +
            "        }\n" +
            "        answer = (sign?1:-1)*result;\n" +
            "        return answer;\n" +
            "    }\n" +
            "}";
    public static final String code3 = "public class Soln {\n" +
            "    public String solution(String phone_number) {\n" +
            "         char[] ch = phone_number.toCharArray();\n" +
            "        for(int i = 0; i < ch.length - 4; i ++){\n" +
            "            ch[i] = '*';\n" +
            "        }\n" +
            "        return String.valueOf(ch);\n" +
            "    }\n" +
            "}";
    public static final String code4 = " public class Solution {\n" +
            "    public long[] solution(int y, int n) {\n" +
            "        long[] answer = new long[n];\n" +
            "        int multiple = 1;\n" +
            "        for (int i=0; i<n; i++){\n" +
            "            answer[i] =(long)x*multiple;\n" +
            "            multiple++;\n" +
            "        }\n" +
            "        return answer;\n" +
            "    }\n" +
            "}";
    public static final String code5 = "public class Solution {\n" +
            "    public long solution(int price, int money, int count) {\n" +
            "        long answer = money;\n" +
            "        \n" +
            "        for(int i=1; i<=count; i++){\n" +
            "            answer -= (price*i);\n" +
            "        }\n" +
            "        \n" +
            "        return answer<0?(answer*-1):0;\n" +
            "    }\n" +
            "}";
    static final String code6 = "public class Solution {\n" +
            "    \n" +
            "    public String solution(int a, int b) {\n" +
            "       \n" +
            "        String[] weekOfDays = {\"FRI\", \"SAT\", \"SUN\", \"MON\", \"TUE\", \"WED\", \"THU\"};\n" +
            "        int[] daysOfMonth = {31,29,31,30,31,30,31,31,30,31,30,31};\n" +
            "        int idx = 0;\n" +
            "\n" +
            "        int totalDay = 0;\n" +
            "        for(int i=0;i<a-1;i++){\n" +
            "            totalDay+=daysOfMonth[i];\n" +
            "        }\n" +
            "        totalDay +=  b-1;\n" +
            "        idx = totalDay%7;\n" +
            "        return weekOfDays[idx].toString();\n" +
            "    }\n" +
            "}";
    static final String code7 = "public class Solution {\n" +
            "    boolean solution(String s) {\n" +
            "        boolean answer = true;\n" +
            "        int cnt =0;\n" +
            "\n" +
            "        char[] arr = s.toLowerCase().toCharArray();\n" +
            "        for(char i: arr){\n" +
            "            if((i-112)==0){\n" +
            "                cnt++;\n" +
            "            }else if((i-121)==0) {\n" +
            "                cnt--;\n" +
            "            }\n" +
            "        }\n" +
            "        if(cnt==0)return answer;\n" +
            "        else return false;\n" +
            "    }\n" +
            "}";
    static final String code8 = "public class Solution {\n" +
            "    public int solution(int n) {\n" +
            "        int answer = 0;\n" +
            "        int num = 1;\n" +
            "        StringBuilder str = new StringBuilder();\n" +
            "        \n" +
            "        while(0<n){\n" +
            "            str.append(n%3);\n" +
            "            n = n/3;\n" +
            "        }\n" +
            "        \n" +
            "        String[] triple = str.toString().split(\"\"); \n" +
            "        for(int i =triple.length-1; i>-1 ; i--){\n" +
            "            answer+=Integer.parseInt(triple[i])*num;\n" +
            "            num*=3;\n" +
            "        }\n" +
            "        return answer;\n" +
            "    }\n" +
            "}";
    static final String code9 = "import java.util.*;\n" +
            "\n" +
            "public class Solution {\n" +
            "    public int[] solution(int []arr) {\n" +
            "       \n" +
            "        int compare = -1;\n" +
            "        List<Integer> list = new LinkedList<>();\n" +
            "       \n" +
            "        for(int i=0; i< arr.length; i++){\n" +
            "           \n" +
            "            if(compare != arr[i]){\n" +
            "                compare = arr[i];\n" +
            "                list.add(arr[i]);\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        return list.stream().mapToInt(p1->p1).toArray();\n" +
            "    }\n" +
            "}";
}

