package org.acme.service;

import org.acme.dto.BinTotalDTO;
import org.acme.entity.PanEntity;
import org.acme.repository.PanRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PanReportService {
    @Inject
    private PanRepository panRepository;

    public List<BinTotalDTO> findAndCountBinPerDate(String date) {
        Map<String, Long> binTotals = new HashMap<>();

        List<PanEntity> items = panRepository.find("createdAt = ?1", date).list();

        for (PanEntity item : items) {
            String bin = item.getBin();
            if (!binTotals.containsKey(bin)) {
                long total = panRepository.count("bin = ?1 and createdAt = ?2", bin, date);
                binTotals.put(bin, total);
            }
        }

        List<BinTotalDTO> totalBins = binTotals.entrySet().stream()
                .map(entry -> new BinTotalDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return totalBins;
    }

}
