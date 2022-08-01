package com.seventeam.algoritmgameproject.service.crawlingService;

import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgrammersTest {

    private static final String WEBDRIVER = "webdriver.chrome.driver";
    private static final String WEB_DRIVER_PATH = "C:/Users/biolk/Downloads/chromedriver.exe";
    private WebDriver driver;
    private ChromeOptions options;
    private static final String[] QuestionUrl = {
            //하
            "https://programmers.co.kr/learn/courses/30/lessons/12903",
            "https://programmers.co.kr/learn/courses/30/lessons/12925",
            "https://programmers.co.kr/learn/courses/30/lessons/12948",
            "https://programmers.co.kr/learn/courses/30/lessons/12954",

            //중
            "https://programmers.co.kr/learn/courses/30/lessons/82612",
            "https://programmers.co.kr/learn/courses/30/lessons/12901",//여기서 부터 시작
            "https://programmers.co.kr/learn/courses/30/lessons/12916",

            //상
            "https://programmers.co.kr/learn/courses/30/lessons/68935",
            "https://programmers.co.kr/learn/courses/30/lessons/12906",
            //"https://programmers.co.kr/learn/courses/30/lessons/77484"

    };

    private static final String[] languages = {"java", "javascript", "python3"};

    @Before
    public void setDriverSetting() {
        System.setProperty(WEBDRIVER, WEB_DRIVER_PATH);
        options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--lang=ko");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-gpu");
        options.setCapability("ignoreProtectedModeSettings", true);

        //페이지가 로드될 때까지 대기
        //Normal: 로드 이벤트 실행이 반환 될 때 까지 기다린다.
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
    }

