package api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/categories" , description = "Handling of creating and retrieving categories")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON) // states that, when a method returns something, it is in Json
public interface CategoryRestApi {
    String ID_PARAM ="The numeric id of the categories";

    @ApiOperation("Get all the categories")
    @GET
    List<CategoryDto> get();

    @ApiOperation("Create a new category")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of newly created category")
    Long createCategory(@ApiParam("Categoryname") CategoryDto dto);

    @ApiOperation("Get a single category specified by id")
    @GET
    @Path("/id/{id}")
    CategoryDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Delete a category with the given id")
    @DELETE
    @Path("/id/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);
}