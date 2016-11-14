import api.rest.Formats;
import com.fasterxml.jackson.databind.ObjectMapper;
import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import dto.CategoryDto;
import dto.SubCategoryDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import web.JBossUtil;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
/**
 * Created by thang on 30.10.2016.
 */
public class CategoryTestIT extends CategoryRestTestBase{

    @Test
    public void testCleanDB() {

        get().then()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testCreateAndGetCategory() {
        String rootCategory = "Thangs life";
        String rootId = createCategory(rootCategory);

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", rootId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(rootId))
                .body("rootCategory", is(rootCategory));
    }
    @Test
    public void testDelete() {
        String id = createCategory("Hello");
        get().then().body("id", contains(id));
        delete("/id/" + id).then().statusCode(301); //instead of 204
    }

    @Test
    public void testUpdate() throws Exception {

        String text = "someText";
        String id = createCategory(text);

        get("/id/" + id).then().body("rootCategory", is(text));

        String updatedText = "new updated text";

        //Change with put
        given().contentType(Formats.V1_JSON)
                .pathParam("id", id)
                .body(new CategoryDto(id, updatedText))
                .put("/{id}")
                .then()
                .statusCode(204); // instead of 204
        //See result
        given().pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("rootCategory", is(updatedText));
    }

    @Test
    public void testPatch(){
        String text = "someText";
        String id = createCategory(text);


        String updatedText = "new updated text";
        given().contentType(Formats.V1_JSON)
                .body(updatedText)
                .pathParam("id", id)
                .patch("/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetSubcategories(){
        String rootCategory = "Hello, Thang";
        String rootId = createCategory(rootCategory);

        given().pathParam("id", rootId)
                .get("/{id}/subcategories")
                .then()
                .statusCode(200)
                .body("size()", is(0));

        //Now lets create the subcategory
        String subCategory = "Hello, its me";
        createSubCategory(rootId, subCategory);

        //Check if body is 1 and rootCategory is here
        given().pathParam("id", rootId)
                .get("/{id}/subcategories")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }
    @Test
    public void testInvalidGetById() {
        get("/categories/3000").then().statusCode(404);
    }

    @Test
    public void testInvalidUpdate() {
        String id = createCategory("Hello");
        String updatedText = "";

        given().contentType(Formats.V1_JSON)
                .pathParam("id", id)
                .body(new CategoryDto(id, updatedText))
                .put("/{id}")
                .then()
                .statusCode(400); // instead of 400
    }
}
