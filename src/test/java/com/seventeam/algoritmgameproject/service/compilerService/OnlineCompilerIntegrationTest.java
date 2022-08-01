package com.seventeam.algoritmgameproject.service.compilerService;


//@SpringBootTest
//@Slf4j
//public class OnlineCompilerIntegrationTest {
//
//
//    @Autowired
//    private TestCaseDslRepository repository;
//
//    @Autowired
//    private JDoodleApi service;
//    @Autowired
//    Map<String, GeneratedTemplate> beans;
//
//    @Autowired
//    CompilerService compilerService;
//
//    @Test
//    @DisplayName("자바 컴파일 테스트")
//    void 자바() {
//        GeneratedJavaTemplateImpTest java = new GeneratedJavaTemplateImpTest(repository);
//
//        log.info(service.compile(template.compileCode(QuestionsStr.code1, 125L),Language.JAVA));
//        log.info(service.compile(compileCode(QuestionsStr.code2,138L)));
//        log.info(service.compile(compileCode(QuestionsStr.code3,150L)));
//        log.info(service.compile(compileCode(QuestionsStr.code4,161L)));
//        log.info(service.compile(compileCode(QuestionsStr.code5,172L)));
//        log.info(service.compile(compileCode(QuestionsStr.code6,184L)));
//        log.info(service.compile( compileCode(QuestionsStr.code7,196L)));
//        log.info(service.compile(compileCode(QuestionsStr.code8,209L)));
//        log.info(service.compile(compileCode(QuestionsStr.code9,222L)));

// }

//  @Test
//  @DisplayName("자바스크립트 코드 문자열 추가 확인 테스트")
//   void 자바스크립트문자열추가() throws InterruptedException, ScriptException {
//        GeneratedJavascriptTemplateImpTest js = new GeneratedJavascriptTemplateImpTest(repository);
//
//        compile(js.compileCode(QuestionStrToJS.code1, 125L));
//
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn"); //  --- (1)
//        engine.eval(js.compileCode(QuestionStrToJS.code1, 125L));
//        log.info(js.compileCode(QuestionStrToJS.code2, 138L));
//        log.info(js.compileCode(QuestionStrToJS.code3, 150L));
//        log.info(js.compileCode(QuestionStrToJS.code4, 161L));
//        log.info(js.compileCode(QuestionStrToJS.code5, 172L));
//        log.info(js.compileCode(QuestionStrToJS.code6, 184L));
//        log.info(js.compileCode(QuestionStrToJS.code7, 196L));
//        log.info(js.compileCode(QuestionStrToJS.code8, 209L));
//        log.info(js.compileCode(QuestionStrToJS.code9, 222L));
//   }

//   public void compile(String codeStr) throws InterruptedException {
//        try (Context context = Context.create("js")) {
//            Value js = context.eval("js", codeStr);
//            System.out.println("값:" + js.toString());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

//   }

//   @Test
//  @DisplayName("자바스크립트 컴파일 테스트")
//   void 자바스크립트() {
//        GeneratedJavascriptTemplateImpTest js = new GeneratedJavascriptTemplateImpTest(repository);
//
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code1, 125L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code2, 138L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code3, 150L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code4, 161L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code5, 172L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code6, 184L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code7, 196L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code8, 209L), Language.NODEJS));
//        log.info(service.compile(js.compileCode(QuestionStrToJS.code9, 222L), Language.NODEJS));

//    }

//   @Test
//   @DisplayName("파이썬 컴파일 코드 문자열 확인 테스트")
//   void 파이썬코드템플릿() throws InterruptedException {
//        GeneratedPythonTemplateImpTest py = new GeneratedPythonTemplateImpTest(repository);
//        compile(py.compileCode(QuestionStrToPython.code1, 125L));
//        log.info(py.compileCode(QuestionStrToPython.code1, 125L));
//        log.info(py.compileCode(QuestionStrToPython.code2, 138L));
//        log.info(py.compileCode(QuestionStrToPython.code3, 150L));
//        log.info(py.compileCode(QuestionStrToPython.code4, 161L));
//        log.info(py.compileCode(QuestionStrToPython.code5, 172L));
//        log.info(py.compileCode(QuestionStrToPython.code6, 184L));
//        log.info(py.compileCode(QuestionStrToPython.code7, 196L));
//        log.info(py.compileCode(QuestionStrToPython.code8, 209L));
//        log.info(py.compileCode(QuestionStrToPython.code9, 222L));
//   }

