package Base; /**
 * Created by thang on 01.11.2016.
 */

import api.rest.Formats;
import com.google.gson.Gson;
import dto.CategoryDto;
import dto.SubCategoryDto;
import dto.SubSubCategoryDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import web.JBossUtil;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class CategoryRestTestBase {
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
        List<CategoryDto> list = Arrays.asList(given().accept(ContentType.JSON).get()
                .then()
                .statusCode(200)
                .extract().as(CategoryDto[].class));

        list.stream().forEach(dto ->
                given().pathParam("id", dto.id).delete("/{id}").then().statusCode(204));

        get().then().statusCode(200).body("size()", is(0));
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
        RestAssured.basePath = "/myrest/api/categories/";

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
        RestAssured.basePath = "/myrest/api/categories/";

        return rootId;
    }


    protected void changePath(String path){
        RestAssured.basePath = "/myrest/api/" + path + "/";
    }
}
