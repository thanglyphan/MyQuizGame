import com.github.tomakehurst.wiremock.client.WireMock;
import config.GameApplication;
import config.GameConfiguration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import java.io.UnsupportedEncodingException;

import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by thang on 02.12.2016.
 */
public class GameTestIT extends GameMock{
    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class);

    @Test
    public void testPostAndCheckResults() throws Exception {
        String n = "1";
        String filter = "3";
        String quizId = "4";

        String jsonFirstResponse = randomQuizzesIDMock();
        String jsonSecondResponse = quizzesInfoMock();

        //Get the first url. The quiz ID.
        stubJsonResponseFirstUrl(jsonFirstResponse, n, filter);
        //Then get the second url. The info on the id. Bcuz quizId is only one number, then i take that id and request the real info.
        stubJsonResponseFollowerUrl(jsonSecondResponse, quizId);

        //The post, speaking with another API(quizAPI)
        given().accept(MediaType.APPLICATION_JSON)
                .queryParam("n", n)
                .queryParam("name", "A game")
                .queryParam("filter", filter)
                .post("/games")
                .then()
                .statusCode(200);

        //Check for get if thats ok.
        given().accept(MediaType.APPLICATION_JSON)
                .queryParam("n", 4)
                .get("/games")
                .then()
                .statusCode(200);

        //We only created one game, so ID is 1. We check for the get game ID 1 returns the game with ID 1.
        String id2 = "1";
        given().accept(MediaType.APPLICATION_JSON)
                .pathParam("id", id2)
                .get("/games/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id2));
    }
    @Test
    public void testAnsweringQuiz() throws Exception {
        String n = "1";
        String filter = "3";
        String quizId = "4";

        String id = "1";
        String qid = "5";
        String answer = "Not much";

        String jsonFirstResponse = randomQuizzesIDMock();
        String jsonSecondResponse = quizzesInfoMock();
        String jsonQuestionAnswerResponse = questionAnswerMock();

        //Get the first url. The quiz ID.
        stubJsonResponseFirstUrl(jsonFirstResponse, n, filter);
        //Then get the second url. The info on the id. Bcuz quizId is only one number, then i take that id and request the real info.
        stubJsonResponseFollowerUrl(jsonSecondResponse, quizId);

        //Then, answer the question.
        stubJsonResponseQuestionAnswer(jsonQuestionAnswerResponse, qid);
        stubJsonResponseQuestionAnswer2(answerMock(answer), quizId, "Hello all good?");

        //The post, speaking with another API(quizAPI)
        given().accept(MediaType.APPLICATION_JSON)
                .queryParam("n", n)
                .queryParam("name", "A game")
                .queryParam("filter", filter)
                .post("/games")
                .then()
                .statusCode(200);

        given().accept(MediaType.APPLICATION_JSON)
                .queryParam("id", id)
                .queryParam("qid", qid)
                .queryParam("answer", answer)
                .pathParam("id", id)
                .post("/games/{id}")
                .then()
                .body(equalTo("You answered question: Hello all good? with answer: Not much. That is correct!"))
                .statusCode(200);



    }

}