//    @Test
//    @DisplayName("문제 출력")
//    public void 문제_및_언어_템플릿_가져오기() {
//        int cnt = 0;
//        List<Question> list = new ArrayList<>();
//
//        for (String url : QuestionUrl) {
//            //given
//            boolean isGetQuestions = true;
//            Question question = null;
//            //문제 제목
//            String title = null;
//            //문제 설명
//            String questionDescription= null;
//            //제한 조건
//            String limitation= null;
//            //입출력 예
//            String inOutExHead= null;
//            String inOutEx= null;
//            //입출력 예 설명
//            String inOutExDescription= null;
//            //참고 사항
//            String reference= null;
//            //난이도
//            String level= null;
//            Map<String, String> codesTemplate = new HashMap<>();
//
//            try {
//                //세션 시작
//                driver = new ChromeDriver(options);
//                //when
//                for (String language : languages) {
//
//                    driver.get(url + "?language=" + language);
//
//                    //한번만 문제 가져오기
//                    if (isGetQuestions) {
//                        isGetQuestions = false;
//
//
//                       title = driver.findElement(By.xpath("//*[@id=\"tab\"]/li")).getText();
//
//                        questionDescription = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();
//
//                        limitation = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul")).getText();
//
//
//                        try {
//                            inOutExHead = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table/thead")).getText();
//                            inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table/tbody")).getText();
//                        } catch (NoSuchElementException e) {
//                            inOutExHead = "";
//                            inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
//                        }
//
//
//
//                        try {
//                            inOutExDescription = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
//                        } catch (NoSuchElementException e) {
//                            inOutExDescription = "No Info";
//                        }
//
//                        try {
//                            reference = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[2]")).getText();
//                        } catch (NoSuchElementException e) {
//                            reference = "No Info";
//                        }
//
//
//                        if (cnt < 4) {
//                            level = "하";
//                        } else if (6 < cnt) {
//                            level = "상";
//                        } else {
//                            level = "중";
//                        }
//
//                        cnt++;
//                    }
//
//                    //코드 태그 목록 추출, 합치기
//                    StringBuilder buffer = new StringBuilder();
//                    List<WebElement> solutions = driver.findElements(By.className("CodeMirror-line")); //모든 코드 줄 가져오기
//
//                    for (WebElement solution : solutions) {
//                        String eachLine = solution.findElement(By.tagName("span")).getText();
//                        buffer.append(eachLine);
//
//                    }
//                    codesTemplate.put(language, buffer.toString());
//                }
//
//                //객체 생성
//                question = Question.builder()
//                        .title(title)
//                        .questionDescription(questionDescription)
//                        .limitation(limitation)
//                        .inOutExHead(inOutExHead)
//                        .inOutEx(inOutEx)
//                        .inOutExDescription(inOutExDescription)
//                        .reference(reference)
//                        .level(level)
//                        .templates(codesTemplate)
//                        .build();
//
//            } finally {
//                driver.quit();
//            }
//            list.add(question);
//            //여기에서 맵데이터 엔티티에 넣기
//            question.templates = codesTemplate;
//        }
//        for (Question question : list) {
//            System.out.println(question.toString());
//        }
//    }

    @Test
    @DisplayName("기본 코드 템플릿")
    public void getSolutions() {

        driver = new ChromeDriver(options);
        List<String> questions = new ArrayList<>();
        try {
            String[] languages = {"java"}; //가져올 언어
            for (String language : languages) {

                //문제 url
                String url = "https://programmers.co.kr/learn/courses/30/lessons/12969?language=";
                driver.get(url + language);

                //코드 태그 목록 추출, 합치기
                StringBuilder buffer = new StringBuilder();
                List<WebElement> solutions = driver.findElements(By.className("CodeMirror-line")); //모든 코드 줄 가져오기

                for (WebElement solution : solutions) {
                    String eachLine = solution.findElement(By.tagName("span")).getText();
                    buffer.append(eachLine);
                }
                int i = buffer.indexOf("{")+1;
                buffer.insert(i, "\n");
                questions.add(buffer.toString());
            }
        } finally {
            driver.close();
        }
        System.out.println(questions);
    }

    @Test
    @DisplayName("크롤링 코드 리팩토링")
    public void test() {
        List<Question> list = new ArrayList<>();
        QuestionLevel level;
        for (int i = 0; i < QuestionUrl.length; i++) {
            if (i < 4) {
                level = QuestionLevel.EASY;
            } else if (6 < i) {
                level = QuestionLevel.HARD;
            } else {
                level = QuestionLevel.NORMAL;
            }
            Question question = uploadQuestion(QuestionUrl[i], level);
            list.add(question);
        }
        for (Question question : list) {
            System.out.println(question.toString());
        }
    }

    @Test
    @DisplayName("6번 문제 가져오기")
    public void 육번() {
        driver = new ChromeDriver(options);
        driver.get("https://programmers.co.kr/learn/courses/30/lessons/12901");
        String question1 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();
        String question2 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
        System.out.println(question1 + question2);
        driver.quit();
    }

    @Test
    @DisplayName("10번 문제 가져오기")
    public void 구번() {
        driver = new ChromeDriver(options);
        driver.get("https://programmers.co.kr/learn/courses/30/lessons/77484");
        String title = driver.findElement(By.xpath("//*[@id=\"tab\"]/li")).getText();

        String q1 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();
        q1 = q1.substring(0, q1.length()-1);
        String q2 = "\n<table class=\"table\">\n" +
                "        <thead><tr>\n" +
                "<th>순위</th>\n" +
                "<th>당첨 내용</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "        <tbody><tr>\n" +
                "<td>1</td>\n" +
                "<td>6개 번호가 모두 일치</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>2</td>\n" +
                "<td>5개 번호가 일치</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>3</td>\n" +
                "<td>4개 번호가 일치</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>4</td>\n" +
                "<td>3개 번호가 일치</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>5</td>\n" +
                "<td>2개 번호가 일치</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>6(낙첨)</td>\n" +
                "<td>그 외</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "      </table>";
        String q3 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
        String q4 = "<table class=\"table\">\n" +
                "        <thead><tr>\n" +
                "<th>당첨 번호</th>\n" +
                "<th>31</th>\n" +
                "<th>10</th>\n" +
                "<th>45</th>\n" +
                "<th>1</th>\n" +
                "<th>6</th>\n" +
                "<th>19</th>\n" +
                "<th>결과</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "        <tbody><tr>\n" +
                "<td>최고 순위 번호</td>\n" +
                "<td><u><strong>31</strong></u></td>\n" +
                "<td>0→<u><strong>10</strong></u></td>\n" +
                "<td>44</td>\n" +
                "<td><u><strong>1</strong></u></td>\n" +
                "<td>0→<u><strong>6</strong></u></td>\n" +
                "<td>25</td>\n" +
                "<td>4개 번호 일치, 3등</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>최저 순위 번호</td>\n" +
                "<td><u><strong>31</strong></u></td>\n" +
                "<td>0→11</td>\n" +
                "<td>44</td>\n" +
                "<td><u><strong>1</strong></u></td>\n" +
                "<td>0→7</td>\n" +
                "<td>25</td>\n" +
                "<td>2개 번호 일치, 5등</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "      </table>\n";
        String q5 = "<ul>\n" +
                "<li>순서와 상관없이, 구매한 로또에 당첨 번호와 일치하는 번호가 있으면 맞힌 걸로 인정됩니다. </li>\n" +
                "<li>알아볼 수 없는 두 개의 번호를 각각 10, 6이라고 가정하면 3등에 당첨될 수 있습니다. \n" +
                "\n" +
                "<ul>\n" +
                "<li>3등을 만드는 다른 방법들도 존재합니다. 하지만, 2등 이상으로 만드는 것은 불가능합니다. </li>\n" +
                "</ul></li>\n" +
                "<li>알아볼 수 없는 두 개의 번호를 각각 11, 7이라고 가정하면 5등에 당첨될 수 있습니다. \n" +
                "\n" +
                "<ul>\n" +
                "<li>5등을 만드는 다른 방법들도 존재합니다. 하지만, 6등(낙첨)으로 만드는 것은 불가능합니다.</li>\n" +
                "</ul></li>\n" +
                "</ul>";
        String q6 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[3]")).getText();
        String question =  q1+q2+q3+q4+q5+q6;
        String limitation = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[2]")).getText();
        String inOutExHead = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table[3]/thead")).getText();
        String inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table[3]/tbody")).getText();
        String reference = driver.findElement(By.xpath("//*[@id=\"fn1\"]/p")).getText();
        Question s =Question.builder()
                .title(title)
                .questionDescription(question)
                .limitation(limitation)
                .inOutExHead(inOutExHead)
                .inOutEx(inOutEx)
                .inOutExDescription("No Info")
                .reference(reference)
                .build();
        System.out.println(s);
        //System.out.println(driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[2]")).getText());
        driver.quit();
    }

    public Question uploadQuestion(String questionUrl, QuestionLevel level) {
        //given
        boolean isGetQuestions = true;
        com.seventeam.algoritmgameproject.domain.model.questions.Question question = null;
        //문제 제목
        String title = null;
        //문제 설명
        String questionDescription = null;
        //제한 조건
        String limitation = null;
        //입출력 예
        String inOutExHead = null;
        String inOutEx = null;
        //입출력 예 설명
        String inOutExDescription = null;
        //참고 사항
        String reference = null;
        //난이도
        Map<String, String> codesTemplate = new HashMap<>();

        for (String language : languages) {
            try {
                //세션 시작
                driver = new ChromeDriver(options);
                //when


                driver.get(questionUrl + "?language=" + language);

                //한번만 문제 가져오기
                if (isGetQuestions) {
                    isGetQuestions = false;

                    title = driver.findElement(By.xpath("//*[@id=\"tab\"]/li")).getText();

                    questionDescription = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();

                    limitation = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul")).getText();


                    try {
                        inOutExHead = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table/thead")).getText();
                        inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table/tbody")).getText();
                    } catch (NoSuchElementException e) {
                        inOutExHead = "";
                        inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
                    }


                    try {
                        inOutExDescription = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
                    } catch (NoSuchElementException e) {
                        inOutExDescription = "No Info";
                    }

                    try {
                        reference = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[2]")).getText();
                    } catch (NoSuchElementException e) {
                        reference = "No Info";
                    }

                }

                //코드 태그 목록 추출, 합치기
                StringBuffer buffer = new StringBuffer();
                List<WebElement> solutions = driver.findElements(By.className("CodeMirror-line")); //모든 코드 줄 가져오기

                for (WebElement solution : solutions) {
                    String eachLine = solution.findElement(By.tagName("span")).getText();
                    buffer.append(eachLine);

                }
                codesTemplate.put(language, buffer.toString());
            } finally {
                driver.quit();
            }
        }
        return Question.builder()
                .title(title)
                .questionDescription(questionDescription)
                .limitation(limitation)
                .inOutExHead(inOutExHead)
                .inOutEx(inOutEx)
                .inOutExDescription(inOutExDescription)
                .reference(reference)
                .level(level)
                .templates(codesTemplate)
                .build();
    }

}

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
class Question {
    String title;
    //문제 설명
    String questionDescription;
    //제한 조건
    String limitation;
    //입출력 예 header
    String inOutExHead;
    //입출력 예 Body
    String inOutEx;
    //입출력 예 설명(null 허용)
    String inOutExDescription;
    //참고 사항(null 허용)
    String reference;
    //난이도
    QuestionLevel level;
    // 언어별 문제 기본 템플릿
    Map<String, String> templates = new HashMap<>();

    @Builder
    public Question(String title, String questionDescription, String limitation, String inOutExHead, String inOutEx, String inOutExDescription, String reference, QuestionLevel level, Map<String, String> templates) {
        this.title = title;
        this.questionDescription = questionDescription;
        this.limitation = limitation;
        this.inOutExHead = inOutExHead;
        this.inOutEx = inOutEx;
        this.inOutExDescription = inOutExDescription;
        this.reference = reference;
        this.level = level;
        this.templates = templates;

    }
}