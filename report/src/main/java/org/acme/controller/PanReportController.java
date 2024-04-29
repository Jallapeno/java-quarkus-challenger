package org.acme.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.acme.service.PanReportService;
import org.acme.dto.BinTotalDTO;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/pans")
public class PanReportController {
    @Inject
    PanReportService panReportService;

    @GET
    @Path("/{year}/{month}/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BinTotalDTO> perform(
            @PathParam("year") String yearParam,
            @PathParam("month") String monthParam,
            @PathParam("day") String dayParam) {

        if (!yearParam.matches("\\d+") || !monthParam.matches("\\d+") || !dayParam.matches("\\d+")) {
            throw new IllegalArgumentException("Os parâmetros year, month e day devem ser numéricos.");
        }

        if (yearParam.length() != 4) {
            throw new IllegalArgumentException("O parâmetro 'year' deve ter 4 dígitos.");
        }

        if (monthParam.length() != 2) {
            throw new IllegalArgumentException("O parâmetro 'month' deve ter 2 dígitos.");
        }

        if (dayParam.length() != 2) {
            throw new IllegalArgumentException("O parâmetro 'day' deve ter 2 dígitos.");
        }

        int year = Integer.parseInt(yearParam);
        int month = Integer.parseInt(monthParam);
        int day = Integer.parseInt(dayParam);

        LocalDate date = LocalDate.of(year, month, day);
        String dateFormat = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return panReportService.findAndCountBinPerDate(dateFormat, 100000);

    }
}
