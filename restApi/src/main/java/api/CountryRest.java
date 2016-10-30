package api;

/**
 * Created by thang on 30.10.2016.
 */

import essentials.CountryList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/countries", description = "API for country data.")
@Path("/countries")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class CountryRest {

    @ApiOperation("Retrieve list of country names")
    @GET
    public List<String> get(){
        return CountryList.getCountries();
    }

}