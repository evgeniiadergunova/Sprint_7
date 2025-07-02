package utils;
import io.restassured.RestAssured;

public class SetUpURI {

    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
    }
}

