import Base.RestTestBase;
import api.rest.Formats;
import dto.SubSubCategoryDto;
import org.junit.Test;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

/**
 * Created by thang on 29.11.2016.
 */
public class SubSubCategoryTestIT extends RestTestBase {

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

        //Change path
        changePath("subsubcategories");

        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));

        //Check sub sub
        given().pathParam("id", subsubId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(subsubId))
                .body("rootId", is(rootId))
                .body("subCategoriId", is(subId))
                .body("subSubCategory", is(subSubCategory));

        //Change back for deleting
        changePath("categories");
    }

    @Test
    public void testDelete() {
        //Add and confirm
        String id = createCategory("Hello");
        String subId = createSubCategory(id, "YO");
        String subSubId = createSubSubCategory(id, subId, "Yes");

        changePath("subsubcategories");
        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));

        //Delete
        delete("/" + subSubId).then().statusCode(204);

        //Confirm deletion
        get().then().statusCode(200).body("list.size()", is(0)).body("totalSize", is(0));

        //Change dir back to categories, confirm still one category, i deleted only sub
        changePath("categories");
        get().then().statusCode(200).body("list.size()", is(1)).body("totalSize", is(1));
    }

    @Test
    public void testUpdate() throws Exception {

        String text = "someText";
        String id = createCategory(text);

        String subText = "My text";
        String subId = createSubCategory(id, subText);

        String subSubText = "The text";
        String subSubId = createSubSubCategory(id, subId, subSubText);

        String updatedText = "new updated text";

        //Change path
        changePath("subsubcategories");

        //Change with put
        given().contentType(Formats.V1_JSON)
                .pathParam("id", subSubId)
                .body(new SubSubCategoryDto(subSubId, id, subId, updatedText))
                .put("/{id}")
                .then()
                .statusCode(204);

        //See result
        given().pathParam("id", subSubId)
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(subSubId))
                .body("subSubCategory", is(updatedText));

        //Change path
        changePath("categories");
    }


    @Test
    public void testInvalidUpdateId() {
        String id = createCategory("Hello");
        String subId = createSubCategory(id, "Its me");
        String subSubId = createSubSubCategory(id, subId, "YO");

        String updatedText = "New text";

        changePath("subsubcategories");
        given().contentType(Formats.V1_JSON)
                .pathParam("id", subSubId + 1)
                .body(new SubSubCategoryDto(subSubId, subId, id, updatedText))
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

        String subSubId = createSubSubCategory(id, subId, "YO");

        changePath("subsubcategories");

        String updatedText = "new updated text";

        given().contentType(Formats.V1_JSON)
                .body(updatedText)
                .pathParam("id", subSubId)
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
        String rootCategory = "Thangs life";
        String subCategory = "Sub";
        String subSubCategory = "SubSub";
        String quizName = "What is up?";
        String rootId = createCategory(rootCategory);
        String subId = createSubCategory(rootId, subCategory);
        String subSubId = createSubSubCategory(rootId, subId, subSubCategory);

        changePath("categories");
        String rootId2 = createCategory("No quiz");
        String subId2 = createSubCategory(rootId2, "No quiz");
        String subSubId2 = createSubSubCategory(rootId2, subId2, "No quiz");


        String quizId = createQuiz(subSubId, quizName);

        changePath("subsubcategories");

        //Confirm added stuff.
        get().then().statusCode(200).body("list.size()", is(2)).body("totalSize", is(2));

        //Check with quiz. I have only added one category with quiz, the other dont have. Check if size is 1.
        given().queryParam("withQuizzes", true)
                .get("/subsubWithQuiz")
                .then()
                .statusCode(200)
                .body("size()", is(1));

        //Without returns nothing, check if size is 0
        given().queryParam("withQuizzes", false)
                .get("/subsubWithQuiz")
                .then()
                .statusCode(200)
                .body("size()", is(0));

        changePath("categories");

    }
}
