package service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Servicio para generar y mostrar gráficas de barras usando JFreeChart.
 */
public class ChartService {
    // Oculta el constructor implícito para clase utilitaria
    private ChartService() {}

    /**
     * Panel reutilizable para integrar la gráfica en la UI principal.
     */
    public static class ChartPanelWrapper extends JPanel {
        public ChartPanelWrapper(Map<String, Double> averages, String title) {
            setLayout(new BorderLayout());
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            averages.forEach((city, avg) -> dataset.addValue(avg, "Temperatura", city));
            JFreeChart barChart = ChartFactory.createBarChart(
                    title,
                    "Ciudad",
                    "Temperatura promedio",
                    dataset
            );
            ChartPanel chartPanel = new ChartPanel(barChart);
            add(chartPanel, BorderLayout.CENTER);
        }
    }

    /**
     * Muestra una gráfica de barras en una ventana independiente (modo clásico).
     */
    public static void showBarChart(Map<String, Double> averages, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new ChartPanelWrapper(averages, title));
        frame.setVisible(true);
    }
}
