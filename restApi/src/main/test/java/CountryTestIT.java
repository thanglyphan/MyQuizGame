import org.junit.BeforeClass;
import org.junit.Test;
import web.HttpUtil;
import web.JBossUtil;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by thang on 30.10.2016.
 */
public class CountryTestIT {

    @BeforeClass
    public static void initClass(){
        JBossUtil.waitForJBoss(10);
    }
    @Test
    public void testFail(){
        fail();
    }

    @Test
    public void testWithRawTcp() throws Exception {

        /*
            Here we build a "raw" HTTP request to get the list of countries,
            and do it by using low level TCP connections.
            Note: this is only for didactic purposes
         */

        // "verb" (GET in this case), followed by " ", then the path to resource, " ", and finally the protocol
        String request = "GET /myrest/api/countries HTTP/1.1 \n";
        //headers are pairs <key>:<value>, where the key is case insensitive
        request += "Host:localhost \n";  //this is compulsory: a server running at an IP can serve different host names
        request += "Accept:application/json \n"; //we states that we want the resource in Json format
        request += "\n"; //empty line indicates the end of the header section

        String result = HttpUtil.executeHttpCommand("localhost", 8080, request);
        System.out.println(result);

        String headers = HttpUtil.getHeaderBlock(result);
        assertTrue(headers.contains("200 OK"));

        String contentType = HttpUtil.getHeaderValue("Content-Type", result);
        assertTrue(contentType.contains("application/json"));

        String body = HttpUtil.getBodyBlock(result);
        assertTrue(body.contains("Norway"));
        assertTrue(body.contains("Sweden"));
        assertTrue(body.contains("Germany"));
    }

}
