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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Api(value = "/subcategories" , description = "Handling of creating and retrieving sub categories")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/subcategories")
@Produces(MediaType.APPLICATION_JSON) // states that, when a method returns something, it is in Json
public interface SubCategoryRestApi {
    String ID_PARAM ="The numeric id of the sub categories";

    @ApiOperation("Get all the sub categories")
    @GET
    List<SubCategoryDto> get();

    @ApiOperation("Create a new sub category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of newly created sub category")
    Long createSubCategory(@ApiParam("SubCategoryName") SubCategoryDto dto);

    @ApiOperation("Get a single sub category specified by id")
    @GET
    @Path("/id/{id}")
    SubCategoryDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Delete a sub category with the given id")
    @DELETE
    @Path("/id/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("GET all subcategories with the given parent specified by id")
    @GET
    @Path("/parent/{id}")
    List<SubCategoryDto> getSubcategoriesByParentId(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("GET all subsubcategories of the subcategory specified by id")
    @GET
    @Path("/id/{id}/subsubcategories")
    List<SubSubCategoryDto> getSubSubcategoriesById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

}