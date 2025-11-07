package service;

import model.TemperatureRecord;
import utils.DateUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para cargar y parsear archivos CSV de temperaturas.
 */
public class CsvService {
    // Oculta el constructor implícito para clase utilitaria
    private CsvService() {}

    /**
     * Carga los registros de temperatura desde un archivo CSV.
     * @param filePath Ruta del archivo CSV.
     * @return Lista de TemperatureRecord.
     * @throws IOException Si ocurre un error de lectura.
     */
    public static List<TemperatureRecord> loadFromCsv(String filePath) throws IOException {
        try (var lines = Files.lines(Path.of(filePath))) {
            return lines
                    .skip(1) // Omitir encabezado
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 3)
                    .map(parts -> {
                        var date = DateUtils.parse(parts[1].trim());
                        try {
                            return new TemperatureRecord(
                                    parts[0].trim(),
                                    date,
                                    Double.parseDouble(parts[2].trim())
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(r -> r != null && r.getDate() != null)
                    .collect(java.util.stream.Collectors.toList());
        }
    }
}
