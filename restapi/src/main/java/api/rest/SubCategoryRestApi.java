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


@Api(value = "/subcategories", description = "Handling of creating and retrieving sub categories")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/subcategories")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface SubCategoryRestApi {
    String ID_PARAM = "The numeric id of the sub categories";

    @ApiOperation("Get all the sub categories")
    @GET
    List<SubCategoryDto> get();

    @ApiOperation("Create a new sub category")
    @POST
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 200, message = "The id of newly created sub category")
    Long createSubCategory(@ApiParam("SubCategoryName") SubCategoryDto dto);


    @ApiOperation("Get a single sub category specified by id")
    @GET
    @Path("/{id}")
    SubCategoryDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Delete a sub category with the given id")
    @DELETE
    @Path("/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Update an existing sub category")
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void update(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,
            //
            @ApiParam("The sub category that will replace the old one. Cannot change its id though.")
                    SubCategoryDto dto);

    @ApiOperation("Modify the rootcategory")
    @Path("/{id}")
    @PATCH
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    void patch(@ApiParam("The unique id of the counter")
               @PathParam("id")
                       Long id,
               //
               @ApiParam("Change sub category")
                       String text);

    /*
        @ApiOperation("GET all subcategories with the given parent specified by id")
        @GET
        @Path("/categories/{id}/subcategories")
        List<SubCategoryDto> getSubcategoriesByParentId(@ApiParam(ID_PARAM) @PathParam("id") Long id);
    */
    @ApiOperation("GET all subsubcategories of the subcategory specified by id")
    @GET
    @Path("/{id}/subsubcategories")
    List<SubSubCategoryDto> getSubSubcategoriesById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

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
            @ApiParam("The sub category that will replace the old one. Cannot change its id though.")
                    SubCategoryDto dto);

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
                     @ApiParam("Change sub category")
                             String text);

    @ApiOperation("Deprecated. Use \"categories/{id}/subcategories\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/parent/{id}")
    @Deprecated
    Response deprecatedGetSubCategoriesByParentId(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Deprecated. Use \"{id}/subsubcategories\" instead")
    @ApiResponse(code = 301, message = "Deprecated URI. Moved permanently.")
    @GET
    @Path("/id/{id}/subsubcategories")
    @Deprecated
    Response deprecatedGetSubSubCategoriesBySubId(@ApiParam(ID_PARAM) @PathParam("id") Long id);
}