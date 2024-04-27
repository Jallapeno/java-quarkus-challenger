package org.acme.middlewares;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PanGenerator {

    private static final int BATCH_SIZE = 1000; // Tamanho do lote
    private static final int THREAD_POOL_SIZE = 1000; // Número de threads no pool

    public List<String> genRandomPansBatch(int batchSize, int quantity, int size) {
        List<String> pans = new ArrayList<>();
        Random random = new Random();

        for (int batch = 0; batch < quantity; batch += batchSize) {
            List<Long> batchPans = new ArrayList<>();
            // tratamento para o ultimo lote incompleto
            int remaining = Math.min(batchSize, quantity - batch);
            for (int i = 0; i < remaining; i++) {
                int panWithoutLuhn = 0;
                if (size == 8) {
                    panWithoutLuhn = random.nextInt(900000000) + 100000000;
                } else if (size == 6) {
                    panWithoutLuhn = random.nextInt(9000000) + 1000000;
                }
                int luhnDigit = calculateLuhnDigit(panWithoutLuhn);
                long panWithLuhn = panWithoutLuhn * 10L + luhnDigit;
                batchPans.add(panWithLuhn);
            }
            pans.addAll(batchPans.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        return pans;
    }

    private int calculateLuhnDigit(int number) {
        int sum = 0;
        boolean doubleDigit = false;

        while (number > 0) {
            int digit = number % 10;

            // Dobrar o dígito se for necessário
            digit = doubleDigit ? doubleDigitDigit(digit) : digit;

            sum += digit;
            doubleDigit = !doubleDigit;
            number /= 10;
        }

        int remainder = sum % 10;
        int luhnDigit = (remainder == 0) ? 0 : (10 - remainder);
        return luhnDigit;
    }

    private int doubleDigitDigit(int digit) {
        digit *= 2;
        if (digit > 9) {
            digit -= 9;
        }
        return digit;
    }

    private static class PanGenerated {
        String panNumber;
    }

}
