import Base.RestTestBase;
import api.rest.Formats;
import dto.SubCategoryDto;
import org.junit.Test;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;

/**
 * Created by thang on 14.11.2016.
 */
public class SubCategoryTestIT extends RestTestBase {

    @Test
    public void testCleanDB() {
        get().then()
                .statusCode(200)
                .body("list.size()", is(0));
    }

    @Test
    public void testCreateAndGetSubCategory() {
        String rootCategory = "Thangs life";
        String rootId = createCategory(rootCategory);

        //Create sub
        String subCategory = "Sub category";
        String id = createSubCategory(rootId, "Sub category");

        //Change path
        changePath("subcategories");

        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));

        //Check sub
        given().pathParam("id", id)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("rootId", is(rootId))
                .body("subCategory", is(subCategory));

        //Change back for deleting
        changePath("categories");
    }
/*
    @Test
    public void testDelete() {
        //Add and confirm
        String id = createCategory("Hello");
        String subId = createSubCategory(id, "YO");

        changePath("subcategories");
        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));

        //Delete
        delete("/" + subId).then().statusCode(204);

        //Confirm deletion
        get().then().statusCode(200).body("list.size()", is(0)).body("totalSize", is(0));

        //Change dir back to categories, confirm still one category, i deleted only sub
        changePath("categories");
        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));

    }

*/

    @Test
    public void testUpdate() throws Exception {

        String text = "someText";
        String id = createCategory(text);

        String subText = "My text";
        String subId = createSubCategory(id, subText);

        String updatedText = "new updated text";

        //Change path
        changePath("subcategories");

        //Change with put
        given().contentType(Formats.V1_JSON)
                .pathParam("id", subId)
                .body(new SubCategoryDto(subId, id, updatedText))
                .put("/{id}")
                .then()
                .statusCode(204);
        //See result
        given().pathParam("id", subId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(subId))
                .body("subCategory", is(updatedText));

        //Change path
        changePath("categories");
    }


    @Test
    public void testInvalidUpdateId() {
        String id = createCategory("Hello");
        String subId = createSubCategory(id, "Its me");
        String updatedText = "New text";

        changePath("subcategories");
        given().contentType(Formats.V1_JSON)
                .pathParam("id", id + 1)
                .body(new SubCategoryDto(subId, id, updatedText))
                .put("/{id}")
                .then()
                .statusCode(409); // instead of 400
        changePath("categories");
    }

    @Test
    public void testPatch(){
        String text = "someText";
        String id = createCategory(text);

        String subText = "Hello";
        String subId = createSubCategory(id, subText);

        changePath("subcategories");

        String updatedText = "new updated text";
        given().contentType(Formats.V1_JSON)
                .body(updatedText)
                .pathParam("id", subId)
                .patch("/{id}")
                .then()
                .statusCode(204);

        changePath("categories");
    }

    @Test
    public void testInvalidGetById() {
        get("/subcategories/3000").then().statusCode(404);
    }

    @Test
    public void testGetSubCategorysSubSub(){
        String text = "someText";
        String id = createCategory(text);

        String subText = "Hello";
        String subId = createSubCategory(id, subText);

        //Test nothing in here before create subsub
        changePath("subcategories");
        given().pathParam("id", subId)
                .get("/{id}/subsubcategories")
                .then()
                .statusCode(200)
                .body("size()", is(0));


        String subSubText = "Thang";
        String subSubId = createSubSubCategory(id, subId, subSubText);

        //Added subsub, check if its OK.
        changePath("subcategories");
        given().pathParam("id", subId)
                .get("/{id}/subsubcategories")
                .then()
                .statusCode(200)
                .body("size()", is(1));

        changePath("categories");
    }
}
