package framework;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;

/*
*     可以根據實際項目進行指定嘅落TAG,調試，比如落@Disabled等等這些進行control
*
* */


public class RunControl {

    @BeforeAll
    public static void setupEnv(){

        RestAssured.baseURI = "https://api.github.com/";
        RestAssured.authentication = oauth2("c959a90ae1411d779d7e1ce6b979469ebb6993eb");

    }

    @Test
    //@DisabledIf(value = {"var time = new Date()","time.getHours() > 16"})
    void Get_Method(){
        System.out.println(System.getProperty("os.name"));

        given().log().all().when().get("user/repos").then().log().status();
    }

    @Test
    //@Disabled
    void Post_Method(){
        String postBody = "{\n" +
                "  \"name\": \"APItest\",\n" +
                "  \"description\": \"This is your first repository\",\n" +
                "  \"homepage\": \"https://github.com\",\n" +
                "  \"private\": false,\n" +
                "  \"has_issues\": true,\n" +
                "  \"has_projects\": true,\n" +
                "  \"has_wiki\": true\n" +
                "}";

        given().body(postBody).log().all().when().post("user/repos").then().log().all().statusCode(201);

    }
}
