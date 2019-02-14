package framework;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;


@Epic(value = "WireMockTestDemo")
@Feature("WireMockForJunit5")
@Owner("hongkongqa")
public class TestWiremock {

    static WireMockServer wiremockServer = new WireMockServer(9088,9089);

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        wiremockServer.start();
        RestAssured.baseURI = "http://localhost:9088";
    }

    @AfterEach
    public void teardown(TestInfo testinfo) {
        System.out.println("---------"+testinfo.getDisplayName()+" finished!---------");

    }

    @Test
    @DisplayName("Basic listening rule settings")
    @Story("Basic function test")
    @Description("This is wiremock demo !")
    @Severity(SeverityLevel.CRITICAL)
    @Link(name = "288.html", type = "hongkongqa")
    void test001() {
        test001_step1();
        test001_step2();
    }

    @Step("Define wiremock response")
    void test001_step1() {
        wiremockServer.stubFor(get(urlEqualTo("/Rest/Mock")).
                willReturn(aResponse().withStatus(200).withHeader("user", "hongkongqa").withBody("basic test")));
    }

    @Step("Verify response status")
    void test001_step2() {
        given().when().get("/Rest/Mock").then().statusCode(200).log().all();
    }

    @Test
    @DisplayName("Response rule simplification settings")
    @Story("Basic function test")
    @Severity(SeverityLevel.MINOR)
    void test002() {
        wiremockServer.stubFor(get(urlEqualTo("/Rest/simple"))
                .willReturn(ok("This is a simplified way of writing")));

        given().when().get("/Rest/simple").then().log().all();
    }

    @Test
    @DisplayName("Redirect")
    @Story("Basic function test")
    @Severity(SeverityLevel.NORMAL)
    void test003() {
        wiremockServer.stubFor(post(urlEqualTo("/Rest/redirect"))
                .willReturn(temporaryRedirect("/Test/newplace"))
        );

        given().when().post("/Rest/redirect").then().log().all();
    }

    @Test
    @DisplayName("ServerError")
    @Story("Advanced function test")
    @Severity(SeverityLevel.BLOCKER)
    void test004() {
        wiremockServer.stubFor(post(urlEqualTo("/Rest/ServerError")).willReturn(serverError()));

        given().when().post("/Rest/ServerError").then().log().all();
    }

    @Test
    @DisplayName("Application of priority rules")
    @Story("Advanced function test")
    @Severity(SeverityLevel.CRITICAL)
    @Issues({@Issue("bug004"),@Issue("bug005")})
    void test005() {
        test005_step1();
        test005_step2();
        test005_step3();
        test005_step4();
    }

    @Step("Match all request settings")
    void test005_step1() {

        wiremockServer.stubFor(any(anyUrl()).atPriority(10).willReturn(notFound()));
    }

    @Step("Matches match path request settings")
    void test005_step2() {
        wiremockServer.stubFor(get(urlMatching("/Rest/.*")).atPriority(5).willReturn(aResponse().withStatus(402).withBody("No access !"))
        );
        //wiremockServer.stopRecording().hashCode();
    }

    @Step("Match exact path settings")
    void test005_step3() {
        wiremockServer.stubFor(get(urlEqualTo("/Rest/Test")).atPriority(1).willReturn(ok("Test address"))
        );
    }

    @Step("Verify priority matching")
    void test005_step4() {
        given().when().get("/test/none").then().log().status();

        given().when().get("/Rest/none").then().log().body();

        given().when().get("/Rest/Test").then().log().body();
    }

    @Test
    @DisplayName("How to use the scene")
    @Story("Advanced function test")
    @Severity(SeverityLevel.NORMAL)
    public void test006() {
        wiremockServer.stubFor(get(urlEqualTo("/Rest/user")).willReturn(ok("{\"user\":\"hongkongqa\"}")));

        wiremockServer.stubFor(delete(urlEqualTo("/Rest/user")).inScenario("get user").whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse().withStatus(204))
                .willSetStateTo("deleted")
        );

        wiremockServer.stubFor(get(urlEqualTo("/Rest/user")).inScenario("get user")
                .whenScenarioStateIs("deleted")
                .willReturn(notFound())
        );

        given().when().get("/Rest/user").then().log().all();

        given().when().delete("/Rest/user").then().log().all();

        given().when().get("/Rest/user").then().log().all();

    }

    // HTTPS
    @Test
    @DisplayName("Test HTTPS")
    @Story("Advanced function test")
    @Severity(SeverityLevel.CRITICAL)
    @Link(name = "77.html", type = "hongkongqa")
    public void test007() {
        RestAssured.baseURI = "https://localhost:9089";
        RestAssured.useRelaxedHTTPSValidation();

        wiremockServer.startRecording("https://api.github.com");

        given().when().get("/users/rest-assured").then().statusCode(200).log().all();

        //wiremockServer.stopRecording();

        given().when().get("/users/rest-assured").then().statusCode(200).log().all();

    }

    // Get json.file
    @Test
    @DisplayName("Get response content from file")
    @Story("Basic function test")
    @Severity(SeverityLevel.MINOR)
    public void test008() {
        wiremockServer.stubFor(get(urlEqualTo("/Rest/file")).willReturn(aResponse().withStatus(200)
                .withBodyFile("filebody.json")));

        given().when().get("/Rest/file").then().log().all();
    }
    
    @Test
    @DisplayName("...")
    @Story("Basic")
    @Severity(SeverityLevel.MINOR)
    public void test009(){
        wiremockServer.stubFor(get(urlEqualTo("/Rest/file")).willReturn(aResponse().withStatus(200).withBodyFile("filebody.json")));

        given().when().get("/Rest/file").then().log().all();
    }
}
