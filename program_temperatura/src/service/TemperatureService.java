package service;

import model.TemperatureRecord;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para procesar datos de temperatura usando programación funcional.
 */
public class TemperatureService {
    /**
     * Calcula el promedio de temperatura por ciudad en un rango de fechas.
     * @param records Lista de registros.
     * @param from Fecha inicial (inclusive).
     * @param to Fecha final (inclusive).
     * @return Mapa ciudad -> promedio de temperatura.
     */
    public static Map<String, Double> averageTemperatureByCity(List<TemperatureRecord> records, LocalDate from, LocalDate to) {
        return records.stream()
                .filter(r -> !r.getDate().isBefore(from) && !r.getDate().isAfter(to))
                .collect(Collectors.groupingBy(
                        TemperatureRecord::getCity,
                        Collectors.averagingDouble(TemperatureRecord::getTemperature)
                ));
    }

    /**
     * Obtiene la ciudad más calurosa y la menos calurosa en una fecha específica.
     * @param records Lista de registros.
     * @param date Fecha a consultar.
     * @return Par de Optional<TemperatureRecord> (más calurosa, menos calurosa).
     */
    public static Pair<Optional<TemperatureRecord>, Optional<TemperatureRecord>> hottestAndColdestCity(List<TemperatureRecord> records, LocalDate date) {
        var filtered = records.stream()
                .filter(r -> r.getDate().equals(date))
                .toList();
        Optional<TemperatureRecord> hottest = filtered.stream().max(Comparator.comparingDouble(TemperatureRecord::getTemperature));
        Optional<TemperatureRecord> coldest = filtered.stream().min(Comparator.comparingDouble(TemperatureRecord::getTemperature));
        return new Pair<>(hottest, coldest);
    }

    /**
     * Utilidad simple para pares.
     */
    public record Pair<A, B>(A first, B second) {}
}
