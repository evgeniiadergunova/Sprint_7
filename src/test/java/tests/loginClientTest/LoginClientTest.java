package tests.loginClientTest;

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

public class LoginClientTest {

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

    //api/v1/courier/login
    @Test //OK
    @DisplayName("Created courier can log in")
    public void loginCourier() {
        courierClientSteps.createCourier(testCourier);
        CourierCredentials credentials = new CourierCredentials(testCourier.getLogin(), testCourier.getPassword());
        Response responseLoginCourier = courierClientSteps.loginCourier(credentials);

        courierID = responseLoginCourier.path("id");
        assertTrue("Courier ID должен быть больше 0", courierID > 0);
    }


    @Test //OK
    @DisplayName("Login with wrong password failed")
    public void loginWithWrongPasswordFailed() {
        courierClientSteps.createCourier(testCourier);
        String password = testCourier.getPassword();

        testCourier.setPassword(password.concat("12"));
        CourierCredentials wrongCreds = new CourierCredentials(testCourier.getLogin(), testCourier.getPassword());
        Response loginWithWrongPassword = courierClientSteps.loginWithWrongDataFailed(wrongCreds);
        loginWithWrongPassword.then().statusCode(404).body("message", equalTo("Учетная запись не найдена"));
        testCourier.setPassword(password);

    }



    @Test //OK
    @DisplayName("Login with missing parameter failed")
    public void loginWithMissingPasswordFailed() {
        courierClientSteps.createCourier(testCourier);
        String password = testCourier.getPassword();
        testCourier.setPassword(password.replaceAll(password, ""));

        CourierCredentials missingParameter = new CourierCredentials(testCourier.getLogin(), testCourier.getPassword());
        Response loginWithMissingParameter = courierClientSteps.loginWithWrongDataFailed(missingParameter);
        loginWithMissingParameter.then().statusCode(400).body("message", equalTo("Недостаточно данных для входа"));
        testCourier.setPassword(password);

    }

    @Test //After not needed
    @DisplayName("Login with non existent courier")
    public void loginWithNonExistentCourier() {
        CourierCredentials missingParameter = new CourierCredentials(testCourier.getLogin(), testCourier.getPassword());
        Response loginWithNonExistentUser = courierClientSteps.loginWithWrongDataFailed(missingParameter);
        loginWithNonExistentUser.then().statusCode(404).body("message", equalTo("Учетная запись не найдена"));



    }

    @After
    public void deleteCourier() {

        CourierCredentials credentials = new CourierCredentials(testCourier.getLogin(), testCourier.getPassword());
        Response loginResponse = courierClientSteps.loginCourier(credentials);
        int statusCode = loginResponse.getStatusCode();

        if(statusCode == 200) {
            courierID = loginResponse.path("id");
            assertTrue("Courier ID должен быть больше 0", courierID > 0);

            Response deleteResponse = courierClientSteps.deleteCourier(courierID);
            deleteResponse.then().statusCode(200).body("ok", equalTo(true));
        }

    }
}
