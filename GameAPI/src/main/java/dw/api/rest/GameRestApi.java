package dw.api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import dw.api.dto.GameDto;
import dw.backend.datalayer.Game;
import io.swagger.annotations.*;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Api(value = "/games" , description = "The game api, get and post information")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/games")
@Produces(MediaType.APPLICATION_JSON) // states that, when a method returns something, it is in Json
public interface GameRestApi {
    String ID_PARAM = "The numeric id of the games";

    //Method start
    @ApiOperation("Get all the available games")
    @GET
    List<GameDto> get(@QueryParam("n") String x);

    //Method start
    @ApiOperation("Create a new game")
    @POST
    @ApiResponse(code = 200, message = "POST a new game")
    Long createGame(@DefaultValue("5") @QueryParam("How many quizzes do you want to add?") String x, @QueryParam("Game name") String gameName,  @QueryParam("ID from either root, sub or subsub category") String secondParam) throws IOException;

    //Method start
    @ApiOperation("Get a specific game")
    @GET
    @Path("/{id}")
    GameDto getGameByID(@ApiParam(ID_PARAM) @PathParam("id") Long id);

    //Method start
    @ApiOperation("Answer a specific quiz in a specific game")
    @POST
    @Path("/{id}")
    Response answerGameByID(
            @ApiParam(ID_PARAM) @PathParam("id") Long id,
            @ApiParam("Put in the question ID, the question you want to answer") @QueryParam("qid") String qid,
            @ApiParam("Your answer to the question you put in the Q-ID field") @QueryParam("answer") String answer) throws IOException;

    //Method start
    @ApiOperation("Delete a category with the given id")
    @DELETE
    @Path("/{id}")
    void delete(@ApiParam(ID_PARAM) @PathParam("id") Long id);

}