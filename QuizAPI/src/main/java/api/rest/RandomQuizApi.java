package api.rest;

/**
 * Created by thang on 31.10.2016.
 */

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


@Api(value = "/randomquiz" , description = "Handling of creating and retrieving sub sub categories")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/randomquiz")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface RandomQuizApi {

    @ApiOperation("Random quiz")
    @ApiResponse(code = 307, message = "Temp redirect to /quizzes{id}")
    @Path("randomQuiz")
    @GET
    Response getRandomQuiz(@QueryParam("filter") String filter);

    @ApiOperation("Random quiz")
    @ApiResponse(code = 200, message = "Temp redirect to /quizzes{id}")
    @POST
    @Path("randomQuizzes")
    @Produces({Formats.BASE_JSON, Formats.V1_JSON})
    List<String> getRandomQuizzes(@DefaultValue("5") @QueryParam("n") String y, @QueryParam("filter") String x);
}