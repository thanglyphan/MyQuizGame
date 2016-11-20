package api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import dto.CategoryDto;
import dto.QuizDto;
import dto.SubSubCategoryDto;
import io.swagger.annotations.*;
import io.swagger.jaxrs.PATCH;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/quizzes" , description = "The quiz api, get and post information")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/quizzes")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface QuizRestApi {
    String ID_PARAM ="The numeric id of the categories";

    @ApiOperation("Get all the available quizzes")
    @GET
    List<QuizDto> get();

    @ApiOperation("Create a new quiz")
    @POST
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @ApiResponse(code = 200, message = "The id of newly created category")
    Long createQuiz(@ApiParam("Create a quiz") QuizDto dto);


    @ApiOperation("Get a single quiz specified by id")
    @GET
    @Path("/{id}")
    QuizDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Delete a quiz with the given id")
    @DELETE
    @Path("/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);


    @ApiOperation("Update an existing quiz")
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void update(
            @ApiParam(ID_PARAM)
            @PathParam("id")
                    Long id,
            //
            @ApiParam("The sub sub category that will replace the old one. Cannot change its id though.")
                    QuizDto dto);

    @ApiOperation("Modify the quiz")
    @Path("/{id}")
    @PATCH
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    void patch(@ApiParam("The unique id of the quiz")
               @PathParam("id")
                       Long id,
               //
               @ApiParam("Change quiz name")
                       String text);

    //------------------------------------------------ DECREPATED ------------------------------------------------//
    @ApiOperation("Deprecated. Use \"id\" instead")
    @GET
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedGetById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Deprecated. Use \"id\" instead")
    @DELETE
    @Path("/id/{id}")
    @Deprecated
    Response deprecatedDelete(@ApiParam(ID_PARAM) @PathParam("id") Long id);


    @ApiOperation("Deprecated. Use \"id\" instead")
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
                    QuizDto dto);

    @ApiOperation("Deprecated. Use \"id\" instead")
    @Path("/id/{id}")
    @PATCH
    @Consumes({Formats.V1_JSON, Formats.BASE_JSON})
    @Produces(Formats.BASE_JSON)
    @Deprecated
    Response deprecatedPatch(@ApiParam("The unique id of the quiz")
               @PathParam("id")
                       Long id,
               //
               @ApiParam("Change quiz name")
                       String text);

}