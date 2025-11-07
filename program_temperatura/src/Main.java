// Clase principal que orquesta la aplicación y la interacción con el usuario.
// Usa programación funcional y servicios para cargar, procesar y mostrar los datos.

import model.TemperatureRecord;
import service.CsvService;
import service.TemperatureService;
import service.ChartService;
import service.TemperatureService.Pair;
import utils.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    // Ventana principal y paneles globales
    private static JFrame mainFrame;
    private static JPanel contentPanel;
    private static JPanel menuPanel;

    public static void main(String[] args) {
        mostrarCriteriosCSV();
        SwingUtilities.invokeLater(Main::initUI);
    }

    /**
     * Inicializa la interfaz gráfica principal con menú inferior fijo.
     */
    private static void initUI() {
        mainFrame = new JFrame("Temperaturas por Ciudad");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 650);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());

        contentPanel = new JPanel(new BorderLayout());
        menuPanel = crearMenuInferior();

        mainFrame.add(contentPanel, BorderLayout.CENTER);
        mainFrame.add(menuPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);

        runApp();
    }

    /**
     * Crea el panel de menú inferior con botones interactivos.
     */
    private static JPanel crearMenuInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panel.setBackground(new Color(40, 44, 52));

        JButton btnGrafica = new JButton("Gráfica de promedios por rango");
        JButton btnCalorFrio = new JButton("Ciudad más/menos calurosa por fecha");
        JButton btnSalir = new JButton("Salir");

        // Estilo moderno
        for (JButton btn : new JButton[]{btnGrafica, btnCalorFrio, btnSalir}) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(60, 63, 65));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        btnGrafica.addActionListener(e -> handleRangeChartUI());
        btnCalorFrio.addActionListener(e -> handleHottestColdestUI());
        btnSalir.addActionListener(e -> mainFrame.dispose());

        panel.add(btnGrafica);
        panel.add(btnCalorFrio);
        panel.add(btnSalir);
        return panel;
    }

    /**
     * Ejecuta la lógica principal y solicita el archivo CSV.
     */
    private static void runApp() {
        String csvPath = promptFilePath();
        if (csvPath == null) {
            mainFrame.dispose();
            return;
        }
        List<TemperatureRecord> records;
        try {
            records = CsvService.loadFromCsv(csvPath);
        } catch (Exception e) {
            showError("Error al leer el archivo: " + e.getMessage());
            mainFrame.dispose();
            return;
        }
        // Muestra la pantalla de bienvenida o instrucciones
        mostrarBienvenida(records);
    }

    /**
     * Muestra una pantalla de bienvenida o instrucciones.
     */
    private static void mostrarBienvenida(List<TemperatureRecord> records) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><center><h2>Bienvenido</h2>Seleccione una opción en el menú inferior para comenzar.</center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        panel.add(label, BorderLayout.CENTER);
        setContent(panel);
        // Guarda los registros para las acciones del menú
        menuPanel.putClientProperty("records", records);
    }

    /**
     * Muestra la gráfica de promedios por rango de fechas en el panel central.
     */
    private static void handleRangeChartUI() {
        List<TemperatureRecord> records = (List<TemperatureRecord>) menuPanel.getClientProperty("records");
        if (records == null) return;
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Fecha inicial (yyyy-MM-dd):"));
        JTextField fromField = new JTextField(10);
        inputPanel.add(fromField);
        inputPanel.add(new JLabel("Fecha final (yyyy-MM-dd):"));
        JTextField toField = new JTextField(10);
        inputPanel.add(toField);
        int res = JOptionPane.showConfirmDialog(mainFrame, inputPanel, "Selecciona el rango de fechas", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;
        LocalDate from = DateUtils.parse(fromField.getText());
        LocalDate to = DateUtils.parse(toField.getText());
        if (from == null || to == null || from.isAfter(to)) {
            showError("Fechas inválidas.");
            return;
        }
        Map<String, Double> averages = TemperatureService.averageTemperatureByCity(records, from, to);
        if (averages.isEmpty()) {
            showError("No hay datos en ese rango.");
            return;
        }
        // Panel para gráfica
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.add(new JLabel("<html><center><h3>Promedio de temperatura por ciudad</h3></center></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        chartPanel.add(new service.ChartService.ChartPanelWrapper(averages, "Promedio de temperatura por ciudad"), BorderLayout.CENTER);
        setContent(chartPanel);
    }

    /**
     * Muestra la ciudad más y menos calurosa en una fecha específica.
     */
    private static void handleHottestColdestUI() {
        List<TemperatureRecord> records = (List<TemperatureRecord>) menuPanel.getClientProperty("records");
        if (records == null) return;
        String dateStr = JOptionPane.showInputDialog(mainFrame, "Fecha (yyyy-MM-dd):");
        LocalDate date = DateUtils.parse(dateStr);
        if (date == null) {
            showError("Fecha inválida.");
            return;
        }
        Pair<Optional<TemperatureRecord>, Optional<TemperatureRecord>> result = TemperatureService.hottestAndColdestCity(records, date);
        StringBuilder sb = new StringBuilder();
        result.first().ifPresentOrElse(
                r -> sb.append("Más calurosa: ").append(r.getCity()).append(" (" + r.getTemperature() + "°C)\n"),
                () -> sb.append("No hay datos para esa fecha.\n")
        );
        result.second().ifPresentOrElse(
                r -> sb.append("Menos calurosa: ").append(r.getCity()).append(" (" + r.getTemperature() + "°C)\n"),
                () -> {}
        );
        JPanel resultPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html><center><h3>Resultados</h3></center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        area.setBackground(new Color(245, 245, 245));
        resultPanel.add(label, BorderLayout.NORTH);
        resultPanel.add(new JScrollPane(area), BorderLayout.CENTER);
        setContent(resultPanel);
    }

    /**
     * Cambia el contenido central de la ventana principal con transición suave.
     */
    private static void setContent(JPanel newContent) {
        contentPanel.removeAll();
        contentPanel.add(newContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Solicita al usuario la ruta del archivo CSV.
     */
    private static String promptFilePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona el archivo CSV de temperaturas");
        int res = chooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    /**
     * Muestra un mensaje de error.
     */
    private static void showError(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Muestra en la terminal los criterios que debe tener el archivo CSV.
    private static void mostrarCriteriosCSV() {
        System.out.println("\n=== Formato requerido para el archivo CSV de temperaturas ===");
        System.out.println("El archivo debe tener el siguiente encabezado y formato:");
        System.out.println("Ciudad,Fecha,Temperatura");
        System.out.println("Ejemplo de filas válidas:");
        System.out.println("CiudadA,2023-01-01,25.3");
        System.out.println("CiudadB,2023-01-01,22.1");
        System.out.println("\n- La fecha debe estar en formato yyyy-MM-dd.");
        System.out.println("- La temperatura debe ser un número (puede tener decimales).");
        System.out.println("- No debe haber columnas extra ni faltar ninguna columna.");
        System.out.println("============================================================\n");
    }
}
