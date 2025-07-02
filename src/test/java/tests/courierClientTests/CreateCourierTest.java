package tests.courierClientTests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.CourierCredentials;
import pojo.NewCourier;
import steps.CourierSteps;
import utils.SetUpURI;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertTrue;

public class CreateCourierTest {
    private static NewCourier testCourier;
    private static final CourierSteps courierClientSteps = new CourierSteps();
    private static int courierID;

    @BeforeClass
    public static void setUp() {
        SetUpURI set = new SetUpURI();
        set.setUp();

        }

        @Before
        public void setUpCourier() {
            testCourier = new NewCourier("Evgeniia" + new Random().nextInt(1000), "123456", "Evgeniia" + new Random().nextInt(1000));

        }

    @Test //OK
    @DisplayName("Successful creation of a courier")
    public void createCourier() {
        Response createCourierResponse = courierClientSteps.createCourier(testCourier);
        createCourierResponse.then().statusCode(201).body("ok", equalTo(true));

    }

    @Test //OK
    @DisplayName("Creation of two same couriers failed")
    public void createSecondCourierFailed() {
        Response createCourierResponse = courierClientSteps.createCourier(testCourier);
        createCourierResponse.then().statusCode(201);
        Response createSecondCourierResponse = courierClientSteps.createCourier(testCourier);
        createSecondCourierResponse.then().statusCode(409).body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Courier creation with missing parameter failed")
    public void createCourierWithMissingParameterFailed() {

        String password = testCourier.getPassword();
        testCourier.setPassword(password.replaceAll(password, ""));
        CourierCredentials missingPassword = new CourierCredentials(testCourier.getLogin(), testCourier.getPassword());
        Response createCourierWithMissingParameter = courierClientSteps.createCourierWithMissingParameter(missingPassword);
        createCourierWithMissingParameter.then().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }


    @After
    public void deleteCourier() {
        CourierCredentials credentials = new CourierCredentials(testCourier.getLogin(), testCourier.getPassword());
        Response loginResponse = courierClientSteps.loginCourier(credentials);
        int statusCode = loginResponse.getStatusCode();

        if (statusCode == 200) {
            courierID = loginResponse.path("id");
            assertTrue("Courier ID должен быть больше 0", courierID > 0);

            Response deleteResponse = courierClientSteps.deleteCourier(courierID);
            deleteResponse.then().statusCode(200).body("ok", equalTo(true));
        }

    }


}
