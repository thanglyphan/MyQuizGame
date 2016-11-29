package Base; /**
 * Created by thang on 01.11.2016.
 */

import api.rest.Formats;
import com.google.gson.Gson;
import dto.*;
import dto.collection.ListDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import web.JBossUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class RestTestBase {
    @BeforeClass
    public static void initClass() {
        JBossUtil.waitForJBoss(10);

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/myrest/api/categories/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @Before
    @After
    public void clean() {
        int total = Integer.MAX_VALUE;

        while (total > 0) {
            ListDto<?> listDto = given()
                    .queryParam("limit", Integer.MAX_VALUE)
                    .get()
                    .then()
                    .statusCode(200)
                    .extract()
                    .as(ListDto.class);

            listDto.list.stream()
                    .map(n -> ((Map) n).get("id"))
                    .forEach(id ->
                            given().delete("/" + id)
                                    .then()
                                    .statusCode(204)
                    );

            total = listDto.totalSize - listDto.list.size();
        }

    }

    protected String createCategory(String rootCategory){
        CategoryDto dto = new CategoryDto(null, rootCategory);

        String rootId = given().contentType(Formats.V1_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        return rootId;
    }

    protected String createSubCategory(String categoryId, String subCategory){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/myrest/api/subcategories/";
        SubCategoryDto dto = new SubCategoryDto(null, categoryId, subCategory);

        String rootId = given().contentType(Formats.V1_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        return rootId;
    }

    protected String createSubSubCategory(String categoryId, String subCategoryId, String subSubCategory){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/myrest/api/subsubcategories/";
        SubSubCategoryDto dto = new SubSubCategoryDto(null, categoryId, subCategoryId, subSubCategory);

        String rootId = given().contentType(Formats.V1_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        return rootId;
    }

    protected String createQuiz(String subSubCategoryId, String quizName){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/myrest/api/quizzes/";
        QuizDto dto = new QuizDto(null, subSubCategoryId, quizName);

        String rootId = given().contentType(Formats.V1_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        return rootId;
    }

    protected String createQuestion(String quizId, String question, String... strings){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/myrest/api/qa/";
        QuestionDto dto = new QuestionDto(null, quizId, question, strings[0], strings[1], strings[2], strings[3]);

        String rootId = given().contentType(Formats.V1_JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        return rootId;
    }




    protected void changePath(String path){
        RestAssured.basePath = "/myrest/api/" + path + "/";
    }
}
