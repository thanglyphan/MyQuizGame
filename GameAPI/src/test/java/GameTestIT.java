import com.fasterxml.jackson.dataformat.yaml.snakeyaml.scanner.Scanner;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import config.GameApplication;
import config.GameConfiguration;
import dw.api.dto.GameConverter;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import static org.hamcrest.core.IsEqual.equalTo;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

/**
 * Created by thang on 02.12.2016.
 */
public class GameTestIT {
    private static WireMockServer wiremockServer;

    @BeforeClass
    public static void initClass() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/index.html/games/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        /*
            WireMock will run as a process binding on port 8099 (in this case).
            We configure the application by, during testing, redirecting all
            calls to external services to WireMock instead.
            In WireMock, we need to define mocked answers.
         */
        wiremockServer = new WireMockServer(
                wireMockConfig().port(8099).notifier(new ConsoleNotifier(true))
        );
        wiremockServer.start();
    }

    @AfterClass
    public static void tearDown() {
        wiremockServer.stop();
    }
    /*
    @Test
    public void tester() throws Exception {

        given().queryParam("n", "5")
                .get()
                .then()
                .statusCode(200);
    }
    */


}
