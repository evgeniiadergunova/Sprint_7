package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.OrderData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderSteps {

    public static String track;

    @Step
    public ValidatableResponse placeOrder(OrderData orderData) {
        return given()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Step
    public ValidatableResponse getListOfOrders() {
        return  given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then().statusCode(200)
                .body("orders", notNullValue());
    }

    @Step
    public void deleteOrderByTrack() {
        if (track != null && !track.isEmpty()) {
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/v1/orders/" + track)
                    .then()
                    .statusCode(200);
        }
    }




}
