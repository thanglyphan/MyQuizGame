package api.implementation;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Created by thang on 11.11.2016.
 */
public class Responder {
    public static Response response(String path, int statusCode, Long id, String adds){
        return Response.status(statusCode)
                .location(UriBuilder.fromUri(path + id + adds).build())
                .build();
    }
}
