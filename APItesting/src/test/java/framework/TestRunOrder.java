package framework;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;

// 依賴嘅API按順序執行

public class TestRunOrder {


    @BeforeAll
    public static void setupEnv() {

        RestAssured.baseURI = "https://api.github.com/";
        RestAssured.authentication = oauth2("c959a90ae1411d779d7e1ce6b979469ebb6993eb");
    }

    @ParameterizedTest(name = "--{index}-- Execute {0}")
    @ValueSource(strings = {"Get_Method", "Post_Method"})
    public void testAllCase(String MethodName) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Object testClass = TestRunOrder.class.newInstance();
        Method testCase = TestRunOrder.class.getDeclaredMethod(MethodName);
        testCase.invoke(testClass);
    }

    @Test
    @DisabledIf(value = {"var time = new Date()", "time.getHours() > 16"})
    void Get_Method() {
        System.out.println(System.getProperty("os.name"));

        given().log().all().when().get("user/repos").then().assertThat().log().status();

    }

    @Test
    @Disabled
    void Post_Method() {
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
