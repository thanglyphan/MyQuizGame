import Base.RestTestBase;
import api.rest.Formats;
import dto.QuizDto;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

/**
 * Created by thang on 29.11.2016.
 */
public class QuizTestIT extends RestTestBase {

    @Test
    public void testCleanDB() {
        get().then()
                .statusCode(200)
                .body("list.size()", is(0));
    }

    @Test
    public void testCreateAndGetSubSubCategory() {
        String rootCategory = "Thangs life";
        String rootId = createCategory(rootCategory);

        //Create sub
        String subCategory = "Sub category";
        String subId = createSubCategory(rootId, subCategory);

        //Create subsub
        String subSubCategory = "Sub Sub category";
        String subsubId = createSubSubCategory(rootId, subId, subSubCategory);

        String quizName = "The quiz";
        String quizId = createQuiz(subsubId, quizName);
        //Change path
        changePath("quizzes");

        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));

        //Check quiz
        given().pathParam("id", quizId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(quizId))
                .body("quizName", is(quizName));

        //Change back for deleting
        changePath("categories");
    }

    @Test
    public void testDelete() {
        //Add and confirm
        String id = createCategory("Hello");
        String subId = createSubCategory(id, "YO");
        String subSubId = createSubSubCategory(id, subId, "Yes");
        String quizId = createQuiz(subSubId, "quiz");


        changePath("quizzes");
        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));

        //Delete
        delete("/" + quizId).then().statusCode(204);

        //Confirm deletion
        get().then().statusCode(200).body("list.size()", is(0)).body("totalSize", is(0));

        //Change dir back to categories, confirm still one subsub, i deleted only quiz
        changePath("subsubcategories");
        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));
        changePath("categories");

    }


    @Test
    public void testUpdate() throws Exception {
        String text = "someText";
        String id = createCategory(text);

        String subText = "My text";
        String subId = createSubCategory(id, subText);

        String subSubText = "The text";
        String subSubId = createSubSubCategory(id, subId, subSubText);

        String quizId = createQuiz(subSubId, "quiz");

        String updatedText = "new updated text";

        //Change path
        changePath("quizzes");

        //Change with put
        given().contentType(Formats.V1_JSON)
                .pathParam("id", quizId)
                .body(new QuizDto(quizId, subSubId, updatedText))
                .put("/{id}")
                .then()
                .statusCode(204);

        //See result
        given().pathParam("id", quizId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(quizId))
                .body("quizName", is(updatedText));

        //Change path
        changePath("categories");
    }


    @Test
    public void testInvalidUpdateId() {
        String id = createCategory("Hello");
        String subId = createSubCategory(id, "Its me");
        String subSubId = createSubSubCategory(id, subId, "YO");
        String quizId = createQuiz(subSubId, "Quiz");

        String updatedText = "New text";

        changePath("quizzes");
        given().contentType(Formats.V1_JSON)
                .pathParam("id", quizId + 1)
                .body(new QuizDto(quizId, subSubId, updatedText))
                .put("/{id}")
                .then()
                .statusCode(409); // instead of 400
        changePath("categories");
    }


    @Test
    public void testPatch() {
        String text = "someText";
        String id = createCategory(text);

        String subText = "Hello";
        String subId = createSubCategory(id, subText);

        String subSubId = createSubSubCategory(id, subId, "YO");

        String quizId = createQuiz(subSubId, "quiz");


        changePath("quizzes");

        String updatedText = "new updated text";

        given().contentType(Formats.V1_JSON)
                .body(updatedText)
                .pathParam("id", quizId)
                .patch("/{id}")
                .then()
                .statusCode(204);

        changePath("categories");
    }

    @Test
    public void testAddQuestionAndAnswerToQuiz() {
        String id = createCategory("Hello");
        String subId = createSubCategory(id, "Its me");
        String subSubId = createSubSubCategory(id, subId, "YO");
        String quizId = createQuiz(subSubId, "Quiz");
        String question = "Hello all good?";
        String qId = createQuestion(quizId, question, "Not much", "Very much", "Little bit", "Dont know");
        changePath("qa");

        //Lets check if the question is there
        given().contentType(MediaType.APPLICATION_JSON)
                .pathParam("id", qId)
                .get("/question/{id}")
                .then()
                .statusCode(200)
                .body("size()", is(1));

        //Lets check the answer to my question
        given().contentType(MediaType.TEXT_PLAIN)
                .pathParam("id", quizId)
                .pathParam("answerToQuestion", question)
                .get("/{id}/{answerToQuestion}")
                .then()
                .statusCode(200)
                .body(is("Not much"));

        changePath("categories");
    }
}
