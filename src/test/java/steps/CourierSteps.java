package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.CourierCredentials;
import pojo.NewCourier;

import static io.restassured.RestAssured.given;

public class CourierSteps {
    @Step
    public Response createCourier (NewCourier newCourier) {
        return given().header("Content-type", "application/json")
                .body(newCourier)
                .log().all()
                .when()
                .post("/api/v1/courier");
    }
@Step
    public Response loginCourier(CourierCredentials credentials) {
        return given().header("Content-type", "application/json")
                .body(credentials)
                .log().all()
                .when()
                .post("/api/v1/courier/login");

    }
@Step
    public Response  deleteCourier(int courierId) {
        return given().header("Content-type", "application/json")
                .when()
                .log().all()
                .delete("/api/v1/courier/" + courierId);
    }

@Step
    public Response createCourierWithMissingParameter(CourierCredentials credentials) {
        return given().header("Content-type", "application/json")
                .body(credentials)
                .when().post("/api/v1/courier");
    }
@Step
    public Response loginWithWrongDataFailed(CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login");
    }
    @Step
    public Response loginWithMissingParameterFailed (CourierCredentials courierCredentials) {
        return given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login");
    }
}
