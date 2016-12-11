import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import web.JBossUtil2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Created by thang on 10.12.2016.
 */
public class GameMock {
    protected static WireMockServer wiremockServer;

    @BeforeClass
    public static void initClass() {
        //JBossUtil2.waitForJBoss(10);

        System.setProperty("quizWebAddress", "localhost:8099");

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/mygames/api";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        wiremockServer = new WireMockServer(
                wireMockConfig().port(8099).notifier(new ConsoleNotifier(true))
        );
        wiremockServer.start();
    }

    @AfterClass
    public static void tearDown() {
        wiremockServer.stop();
    }

    protected String randomQuizzesIDMock() {
        String json = "[\n" +
                "\"4\"\n" +
                "]";
        return json;
    }


    protected String quizzesInfoMock() {
        String json = "{\n" +
            "  \"id\": \"4\",\n" +
                    "  \"subSubCategoryId\": \"3\",\n" +
                    "  \"rootCategory\": \"Hello\",\n" +
                    "  \"subCategory\": \"Its me\",\n" +
                    "  \"subsubCategory\": \"YO\",\n" +
                    "  \"quizName\": \"Quiz\",\n" +
                    "  \"questionsAndAnswersList\": {\n" +
                    "    \"5; Hello all good?\": [\n" +
                    "      \"Not much\",\n" +
                    "      \"Very much\",\n" +
                    "      \"Little bit\",\n" +
                    "      \"Dont know\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

        return json;
    }

    protected String questionAnswerMock(){
        String json = "{\n" +
                "\"Hello all good?\":[\n" +
                "\"Not much\",\n" +
                "\"Very much\",\n" +
                "\"Little bit\",\n" +
                "\"Dont know\"\n" +
                "]\n" +
                "}";
        return json;
    }

    protected String answerMock(String a){
        return a;
    }
    protected void stubJsonResponseFirstUrl(String json, String n, String filter) throws Exception {
        wiremockServer.stubFor(WireMock.post(
                        urlMatching("/myrest/api/randomquiz/randomQuizzes.*"))
                        .withQueryParam("n", WireMock.matching(n))
                        .withQueryParam("filter", WireMock.matching(filter))
                        .willReturn(WireMock.aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8;")
                                .withHeader("Content-Length", "" + json.getBytes("utf-8").length)
                                .withBody(json)));


    }

    protected void stubJsonResponseFollowerUrl(String jsonSecondResponse, String id) throws Exception {
        wiremockServer.stubFor(WireMock.get(
                urlMatching("/myrest/api/quizzes/" + id))
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8;")
                        .withHeader("Content-Length", "" + jsonSecondResponse.getBytes("utf-8").length)
                        .withBody(jsonSecondResponse)));
    }

    protected void stubJsonResponseQuestionAnswer(String jsonQuestionResponse, String id) throws Exception {
        wiremockServer.stubFor(WireMock.get(
                urlMatching("/myrest/api/qa/question/" + id))
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8;")
                        .withHeader("Content-Length", "" + jsonQuestionResponse.getBytes("utf-8").length)
                        .withBody(jsonQuestionResponse)));
    }

    protected void stubJsonResponseQuestionAnswer2(String jsonQuestionResponse, String quizId, String question) throws Exception {
        String q = URLEncoder.encode(question, "UTF-8").replace("+", "%20");
        wiremockServer.stubFor(WireMock.get(
                urlMatching("/myrest/api/qa/" + quizId + "/" + q))
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=utf-8;")
                        .withHeader("Content-Length", "" + jsonQuestionResponse.getBytes("utf-8").length)
                        .withBody(jsonQuestionResponse)));
    }
}
