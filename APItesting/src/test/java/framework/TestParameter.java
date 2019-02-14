package framework;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.apache.poi.ss.usermodel.*;
import org.apache.tika.exception.EncryptedDocumentException;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;
import static org.hamcrest.Matchers.hasKey;

// 1.excel source 可以滿足大部分需求
// 2.前面幾種就適合一些比較固定的數據

public class TestParameter {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        RestAssured.baseURI = "https://api.github.com/";
        RestAssured.authentication = oauth2("c959a90ae1411d779d7e1ce6b979469ebb6993eb");

    }
    // 1 value Source
    @DisplayName("Get user information")
    @ParameterizedTest(name = "Executed {index} times to get {0} user data")
    @ValueSource(strings= {"hongkongqa","rest-assured","GT"})
    void test_valueSource(String username) {
        given().pathParam("user", username).when().get("/users/{user}").then().log().all();
    }

    enum checkuser {
        login,
        id,
        url
    }

    // 2 enum
    @DisplayName("Check user information field")
    @ParameterizedTest(name="--{index}-- contains the {0} field")
    @EnumSource(checkuser.class)
    public void test_enum(checkuser argu) {

        given().pathParam("user", "Hongkongqa").when().get("users/{user}").then().body("$", hasKey(argu.toString()));

    }

    // 3 csv
    @DisplayName("Get Repo and judge status")
    @ParameterizedTest(name="--{index}--Get the status of {0}/{1}{2}")
    @CsvSource({
            "hongkongqa,LINK_Automation,200",
            "error,wrong,404"
    })
    public void test_csv(String user, String repo, int status) {
        given().log().uri().pathParam("user", user).pathParam("repo", repo).when().get("repos/{user}/{repo}").then().statusCode(status);
    }

    // 4 csvfile source
    @DisplayName("External data in CSV format to get repo information")
    @ParameterizedTest(name="--{index}--Get Repo:{0}/{1}")
    @CsvFileSource(resources = "/repo.csv", numLinesToSkip = 1)
    public void test_csvFile(String user, String repo, int status) {
        given().log().uri().pathParam("user", user).pathParam("repo", repo).when().get("repos/{user}/{repo}").then().statusCode(status);
    }

    // 5 excel
    @DisplayName("Get the repo information of the external data source of Excel")
    @ParameterizedTest(name="--{index}-- Get repo:{0}/{1}")
    @MethodSource("getRepoFromExcel")
    public void test_ExcelDataFile(String user, String repo, int status) {

        given().log().uri().pathParam("user", user).pathParam("repo", repo).when().get("repos/{user}/{repo}").then().statusCode(status);
    }


    static Stream<Arguments> getRepoFromExcel(){
        return getExcelDataFromFile("/Users/colbert.zhou/Desktop/APItesting/src/test/resources/RepoData.xlsx","Sheet1");
    }

    // POI
    public static Stream<Arguments> getExcelDataFromFile(String ExcelFilePath, String SheetName){
        Stream<Arguments> returnStream = Stream.empty();

        try(Workbook workbook = WorkbookFactory.create(new File(ExcelFilePath))) {
            Sheet dataSheet = workbook.getSheet(SheetName);
            DataFormatter dfm = new DataFormatter();

            for(Row row: dataSheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                ArrayList<Object> rowList = new ArrayList<>();

                for(Cell cell: row) {
                    rowList.add(dfm.formatCellValue(cell));
                }
                Arguments arg = Arguments.of(rowList.toArray());
                returnStream = Stream.concat(returnStream,Stream.of(arg));
            }
            return returnStream;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnStream;

    }
}
