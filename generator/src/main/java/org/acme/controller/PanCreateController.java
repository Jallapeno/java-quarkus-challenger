package org.acme.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.acme.dto.PanCreateDTO;
import org.acme.dto.PanDTO;
import org.acme.middlewares.PanGenerator;
import org.acme.service.PanCreateService;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/v1/pans")
public class PanCreateController {
    @Inject
    PanCreateService panCreateService;

    @Inject
    PanGenerator panGenerator;

    @Inject
    ExecutorService executorService;

    @POST
    public Response perform(PanCreateDTO panCreateDTO, @Context Request request) {
        try {
            int bin = panCreateDTO.getBin();
            int quantity = panCreateDTO.getQuantity();
            int size;
            int BATCH_SIZE;
            String binStr = String.valueOf(bin);

            if (quantity >= 1000) {
                BATCH_SIZE = (int) Math.floor(Math.ceil(1000 * 0.25) / 2);
            } else if (quantity <= 999 && quantity > 500) {
                BATCH_SIZE = 500;
            } else {
                BATCH_SIZE = quantity;
            }

            if (binStr.matches("[1-9][0-9]{5}|[1-9][0-9]{7}")) {
                size = binStr.length() == 6 ? 8 : 6;
            } else {
                return Response.serverError()
                        .entity("Erro: O valor de bin deve ter 6 ou 8 dígitos sem zeros à esquerda.").build();
            }

            if (quantity < 1) {
                return Response.serverError()
                        .entity("Erro: quantity deve ser maior que zero.").build();
            }

            List<String> pans = panGenerator.genRandomPansBatch(BATCH_SIZE, quantity, size);
            List<String> pansSaved = new ArrayList<>();

            LocalDate dateNow = LocalDate.now();
            String dateString = dateNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            executorService = Executors.newFixedThreadPool(BATCH_SIZE);

            for (int i = 0; i < pans.size(); i += BATCH_SIZE) {
                List<String> panBatch = pans.subList(i, Math.min(i + BATCH_SIZE, pans.size()));
                pansSaved.addAll(MakeBatchToSavePans(panBatch, executorService, binStr, dateString));
            }

            executorService.shutdown();

            JsonObject responsePansJson = Json.createObjectBuilder().add("pans", Json.createArrayBuilder(pansSaved))
                    .build();

            return Response.ok(responsePansJson.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    public List<String> MakeBatchToSavePans(List<String> panBatch, ExecutorService executorService, String binStr,
            String dateNowFormated) {
        List<String> pansSaved = new ArrayList<>();
        for (String pan : panBatch) {
            executorService.submit(() -> {
                try {
                    savePan(pan, binStr, dateNowFormated);
                } catch (Exception e) {
                    System.err.println("Erro ao enviar o PAN " + pan + " : " + e.getMessage());
                }
            });
            pansSaved.add(binStr + pan);
        }
        return pansSaved;
    }

    @Transactional
    public void savePan(String pan, String binStr, String dateNowFormated) {
        PanDTO newPanDTO = new PanDTO();
        newPanDTO.setBin(binStr);
        String checkDigit = String.valueOf(pan.charAt(pan.length() - 1));
        newPanDTO.setAccountNumber(pan);
        newPanDTO.setCheckDigit(checkDigit);
        newPanDTO.setCreatedAt(dateNowFormated);
        panCreateService.save(newPanDTO);
    }

}
