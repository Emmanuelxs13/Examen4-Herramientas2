package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utilidades para manejo y validación de fechas.
 */
public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Oculta el constructor implícito para clase utilitaria
    private DateUtils() {}

    /**
     * Parsea una cadena a LocalDate usando el formato yyyy-MM-dd.
     * @param dateStr Cadena de fecha.
     * @return LocalDate o null si el formato es inválido.
     */
    public static LocalDate parse(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Formatea un LocalDate a cadena.
     */
    public static String format(LocalDate date) {
        return date.format(FORMATTER);
    }
}
