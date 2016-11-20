package api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import dto.CategoryDto;
import dto.SubCategoryDto;
import dto.SubSubCategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Api(value = "/subsubcategories" , description = "Handling of creating and retrieving sub sub categories")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/subsubcategories")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface SubSubCategoryRestApi {
    String ID_PARAM ="The numeric id of the sub subcategories";

    @ApiOperation("Get all the sub sub categories")
    @GET
    List<SubSubCategoryDto> get();

    @ApiOperation("Get all the sub sub categories")
    @GET
    @Path("subsubWithQuiz")
    List<SubSubCategoryDto> getWithQuizzes(@QueryParam("withQuizzes") boolean withQuizzes);

    @ApiOperation("Create a new sub sub category")
    @POST
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 200, message = "The id of newly created sub sub category")
    Long createSubSubCategory(@ApiParam("SubSubCategoryName") SubSubCategoryDto dto);


    @ApiOperation("Get a single sub sub category specified by id")
    @GET
    @Path("/{id}")
    SubSubCategoryDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Delete a sub sub category with the given id")
    @DELETE
    @Path("/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Update an existing sub sub category")
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void update(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,
            //
            @ApiParam("The sub sub category that will replace the old one. Cannot change its id though.")
                    SubSubCategoryDto dto);

    @ApiOperation("Modify the sub sub category")
    @Path("/{id}")
    @PATCH
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    void patch(@ApiParam("The unique id of the counter")
               @PathParam("id")
                       Long id,
               //
               @ApiParam("Change sub sub category")
                       String text);
/*
    @ApiOperation("GET all subsubcategories with the given subcategory parent specified by id")
    @GET
    @Path("/parent/{id}")
    List<SubSubCategoryDto> getSubSubCategoriesBySubCategoryId(@ApiParam(ID_PARAM) @PathParam("id") Long id);
*/
    //------------------------------------------------ DECREPATED ------------------------------------------------//
    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedDelete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @PUT
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Deprecated
    Response deprecatedUpdate(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,
            //
            @ApiParam("The sub sub category that will replace the old one. Cannot change its id though.")
                    SubSubCategoryDto dto);

    @ApiOperation("Deprecated. Use \"id\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @Path("/id/{id}")
    @PATCH
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @Deprecated
    Response deprecatedPatch(@ApiParam("The unique id of the counter")
               @PathParam("id")
                       Long id,
               //
               @ApiParam("Change sub sub category")
                       String text);

    @ApiOperation("Deprecated. Use \"subcategories/{id}/subsubcategories\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response deprecatedGetSubSubCategoriesBySubCategoryId(@ApiParam(ID_PARAM) @PathParam("id") Long id);
}