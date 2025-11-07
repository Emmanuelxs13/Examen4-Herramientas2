package model;

import java.time.LocalDate;

/**
 * Clase inmutable que representa un registro de temperatura para una ciudad en una fecha específica.
 */
public final class TemperatureRecord {
    private final String city;
    private final LocalDate date;
    private final double temperature;

    public TemperatureRecord(String city, LocalDate date, double temperature) {
        this.city = city;
        this.date = date;
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTemperature() {
        return temperature;
    }
}
