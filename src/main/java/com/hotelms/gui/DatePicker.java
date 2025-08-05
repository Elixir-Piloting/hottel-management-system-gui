package com.hotelms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DatePicker extends JDialog implements ActionListener {
    private LocalDate selectedDate = null;
    private LocalDate displayedMonth;

    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JPanel dayPanel;
    private JButton[] dayButtons;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DatePicker(Dialog parent) {
        super(parent, "Select Date", true);
        setSize(300, 250);
        setResizable(false);
        setLocationRelativeTo(parent);

        displayedMonth = LocalDate.now();

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Month and Year selection
        JPanel topPanel = new JPanel(new FlowLayout());
        monthComboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(LocalDate.of(displayedMonth.getYear(), i, 1).getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        }
        monthComboBox.setSelectedIndex(displayedMonth.getMonthValue() - 1);
        monthComboBox.addActionListener(this);
        topPanel.add(monthComboBox);

        yearComboBox = new JComboBox<>();
        for (int i = displayedMonth.getYear() - 5; i <= displayedMonth.getYear() + 5; i++) {
            yearComboBox.addItem(i);
        }
        yearComboBox.setSelectedItem(displayedMonth.getYear());
        yearComboBox.addActionListener(this);
        topPanel.add(yearComboBox);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Day panel
        dayPanel = new JPanel(new GridLayout(7, 7));
        dayButtons = new JButton[49]; // 7 for day names + 42 for days

        // Day names
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            dayButtons[i] = new JButton(dayNames[i]);
            dayButtons[i].setEnabled(false);
            dayButtons[i].setBackground(Color.LIGHT_GRAY);
            dayPanel.add(dayButtons[i]);
        }

        for (int i = 7; i < 49; i++) {
            dayButtons[i] = new JButton();
            dayButtons[i].setFocusPainted(false);
            dayButtons[i].setBackground(Color.WHITE);
            dayButtons[i].addActionListener(this);
            dayPanel.add(dayButtons[i]);
        }
        mainPanel.add(dayPanel, BorderLayout.CENTER);

        updateCalendar();
    }

    private void updateCalendar() {
        for (int i = 7; i < 49; i++) {
            dayButtons[i].setText("");
            dayButtons[i].setEnabled(false);
            dayButtons[i].setBackground(Color.WHITE);
        }

        LocalDate firstDayOfMonth = LocalDate.of(displayedMonth.getYear(), displayedMonth.getMonth(), 1);
        int dayOfWeekValue = firstDayOfMonth.getDayOfWeek().getValue();
        // Adjust for Sunday being 0 in Calendar, but 7 in DayOfWeek (Monday=1 to Sunday=7)
        int offset = (dayOfWeekValue == 7) ? 0 : dayOfWeekValue;

        for (int i = 0; i < displayedMonth.lengthOfMonth(); i++) {
            dayButtons[7 + offset + i].setText(String.valueOf(i + 1));
            dayButtons[7 + offset + i].setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == monthComboBox) {
            displayedMonth = displayedMonth.withMonth(monthComboBox.getSelectedIndex() + 1);
            updateCalendar();
        } else if (e.getSource() == yearComboBox) {
            displayedMonth = displayedMonth.withYear((Integer) yearComboBox.getSelectedItem());
            updateCalendar();
        } else {
            for (int i = 7; i < 49; i++) {
                if (e.getSource() == dayButtons[i] && dayButtons[i].isEnabled() && !dayButtons[i].getText().isEmpty()) {
                    int day = Integer.parseInt(dayButtons[i].getText());
                    selectedDate = LocalDate.of(displayedMonth.getYear(), displayedMonth.getMonth(), day);
                    dispose();
                    return;
                }
            }
        }
    }

    public String getSelectedDate() {
        if (selectedDate != null) {
            return selectedDate.format(DATE_FORMATTER);
        } else {
            return null;
        }
    }
}