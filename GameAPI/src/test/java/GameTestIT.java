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

    @ClassRule
    public static final DropwizardAppRule<GameConfiguration> RULE =
            new DropwizardAppRule<>(GameApplication.class);


    private static WireMockServer wiremockServer;

    @BeforeClass
    public static void initClass() {

        System.setProperty("foo", "localhost:8099");

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/mygames/api";
//        RestAssured.basePath = "/index.html/games/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        /*
            java -Ddw.server.applicationConnectors[0].port=9090 -Ddw.server.adminConnectors[0].port=9091 -jar target/GameAPI-0.0.1-SNAPSHOT.jar server
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

    @Test
    public void tester() throws Exception {

        given().queryParam("n", "5")
                .get("/games")
                .then()
                .statusCode(200);
    }



}
