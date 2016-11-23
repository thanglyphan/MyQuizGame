package api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import dto.CategoryDto;
import dto.SubCategoryDto;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/categories", description = "Handling of creating and retrieving categories")
@Path("/categories")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface CategoryRestApi {
    String ID_PARAM = "The numeric id of the categories";

    //Method start
    @ApiOperation("Get all the categories")
    @GET
    List<CategoryDto> get();

    //Method start
    @ApiOperation("Create a new category")
    @POST
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 200, message = "The id of newly created category")
    Long createCategory(@ApiParam("Categoryname") CategoryDto dto);


    //Method start
    @ApiOperation("GET all categories that have at least one subcategory with at least one subsubcategory with at least one quiz.")
    @Path("categoryWithQuiz")
    @GET
    List<CategoryDto> getCategoriesWithQuiz(@QueryParam("withQuizzes") boolean withQuizzes);

    //Method start
    @ApiOperation("Get a single category specified by id")
    @GET
    @Path("/{id}")
    CategoryDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);


    //Method start
    @ApiOperation("Update an existing category")
    @PUT
    @Path("/{id}")
    @Consumes(Formats.V1_JSON)
    void update(@ApiParam(ID_PARAM) @PathParam("id") Long id,
                @ApiParam("The category that will replace the old one. Cannot change its id though.") CategoryDto dto);

    //Method start
    @ApiOperation("Modify the rootcategory")
    @PATCH
    @Path("/{id}")
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    void patch(@ApiParam("The unique id of the counter") @PathParam("id") Long id,
               @ApiParam("Change root category") String text);

    //Method start
    @ApiOperation("Delete a category with the given id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @DELETE
    @Path("/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    //Method start
    @ApiOperation("GET all subcategories of the category specified by id.")
    @GET
    @Path("/{id}/subcategories")
    @Produces(Formats.V1_JSON)
    List<SubCategoryDto> getSubCategoriesByParentId(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    //------------------------------------------------ DECREPATED ------------------------------------------------//

    //Method start
    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    //Method start
    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @PUT
    @Path("/id/{id}")
    @Consumes(Formats.V1_JSON)
    @Deprecated
    Response deprecatedUpdate(
            @ApiParam(ID_PARAM) @PathParam("id") Long id,
            @ApiParam("The category that will replace the old one. Cannot change its id though.") CategoryDto dto);

    //Method start
    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @PATCH
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @Deprecated
    Response deprecatedPatch(@ApiParam("The unique id of the counter") @PathParam("id") Long id,
                             @ApiParam("Change root category") String text);

    //Method start
    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @DELETE
    @Deprecated
    Response deprecatedDelete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    //Method start
    @ApiOperation("Deprecated. Use \"{id}/subcategories\" instead.")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/id/{id}/subcategories")
    @Produces(Formats.V1_JSON)
    @Deprecated
    Response deprecatedGetSubCategoriesByParentId(@ApiParam(ID_PARAM) @PathParam("id") Long id);


    //Method start
    @ApiOperation("Deprecated. Use \"?withQuizzes\" instead.")
    @ApiResponse(code = 307, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/withQuizzes")
    @Deprecated
    Response deprecatedGetCategoriesWithQuiz();

    //Method start
    @ApiOperation("GET all subsubcategories with at least one quiz.")
    @ApiResponse(code = 307, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/withQuizzes/subsubcategories")
    @Deprecated
    Response deprecatedGetSubSubCategoriesWithQuiz();


}