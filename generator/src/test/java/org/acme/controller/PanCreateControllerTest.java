package org.acme.controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.acme.dto.PanCreateDTO;
import org.acme.middlewares.PanGenerator;
import org.acme.service.PanCreateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class PanCreateControllerTest {
    @InjectMocks
    private PanCreateController panCreateController;

    @Mock
    private PanCreateService panCreateService;

    @Mock
    private PanGenerator panGenerator;

    @Mock
    private ExecutorService executorService;

    private static String requestBody = "{\n" +
            "  \"bin\": \"123456\",\n" +
            "  \"quantity\": \"1\" \n}";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQuantityGreaterThan1000() {
        PanCreateDTO panCreateDTO = new PanCreateDTO();
        panCreateDTO.setBin(123456);
        panCreateDTO.setQuantity(1500);

        Response response = panCreateController.perform(panCreateDTO, null);

        assertEquals(200, response.getStatus(), "O status da resposta deve ser 200 para quantity > 1000");
    }

    @Test
    public void testQuantityBetween501And999() {
        PanCreateDTO panCreateDTO = new PanCreateDTO();
        panCreateDTO.setBin(123456);
        panCreateDTO.setQuantity(700);

        Response response = panCreateController.perform(panCreateDTO, null);

        assertEquals(200, response.getStatus(), "O status da resposta deve ser 200 para 500 < quantity <= 999");
    }

    @Test
    public void testQuantityLessThan1() {
        PanCreateDTO panCreateDTO = new PanCreateDTO();
        panCreateDTO.setBin(123456);
        panCreateDTO.setQuantity(0);

        Response response = panCreateController.perform(panCreateDTO, null);

        assertEquals(500, response.getStatus(), "O status da resposta deve ser 500 para quantity < 1");
    }

    @Test
    public void testPerform() {
        PanCreateDTO panCreateDTO = new PanCreateDTO();
        panCreateDTO.setBin(123456);
        panCreateDTO.setQuantity(10);

        List<String> pans = new ArrayList<>();
        pans.add("1234567812345678");

        when(panGenerator.genRandomPansBatch(5, 10, 6)).thenReturn(pans);

        panCreateController.perform(panCreateDTO, null);
    }

    @Test
    void testPanCreateEndpoint() {
        given().header("Content-type", "application/json")
                .and().body(requestBody)
                .when()
                .post("/v1/pans")
                .then().statusCode(200).extract().response();
    }

}
