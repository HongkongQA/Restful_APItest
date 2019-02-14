package framework;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.preemptive;

public class TestJenkins {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        RestAssured.baseURI = "http://localhost:1080/";
        RestAssured.authentication = preemptive().basic("admin", "asdf123456");
    }

    @Test
    void GetLastBuildTestResult() {

        given().when().get("job/Notification_Server_API/5/").then().log().all().statusCode(200);
    }

    @Test
    public void RunJob() {

        given().when().post("job/Notification_Server_API/build").then().log().all().statusCode(201);
    }
}
