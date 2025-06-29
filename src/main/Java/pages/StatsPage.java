package pages;

import assemblers.Setup;
import utils.Formatter;
import utils.Visual;
import assemblers.Stats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StatsPage {

    private static JFrame statsFrame;
    private static String username;
    private static String carName;

    private static boolean[] filtered = {true};

    public StatsPage(JFrame frameIn, String username, String carName) {
        StatsPage.username = username;
        StatsPage.carName = carName;
        showStatsPage(frameIn);
    }

    private static void showStatsPage(JFrame frameIn) {
        statsFrame = new JFrame("Statistics");
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.setSize(500, 300);
        statsFrame.setLocationRelativeTo(null);

        int currentMonth = Formatter.getCurrentMonth();

        JLabel titleLabel = new JLabel("Statistics for " + carName);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Yearly
        JLabel yearlySpendLabel = new JLabel("Spent this year: " + Stats.getYearSpend(username, carName, filtered[0]) + " €");
        JLabel yearlyFuelsLabel = new JLabel("Fuels this year: " + Stats.getYearNumberFuels(username, carName, filtered[0]));
        JLabel yearlyMinPriceLabel = new JLabel("Lowest this year: " + Stats.getYearMinPrice(username, carName, filtered[0]) + " €/l");
        JLabel yearlyMaxPriceLabel = new JLabel("Highest this year: " + Stats.getYearMaxPrice(username, carName, filtered[0]) + " €/l");

        // Monthly
        JLabel monthlySpendLabel = new JLabel("Spent this month: " + Stats.getMonthSpend(username, carName, filtered[0], currentMonth) + " €");
        JLabel monthlyFuelsLabel = new JLabel("Fuels this month: " + Stats.getMonthNumberFuels(username, carName, filtered[0], currentMonth));
        JLabel monthlyMinPriceLabel = new JLabel("Lowest this month: " + Stats.getMonthMinPrice(username, carName, filtered[0], currentMonth) + " €/l");
        JLabel monthlyMaxPriceLabel = new JLabel("Highest this month: " + Stats.getMonthMaxPrice(username, carName, filtered[0], currentMonth) + " €/l");


        // Layout
        JPanel statsPanel = new JPanel(new BorderLayout(10, 10));
        statsFrame.getContentPane().add(statsPanel);

        JPanel statsDetailsPanel = new JPanel(new GridLayout(7, 1, 0, 5));
        statsDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel titleLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleLabelPanel.add(titleLabel);

        statsDetailsPanel.add(titleLabelPanel);

        filtered = new boolean[]{true};

        boolean MoreThanZeroCarFuels = Setup.getMoreThanZeroCarFuels(username, carName);

        if (!MoreThanZeroCarFuels) {
            filtered[0] = false;
        }

        boolean hasTwoCarWithFuels = Setup.hasTwoCarWithFuels(username);

        if (hasTwoCarWithFuels && MoreThanZeroCarFuels) {
            JLabel viewAllLabel = new JLabel("                                                                view all cars.");
            viewAllLabel.setFont(new Font("Arial", Font.ITALIC, 12));

            viewAllLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Add your click event handling code here
                    if (filtered[0]) {
                        filtered[0] = false;
                        titleLabelPanel.add(viewAllLabel, BorderLayout.LINE_END);
                        viewAllLabel.setText("                                                                view " + carName + " stats.");
                        titleLabel.setText("Statistics       ");
                        updateStats(filtered);
                    } else {
                        filtered[0] = true;
                        titleLabelPanel.add(viewAllLabel, BorderLayout.LINE_END);
                        viewAllLabel.setText("                                                                view all cars.");
                        titleLabel.setText("Statistics for " + carName);
                        updateStats(filtered);
                    }
                }

                private void updateStats(boolean[] filtered) {
                    yearlySpendLabel.setText("Spent this year: " + Stats.getYearSpend(username, carName, filtered[0]) + " €");
                    yearlyFuelsLabel.setText("Fuels this year: " + Stats.getYearNumberFuels(username, carName, filtered[0]));
                    yearlyMinPriceLabel.setText("Lowest this year: " + Stats.getYearMinPrice(username, carName, filtered[0]) + " €/l");
                    yearlyMaxPriceLabel.setText("Highest this year: " + Stats.getYearMaxPrice(username, carName, filtered[0]) + " €/l");

                    monthlySpendLabel.setText("Spent this month: " + Stats.getMonthSpend(username, carName, filtered[0], currentMonth) + " €");
                    monthlyFuelsLabel.setText("Fuels this month: " + Stats.getMonthNumberFuels(username, carName, filtered[0], currentMonth));
                    monthlyMinPriceLabel.setText("Lowest this month: " + Stats.getMonthMinPrice(username, carName, filtered[0], currentMonth) + " €/l");
                    monthlyMaxPriceLabel.setText("Highest this month: " + Stats.getMonthMaxPrice(username, carName, filtered[0], currentMonth) + " €/l");
                }
            });

            titleLabelPanel.add(viewAllLabel, BorderLayout.LINE_END);
        }

        JPanel yearlyStatsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearlyStatsPanel.add(yearlySpendLabel);
        yearlyStatsPanel.add(Box.createHorizontalStrut(20));
        yearlyStatsPanel.add(yearlyFuelsLabel);
        statsDetailsPanel.add(yearlyStatsPanel);

        JPanel yearlyPricesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearlyPricesPanel.add(yearlyMinPriceLabel);
        yearlyPricesPanel.add(Box.createHorizontalStrut(20));
        yearlyPricesPanel.add(yearlyMaxPriceLabel);
        statsDetailsPanel.add(yearlyPricesPanel);

        JPanel monthlySpendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthlySpendPanel.add(monthlySpendLabel);
        monthlySpendPanel.add(Box.createHorizontalStrut(20));
        monthlySpendPanel.add(monthlyFuelsLabel);
        statsDetailsPanel.add(monthlySpendPanel);

        JPanel monthlyPricesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthlyPricesPanel.add(monthlyMinPriceLabel);
        monthlyPricesPanel.add(Box.createHorizontalStrut(20));
        monthlyPricesPanel.add(monthlyMaxPriceLabel);
        statsDetailsPanel.add(monthlyPricesPanel);

        statsPanel.add(statsDetailsPanel, BorderLayout.NORTH);

        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(1, 10));
        statsPanel.add(spacePanel, BorderLayout.CENTER);

        Visual.addFooter(statsFrame, frameIn);
        statsFrame.setVisible(true);
    }
}
