package api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import dto.CategoryDto;
import dto.QuizDto;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/quizzes" , description = "The quiz api, get and post information")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/quizzes")
@Produces(MediaType.APPLICATION_JSON) // states that, when a method returns something, it is in Json
public interface QuizRestApi {
    String ID_PARAM ="The numeric id of the categories";

    @ApiOperation("Get all the available quizzes")
    @GET
    List<QuizDto> get();

    @ApiOperation("Create a new quiz")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiResponse(code = 200, message = "The id of newly created category")
    Long createQuiz(@ApiParam("Create a quiz") QuizDto dto);


    @ApiOperation("Get a single quiz specified by id")
    @GET
    @Path("/id/{id}")
    QuizDto getById(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    @ApiOperation("Delete a quiz with the given id")
    @DELETE
    @Path("/id/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);
}