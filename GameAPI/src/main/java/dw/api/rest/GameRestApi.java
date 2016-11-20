package dw.api.rest;

/**
 * Created by thang on 31.10.2016.
 */

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;


import javax.ws.rs.*;
import java.util.List;

@Api(value = "/games" , description = "The game api, get and post information")
// when the url is "<base>/news", then this class will be used to handle it
@Path("/games")
@Produces({
        Formats.BASE_JSON,
        Formats.V1_JSON
})
public interface GameRestApi {
    String ID_PARAM ="The numeric id of the categories";

    //Method start
    @ApiOperation("Get all the available games")
    @GET
    List<String> get(@QueryParam("n") String x);

    //Method start
    @ApiOperation("Create a new game")
    @POST
    @ApiResponse(code = 200, message = "POST a new game")
    Long createGame(@QueryParam("n") String x);

}