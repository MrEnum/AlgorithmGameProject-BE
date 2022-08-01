package com.seventeam.algoritmgameproject.web.service.crawling_service;


import com.seventeam.algoritmgameproject.domain.QuestionLevel;
import com.seventeam.algoritmgameproject.domain.model.questions.Question;
import com.seventeam.algoritmgameproject.domain.model.questions.TestCase;
import com.seventeam.algoritmgameproject.web.repository.questions_repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionCrawlingServiceImp implements QuestionCrawlingService {

    private final QuestionRepository repository;
    private final SolutionService solutionService;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String WEBDRIVER = "webdriver.chrome.driver";
    private static final String WEB_DRIVER_PATH = "C:/Users/biolk/Downloads/chromedriver.exe";
    private WebDriver driver;
    private ChromeOptions options;
    private static final String[] languages = {"java", "javascript", "python3"};
    private static final String[] QUESTION_URL = {
            //하
            "https://programmers.co.kr/learn/courses/30/lessons/12903",
            "https://programmers.co.kr/learn/courses/30/lessons/12925",
            "https://programmers.co.kr/learn/courses/30/lessons/12948",
            "https://programmers.co.kr/learn/courses/30/lessons/12954",

            //중
            "https://programmers.co.kr/learn/courses/30/lessons/82612",
            "https://programmers.co.kr/learn/courses/30/lessons/12901",
            "https://programmers.co.kr/learn/courses/30/lessons/12916",

            //상
            "https://programmers.co.kr/learn/courses/30/lessons/68935",
            "https://programmers.co.kr/learn/courses/30/lessons/12906",
            // "https://programmers.co.kr/learn/courses/30/lessons/77484"

    };

    @PostConstruct
    void init() {
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

    //기본 문제 저장
    @Transactional
    public void saveDefaultQuestions() {

        List<Question> list = new ArrayList<>();
        QuestionLevel level;
        for (int i = 0; i < QUESTION_URL.length; i++) {
            if (i < 4) {
                level = QuestionLevel.EASY;
            } else if (6 < i) {
                level = QuestionLevel.HARD;
            } else {
                level = QuestionLevel.NORMAL;
            }
            Question question = uploadQuestion(QUESTION_URL[i], level);

            //해당 문제 테스트케이스 생성(1~10)
            Set<TestCase> testCases = solutionService.generatedTestCases(i);
            for (TestCase atomic : testCases) {
                question.add(atomic);
                log.info(atomic.toString());
            }
            list.add(question);
        }
        repository.saveAll(list);
    }

    @Override
    public void initQuestionIdByLevel() {

    }


    //문제 추가
    public Question uploadQuestion(String questionUrl, QuestionLevel level) {

        boolean isGetQuestions = true;
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
                driver.get(questionUrl + "?language=" + language);

                //한번만 문제 가져오기
                if (isGetQuestions) {
                    isGetQuestions = false;

                    title = driver.findElement(By.xpath("//*[@id=\"tab\"]/li")).getText();

                    if (questionUrl.equals(QUESTION_URL[5])) {
                        questionDescription = getQuestion6();
                    } else if (questionUrl.equals(QUESTION_URL[8])) {
                        questionDescription = getQuestion9();
                    } else {
                        questionDescription = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();
                    }

                    if (questionUrl.equals(QUESTION_URL[8])) {
                        limitation = getQuestion9Limitation();
                    } else {
                        limitation = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul")).getText();
                    }


                    try {
                        inOutExHead = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table/thead")).getText();
                        inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table/tbody")).getText();
                    } catch (NoSuchElementException e) {
                        inOutExHead = "";
                        inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
                    }


                    try {
                        if (questionUrl.equals(QUESTION_URL[5]) || questionUrl.equals(QUESTION_URL[8])) {
                            inOutExDescription = "No Info";
                        } else if (questionUrl.equals(QUESTION_URL[7])) {
                            inOutExDescription = getQuestion8InoutDescription();
                        } else {
                            inOutExDescription = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
                        }
                    } catch (NoSuchElementException e) {
                        inOutExDescription = "No Info";
                    }

                    try {
                        if (questionUrl.equals(QUESTION_URL[7]) || questionUrl.equals(QUESTION_URL[8])) {
                            reference = "No Info";
                        } else {
                            reference = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[2]")).getText();
                        }

                    } catch (NoSuchElementException e) {
                        reference = "No Info";
                    }

                }

                //코드 태그 목록 추출, 합치기
                StringBuilder buffer = new StringBuilder();
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
                .question(questionDescription)
                .limitation(limitation)
                .inOutExHead(inOutExHead)
                .inOutEx(inOutEx)
                .inOutExDescription(inOutExDescription)
                .reference(reference)
                .level(level)
                .templates(codesTemplate)
                .build();
    }

    public String getQuestion6() {
        String question1 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();
        String question2 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[2]")).getText();
        return question1 + question2;
    }

    public String getQuestion8InoutDescription() {
        return "답을 도출하는 과정은 다음과 같습니다. 45(10) -> 1200(3) -> 0021(3 진법 반전) -> 7(10)";
    }

    public String getQuestion9() {
        String text = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();
        String text2 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[1]")).getText();
        String text3 = driver.findElement(By.xpath(" //*[@id=\"tour2\"]/div/div/p[2]")).getText();
        return text + text2 + text3;
    }

    public String getQuestion9Limitation() {
        return driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[2]")).getText();
    }

    public String[] question10Template() {//*[@id="tour2"]/div/div/table[2]

        String q1 = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/p[1]")).getText();
        q1 = q1.substring(0, q1.length() - 1);
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
        String question = q1 + q2 + q3 + q4 + q5 + q6;
        String limitation = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/ul[2]")).getText();
        String inOutExHead = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table[3]/thead")).getText();
        String inOutEx = driver.findElement(By.xpath("//*[@id=\"tour2\"]/div/div/table[3]/tbody")).getText();
        String reference = driver.findElement(By.xpath("//*[@id=\"fn1\"]/p")).getText();
        String inOutExDescription = "No info";
        return new String[]{question, limitation, inOutExHead, inOutEx, inOutExDescription, reference};
    }


}
