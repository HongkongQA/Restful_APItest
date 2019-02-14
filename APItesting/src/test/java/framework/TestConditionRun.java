package framework;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.junit.Assume.assumeTrue;

public class TestConditionRun {

    static RequestSpecification reqSpec;
    static String repoID;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {

        RestAssured.baseURI = "https://api.github.com/";
        RestAssured.basePath = "search/";
        RestAssured.authentication = oauth2("c959a90ae1411d779d7e1ce6b979469ebb6993eb");

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.addParam("sort", "stars");
        reqBuilder.addParam("per_page", "25");
        reqBuilder.addHeader("Accept", "application/vnd.github.mercy-preview+json");
        reqSpec = reqBuilder.build();
    }

    @Nested
    class groupRun {
        @BeforeEach
        void search_repo() {
            String sdate, q;

            // Calendar
/*        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(calendar.DAY_OF_MONTH, -7);
        sdate = sdf.format(calendar.getTime());*/

            // Jode-time
            DateTime dt = new DateTime();
            sdate = dt.plusDays(-7).toString("yyyy-MM-dd");
            System.out.println("query date:" + sdate);

            q = "AutomationTest+created:>" + sdate;

            repoID = given().log().all().spec(reqSpec).param("q", q).
                    when().get("repositories").
                    then().log().body().body("total_count", greaterThan(0)).extract().path("items").toString();
        }

        @Test
        public void demo_Get_5th_repo() {
            assumeTrue(repoID != null);

            RestAssured.basePath = "repositories/";
            given().when().get(repoID).then().statusCode(200).log().body();

        }
    }
}
