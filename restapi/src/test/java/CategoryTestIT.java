import com.fasterxml.jackson.databind.ObjectMapper;
import datalayer.categories.Category;
import dto.CategoryDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import web.JBossUtil;

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
    public void testCreateAndGet() {

        String rootCategory = "Thangs life";
        CategoryDto dto = new CategoryDto(null, rootCategory);

        get().then().statusCode(200).body("size()", is(0));

        String id = given().contentType(ContentType.JSON)
                .body(dto)
                .post()
                .then()
                .statusCode(200)
                .extract().asString();

        get().then().statusCode(200).body("size()", is(1));

        given().pathParam("id", id)
                .get("/id/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("rootCategory", is(rootCategory));
    }
}
