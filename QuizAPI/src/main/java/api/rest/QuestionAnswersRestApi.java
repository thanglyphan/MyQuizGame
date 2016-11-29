package api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import dto.QuestionDto;
import dto.QuizDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/qa" , description = "The quiz api, get and post information")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/qa")
@Produces(Formats.V1_JSON) // states that, when a method returns something, it is in Json
public interface QuestionAnswersRestApi {
    String ID_PARAM ="The numeric id of the categories";

    @ApiOperation("Create a new question!")
    @POST
    @Consumes(Formats.V1_JSON)
    @ApiResponse(code = 200, message = "The id of newly created question")
    Long createQuestion(@ApiParam("Create a question") QuestionDto dto);

    @ApiOperation("Get the answer to a question")
    @GET
    @Path("/{id}/{answerToQuestion}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    Response getAnswer(
            @ApiParam("Quiz ID here") @PathParam("id") Long id,
            @ApiParam("Type in your question(needs to exist in the quiz)")@PathParam("answerToQuestion") String question );

    @ApiOperation("Get the answer to a question")
    @GET
    @Path("/question/{id}")
    Response getQuestion(@PathParam("id") Long id);
}