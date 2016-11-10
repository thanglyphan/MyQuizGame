package api.rest;

/**
 * Created by thang on 31.10.2016.
 */
import dto.CategoryDto;
import dto.SubCategoryDto;
import dto.SubSubCategoryDto;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/categories" , description = "Handling of creating and retrieving categories")
@Path("/categories")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface CategoryRestApi {
    String ID_PARAM ="The numeric id of the categories";

    @ApiOperation("Get all the categories")
    @GET
    List<CategoryDto> get();

    @ApiOperation("Create a new category")
    @POST
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 200, message = "The id of newly created category")
    Long createCategory(@ApiParam("Categoryname") CategoryDto dto);

    @ApiOperation("Get a single category specified by id")
    @GET
    @Path("/id/{id}")
    @Produces(Formats.BASE_JSON)
    CategoryDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Update an existing category")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void update(
            @ApiParam(ID_PARAM) @PathParam("id") Long id,
            @ApiParam("The category that will replace the old one. Cannot change its id though.") CategoryDto dto);

    @ApiOperation("Modify the rootcategory")
    @Path("/id/{id}")
    @PATCH
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    void patch(@ApiParam("The unique id of the counter") @PathParam("id") Long id,
               @ApiParam("Change root category") String text);

    @ApiOperation("Delete a category with the given id")
    @DELETE
    @Path("/id/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("GET all categories that have at least one subcategory with at least one subsubcategory with at least one quiz.")
    @GET
    @Path("/withQuizzes")
    List<CategoryDto> getCategoriesWithQuiz();

    @ApiOperation("GET all subsubcategories with at least one quiz.")
    @GET
    @Path("/withQuizzes/subsubcategories")
    List<SubSubCategoryDto> getSubSubCategoriesWithQuiz();

    @ApiOperation("GET all subcategories of the category specified by id.")
    @GET
    @Path("/id/{id}/subcategories")
    List<SubCategoryDto> getSubCategoriesByParentId(@ApiParam(ID_PARAM) @PathParam("id") Long id);







/*
    //------------------------------------------------ DECREPATED ------------------------------------------------//
    //Method start
    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/id/{id}")
    @Produces(Formats.BASE_JSON)
    @Deprecated
    Response deprecatedGetById(@ApiParam(ID_PARAM) @PathParam("id") Long id);


    //------------------------------------------------------------------------------------------------------------//
    //Method start
    @ApiOperation("Update an existing category")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Deprecated
    Response deprecatedUpdate(@ApiParam(ID_PARAM) @PathParam("id") Long id,
                              @ApiParam("The category that will replace the old one. Cannot change its id though.") CategoryDto dto);


    //------------------------------------------------------------------------------------------------------------//
    //Method start
    @ApiOperation("Modify the rootcategory")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @PATCH
    @Path("/id/{id}")
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @Deprecated
    Response deprecatedPatch(@ApiParam("The unique id of the counter") @PathParam("id") Long id,
                             @ApiParam("Change root category") String text);


    //------------------------------------------------------------------------------------------------------------//
    //Method start
    @ApiOperation("Delete a category with the given id")
    @ApiResponses({
            @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    })
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedDelete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    */
}