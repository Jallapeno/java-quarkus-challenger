package org.acme.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.acme.service.PanReportService;
import org.acme.dto.BinTotalDTO;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/pans")
public class PanReportController {
    @Inject
    PanReportService panReportService;

    @GET
    @Path("/{year}/{month}/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response perform(
            @PathParam("year") String yearParam,
            @PathParam("month") String monthParam,
            @PathParam("day") String dayParam) {

        try {
            if (!yearParam.matches("\\d+") || !monthParam.matches("\\d+") || !dayParam.matches("\\d+")) {
                throw new IllegalArgumentException("Os parâmetros year, month e day devem ser numéricos.");
            }

            if (yearParam.length() != 4) {
                return Response.serverError()
                        .entity("O parâmetro 'year' deve ter 4 dígitos.").build();
            }

            if (monthParam.length() != 2) {
                return Response.serverError()
                        .entity("O parâmetro 'month' deve ter 2 dígitos.").build();
            }

            if (dayParam.length() != 2) {
                return Response.serverError()
                        .entity("O parâmetro 'day' deve ter 2 dígitos.").build();
            }

            int year = Integer.parseInt(yearParam);
            int month = Integer.parseInt(monthParam);
            int day = Integer.parseInt(dayParam);

            LocalDate date = LocalDate.of(year, month, day);
            String dateFormat = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            List<BinTotalDTO> totalBins = panReportService.findAndCountBinPerDate(dateFormat);
            return Response.ok(totalBins).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

    }
}
