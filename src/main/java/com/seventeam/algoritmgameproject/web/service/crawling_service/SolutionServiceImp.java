package com.seventeam.algoritmgameproject.web.service.crawling_service;

import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class SolutionServiceImp implements SolutionService {

    private final TestCaseUtil util;
    private final Random random;
    public SolutionServiceImp(TestCaseUtil util) {
        this.util = util;
        this.random = new Random();
    }

    /*
     * 하 문제
     */
    //1번 가운데 글자 가져오기
    public String solution1(String s) {
        int len = s.length();
        int idx = len / 2;
        return ((len & 1) == 1) ? String.valueOf(s.charAt(idx)) : s.substring(idx - 1, idx + 1);
    }

    @Override
    public Set<TestCase> solution1ByEx() {
        String[] ans = {"abcde", "qwer"};
        return solution1Core(ans, true);
    }

    @Override
    public Set<TestCase> solution1TestCases() {
        String[] ans = {"abcde", "qwer", "qwrq", "dgsahdafahsdhsd", "dergsgseg", "GSDgagdsg", "sdffdasf", "sdfgeegbj", "adfsasfd", "asdfadf"};
        return solution1Core(ans, false);
    }

    Set<TestCase> solution1Core(String[] ans, boolean isExample) {
        String type = "String";
        String ansType = "String";
        Set<TestCase> cases = new LinkedHashSet<>();
        for (String an : ans) {
            TestCase testCase = TestCase.builder()
                    .params(an)
                    .answer(solution1(an))
                    .type(type)
                    .ansType(ansType)
                    .build();

            if (isExample) {
                testCase.setExample();
            }
            cases.add(testCase);
        }
        return cases;
    }

    //2번 문자열을 두 정수로 바꾸기
    public int solution2(String s) {
        int answer;
        int result = 0;
        boolean sign = true;

        for (char i : s.toCharArray()) {
            if (i == '-') {
                sign = false;
            } else if (i != '+') {
                result = result * 10 + (i - '0');
            }
        }
        answer = (sign ? 1 : -1) * result;
        return answer;
    }

    @Override
    public Set<TestCase> solution2ByEx() {
        String[] ans = {"1234"};
        return solution2Core(ans, true);
    }

    @Override
    public Set<TestCase> solution2TestCases() {
        String[] ans = {"1234", "-1234", "1231", "-1231", "2432", "-2432", "347", "-347", "52", "-25"};
        return solution2Core(ans, false);
    }

    Set<TestCase> solution2Core(String[] ans, boolean isExample) {
        String type = "String";
        String ansType = "int";
        Set<TestCase> cases = new LinkedHashSet<>();
        for (String an : ans) {
            TestCase testCase = TestCase.builder()
                    .params(an)
                    .answer(String.valueOf(solution2(an)))
                    .type(type)
                    .ansType(ansType)
                    .build();

            if (isExample) {
                testCase.setExample();
            }
            cases.add(testCase);
        }
        return cases;
    }

    //3번 핸드폰 번호 가리기
    public String solution3(String phone_number) {
        char[] ch = phone_number.toCharArray();
        for (int i = 0; i < ch.length - 4; i++) {
            ch[i] = '*';
        }
        return String.valueOf(ch);
    }

    @Override
    public Set<TestCase> solution3ByEx() {
        String[] input = {"01033334444", "027778888"};
        Set<TestCase> cases = new LinkedHashSet<>();
        for (String s : input) {
            cases.add(solution3Core(s, true));
        }
        return cases;
    }

    @Override
    public Set<TestCase> solution3TestCases() {
        Set<TestCase> cases = new LinkedHashSet<>();
        for (int i = 0; i < 8; i++) {
            String randNumber = util.randomPhoneNumber();
            cases.add(solution3Core(randNumber, false));
        }
        return cases;
    }

    TestCase solution3Core(String phone, boolean isExample) {
        //예시 테스트 케이스
        String type = "String";
        String ansType = "String";
        TestCase testCase = TestCase.builder()
                .params(phone)
                .answer(String.valueOf(solution3(phone)))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }

    //x만큼 간격이 있는 n개의 숫자
    public long[] solution4(int x, int n) {
        long[] answer = new long[n];
        int multiple = 1;
        for (int i = 0; i < n; i++) {
            answer[i] = (long) x * multiple;
            multiple++;
        }
        return answer;
    }

    @Override
    public Set<TestCase> solution4ByEx() {
        int[][] ans = {{2, 5}, {4, 3}, {-4, 2}};
        Set<TestCase> testCases = new LinkedHashSet<>();
        for (int[] an : ans) {
            testCases.add(solution4Core(an[0], an[1], true));
        }
        return testCases;
    }

    @Override
    public Set<TestCase> solution4TestCases() {
        Set<TestCase> testCases = new LinkedHashSet<>();
        for (int i = 0; i < 7; i++) {
            int x = random.nextInt(1001);
            int n = random.nextInt(101) + 1;
            if ((x & 1) == 1) {
                x = x * -1;
            }
            testCases.add(solution4Core(x, n, false));
        }
        return testCases;
    }

    TestCase solution4Core(int x, int n, boolean isExample) {
        String type = "int_2";
        String ansType = "long[]";

        TestCase testCase = TestCase.builder()
                .params(x + "," + n)
                .answer(Arrays.toString(solution4(x, n)))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }

    /*
     * 중 문제
     */
    //5. 부족한 금액 계산하기
    public long solution5(int price, int money, int count) {
        long answer = money;

        for (int i = 1; i <= count; i++) {
            answer -= ((long) price * i);
        }
        return answer < 0 ? (answer * -1) : 0;
    }

    @Override
    public Set<TestCase> solution5ByEx() {
        Set<TestCase> cases = new LinkedHashSet<>();
        cases.add(solution5Core(3, 20, 4, true));
        return cases;
    }

    @Override
    public Set<TestCase> solution5TestCases() {
        Set<TestCase> cases = new LinkedHashSet<>();
        for (int i = 0; i < 10; i++) {
            int price = random.nextInt(2500) + 1;
            int money = random.nextInt(1000000000) + 1;
            int count = random.nextInt(2500) + 1;
            cases.add(solution5Core(price, money, count, false));
        }
        return cases;
    }

    TestCase solution5Core(int price, int money, int count, boolean isExample) {

        String type = "int_3";
        String ansType = "long";
        String params = price + "," + money + "," + count;
        TestCase testCase = TestCase.builder()
                .params(params)
                .answer(String.valueOf(solution5(price, money, count)))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }

    // 문제 6번 2016년

    public String solution6(int a, int b) {

        String[] weekOfDays = {"FRI", "SAT", "SUN", "MON", "TUE", "WED", "THU"};
        int[] daysOfMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int idx;

        int totalDay = 0;
        for (int i = 0; i < a - 1; i++) {
            totalDay += daysOfMonth[i];
        }
        totalDay += b - 1; // 일은 1일 부터 시작 함으로
        idx = totalDay % 7;
        return weekOfDays[idx];
    }

    @Override
    public Set<TestCase> solution6ByEx() {
        Set<TestCase> cases = new LinkedHashSet<>();
        cases.add(solution6Core(5, 24, true));
        return cases;

    }

    @Override
    public Set<TestCase> solution6TestCases() {
        int[] daysOfMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        Set<TestCase> cases = new LinkedHashSet<>();
        for (int i = 0; i < 10; i++) {
            int month = random.nextInt(12) + 1;
            int day = random.nextInt(daysOfMonth[month - 1]) + 1;
            cases.add(solution6Core(month, day, false));
        }
        return cases;
    }

    TestCase solution6Core(int a, int b, boolean isExample) {
        String type = "int_2";
        String ansType = "String";
        String params = a + "," + b;
        TestCase testCase = TestCase.builder()
                .params(params)
                .answer(solution6(a, b))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }

    // 문자열 내 p와 y의 개수
    boolean solution7(String s) {
        boolean answer = true;
        int cnt = 0;

        char[] arr = s.toLowerCase().toCharArray();
        for (char i : arr) {
            if ((i - 112) == 0) {
                cnt++;
            } else if ((i - 121) == 0) {
                cnt--;
            }
        }
        if (cnt == 0) return answer;
        else return false;
    }

    @Override
    public Set<TestCase> solution7ByEx() {
        String[] ans = {"pPoooyY", "Pyy"};
        Set<TestCase> cases = new LinkedHashSet<>();
        for (String an : ans) {
            cases.add(solution7Core(an, true));
        }
        return cases;
    }

    @Override
    public Set<TestCase> solution7TestCases() {
        Set<TestCase> cases = new LinkedHashSet<>();
        for (int i = 0; i < 10; i++) {
            int len = random.nextInt(50) + 1;
            cases.add(solution7Core(util.randomStr(len), false));
        }
        return cases;
    }

    TestCase solution7Core(String s, boolean isExample) {
        String type = "String";
        String ansType = "boolean";
        TestCase testCase = TestCase.builder()
                .params(s)
                .answer(String.valueOf(solution7(s)))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }

    public int solution8(int n) {
        int answer = 0;
        int num = 1;
        StringBuilder str = new StringBuilder();

        while (0 < n) {
            str.append(n % 3);
            n = n / 3;
        }

        String[] triple = str.toString().split("");
        for (int i = triple.length - 1; i > -1; i--) {
            answer += Integer.parseInt(triple[i]) * num;
            num *= 3;
        }
        return answer;
    }

    @Override
    public Set<TestCase> solution8ByEx() {
        Set<TestCase> cases = new LinkedHashSet<>();
        int[] ans = {45, 125};
        for (int an : ans) {
            cases.add(solution8Core(an, true));
        }
        return cases;
    }

    @Override
    public Set<TestCase> solution8TestCases() {
        Set<TestCase> cases = new LinkedHashSet<>();
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(100000000) + 1;
            cases.add(solution8Core(n, false));
        }
        return cases;
    }

    TestCase solution8Core(int n, boolean isExample) {
        String type = "int";
        String ansType = "int";
        TestCase testCase = TestCase.builder()
                .params(String.valueOf(n))
                .answer(String.valueOf(solution8(n)))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }

    //문제 9 같은 숫자는 싫어
    public int[] solution9(int[] arr) {
        ArrayList<Integer> tempList = new ArrayList<>();
        int preNum = 10;
        for (int num : arr) {
            if (preNum != num)
                tempList.add(num);
            preNum = num;
        }
        int[] answer = new int[tempList.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = tempList.get(i);
        }
        return answer;
    }

    @Override
    public Set<TestCase> solution9ByEx() {
        Set<TestCase> cases = new LinkedHashSet<>();
        int[][] ans = {{1, 1, 3, 3, 0, 1, 1}, {4, 4, 4, 3, 3}};
        for (int[] an : ans) {
            cases.add(solution9Core(an, true));
        }
        return cases;
    }

    @Override
    public Set<TestCase> solution9TestCases() {
        Set<TestCase> cases = new LinkedHashSet<>();
        for (int i = 0; i < 10; i++) {
            cases.add(solution9Core(util.randomArr(), false));
        }
        return cases;
    }

    TestCase solution9Core(int[] arr, boolean isExample) {

        String type = "int[]";
        String ansType = "int[]";
        TestCase testCase = TestCase.builder()
                .params(Arrays.toString(arr))
                .answer(Arrays.toString(solution9(arr)))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }

    //10번 로또의 최고 순위와 최저 순위
    public int[] solution10(int[] lottos, int[] win_nums) {

        Map<Integer, Boolean> map = new HashMap<>();
        int zeroCount = 0;

        for (int lotto : lottos) {
            if (lotto == 0) {
                zeroCount++;
                continue;
            }
            map.put(lotto, true);
        }


        int sameCount = 0;
        for (int winNum : win_nums) {
            if (map.containsKey(winNum)) sameCount++;
        }

        int maxRank = 7 - (sameCount + zeroCount);
        int minRank = 7 - sameCount;
        if (maxRank > 6) maxRank = 6;
        if (minRank > 6) minRank = 6;

        return new int[]{maxRank, minRank};
    }

    @Override
    public Set<TestCase> solution10ByEx() {
        Set<TestCase> cases = new LinkedHashSet<>();
        int[][] lottos = {{44, 1, 0, 0, 31, 25}, {0, 0, 0, 0, 0, 0}, {45, 4, 35, 20, 3, 9}};
        int[][] win_nums = {{31, 10, 45, 1, 6, 19}, {38, 19, 20, 40, 15, 25}, {20, 9, 3, 45, 4, 35}};
        for (int i = 0; i < lottos.length; i++) {
            cases.add(solution10Core(lottos[i], win_nums[i], true));
        }
        return cases;
    }

    @Override
    public Set<TestCase> solution10TestCases() {
        Set<TestCase> cases = new LinkedHashSet<>();
        for (int i = 0; i < 10; i++) {
            int[][] params = util.randomParamsSol10();
            cases.add(solution10Core(params[0], params[1], false));
        }
        return cases;
    }

    TestCase solution10Core(int[] lottos, int[] win_nums, boolean isExample) {
        String type = "int[]_2";
        String ansType = "int[]";
        TestCase testCase = TestCase.builder()
                .params(Arrays.toString(lottos) + "_" + Arrays.toString(win_nums))
                .answer(Arrays.toString(solution10(lottos, win_nums)))
                .type(type)
                .ansType(ansType)
                .build();
        if (isExample) {
            testCase.setExample();
        }
        return testCase;
    }
    @Override
    public Set<TestCase>generatedTestCases(int i){
        Set<TestCase> cases = new LinkedHashSet<>();
        switch (i){
            case 0:{
                cases.addAll(solution1ByEx());
                cases.addAll(solution1TestCases());
                break;
            }
            case 1:{
                cases.addAll(solution2ByEx());
                cases.addAll(solution2TestCases());
                break;
            }
            case 2:{
                cases.addAll(solution3ByEx());
                cases.addAll(solution3TestCases());
                break;
            }
            case 3:{
                cases.addAll(solution4ByEx());
                cases.addAll(solution4TestCases());
                break;
            }
            case 4:{
                cases.addAll(solution5ByEx());
                cases.addAll(solution5TestCases());
                break;
            }
            case 5:{
                cases.addAll(solution6ByEx());
                cases.addAll(solution6TestCases());
                break;
            }
            case 6:{
                cases.addAll(solution7ByEx());
                cases.addAll(solution7TestCases());
                break;
            }
            case 7:{
                cases.addAll(solution8ByEx());
                cases.addAll(solution8TestCases());
                break;
            }
            case 8:{
                cases.addAll(solution9ByEx());
                cases.addAll(solution9TestCases());
                break;
            }
            case 9:{
                cases.addAll(solution10ByEx());
                cases.addAll(solution10TestCases());
                break;
            }
        }
        for (TestCase aCase : cases) {
            log.info(aCase.toString());
        }

        return cases;
    }
}