//   @Test
//   @DisplayName("파이썬 컴파일 테스트")
//    void 파이썬() {

//
//          GeneratedPythonTemplateImpTest py = new GeneratedPythonTemplateImpTest(repository);
//         log.info(service.compile(pyStr.compileCode(QuestionStrToPython.code1, 125L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code2, 138L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code3, 150L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code4, 161L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code5, 172L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code6, 184L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code7, 196L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code8, 209L), Language.PYTHON3));
//        log.info(service.compile(py.compileCode(QuestionStrToPython.code9, 222L), Language.PYTHON3));

//   }

//    @Test
//    @DisplayName("오류 출력 테스트")
//    void completePer() {
//        String roomId1 = UUID.randomUUID().toString();
////        String roomId2 = UUID.randomUUID().toString();
////        String roomId3 = UUID.randomUUID().toString();
//        long questionId = 1L;
//        CompileRequestDto dto0 = new CompileRequestDto(roomId1, questionId, 0, QuestionsStr.code1);
////        CompileRequestDto dto1 = new CompileRequestDto(roomId2, questionId, 1, QuestionStrToJS.code1);
////        CompileRequestDto dto2 = new CompileRequestDto(roomId3, questionId, 2, QuestionStrToPython.code1);
//        User user = User.builder()
//                .userId("novem")
//                .avatarUrl("no_info")
//                .password("1234")
//                .build();
//
//        //자바
//        String s = compilerService.compileResult(dto0,user).toString();
//        log.info(s);
//자바스크립트
//        String s1 = compilerService.compileResult(dto1,user).toString();
//        log.info(s1);
//
//        String s2 = compilerService.compileResult(dto2,user).toString();
//        log.info(s2);
// }


//    public String compileResult(GeneratedTemplate template, Long questionId, Language lang, String codeStr) {
//
//        List<TestCase> testCases = repository.getTestCases(questionId);
//        JSONObject compile = service.compile(template.compileCode(codeStr, testCases,questionId), lang);
//        String output = compile.get("output").toString();
//
//        if (output.contains("error") || output.contains("ReferenceError") || output.contains("Traceback")) {
//            int len = output.length();
//            StringBuilder out = new StringBuilder(output);
//
//            out.deleteCharAt(0);
//            out.deleteCharAt(len - 2);
//            output = out.toString();
//
//            String[] split = output.split("\n");
//            StringBuilder errorMsg = new StringBuilder(50);
//            for (String s : split) {
//                s = s
//                        .replace(" ", "&#32;")
//                        .replace("/", "&#47;")
//                        .replace("^", "");
//
//                errorMsg.append(s.trim());
//                errorMsg.append("<br>");
//            }
//            return errorMsg.toString();
//        } else {
//            String[] ans = output.replace("\n", "").split("_");
//            int cnt = 0;
//            //테스트케이스의 개수로 퍼센트지 계산을 위한 변수
//            int divide = (100 / testCases.size());
//            for (int i = 0; i < ans.length; i++) {
//                String answer = testCases.get(i).getAnswer().replaceAll("\\p{Z}", "");
//                StringBuilder result = new StringBuilder(ans[i].replaceAll("\\p{Z}", ""));
//                if (0 < answer.indexOf("[") && !lang.equals(Language.JAVA)) {
//                    result.insert(0, "[");
//                    result.append("]");
//                }
//                System.out.println(answer + "==" + result);
//                if (answer.equals(result.toString())) {
//                    cnt += divide;
//                }
//            }
//            return "정확도: " + cnt + "%";
//        }
//    }

//}
