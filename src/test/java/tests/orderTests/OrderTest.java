package tests.orderTests;


import io.qameta.allure.junit4.DisplayName;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.OrderData;
import steps.OrderSteps;
import utils.SetUpURI;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OrderTest {

    private OrderData orderData;
    static OrderSteps orderSteps = new OrderSteps();

    @BeforeClass
    public static void setUp() {
        SetUpURI set = new SetUpURI();
        set.setUp();

    }

    public OrderTest(OrderData orderData) {
        this.orderData = orderData;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {
                        new OrderData("Иван", "Иванов", "Москва, Ленина 1", "1", "+79991112233", "5", "2025-06-30", "Позвонить заранее", new String[]{"BLACK"})
                },
                {
                        new OrderData("Петр", "Петров", "СПб, Невский 100", "4", "+79998887766", "3", "2025-06-25", "Оставить у двери", new String[]{"GREY"})
                },
                {
                        new OrderData("Анна", "Смирнова", "Казань, Пушкина 10", "10", "+79997775544", "2", "2025-06-24", "", new String[]{})
                }
        });
    }

    @Test
    @DisplayName("Order is successfully placed")
    public void placeOrder() {
        orderSteps.placeOrder(orderData);
    }

    @Test
    @DisplayName("Get list of orders")
    public void getListOfOrders() {
        orderSteps.getListOfOrders();
    }

    @AfterClass
    public static void deleteOrder() {
        orderSteps.deleteOrderByTrack();
    }








}
