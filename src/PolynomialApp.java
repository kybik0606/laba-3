import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PolynomialApp {
    public static void main(String[] args) {
        //  Создание главного окна
        JFrame frame = new JFrame("Табулирование многочлена по схеме Горнера");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        // Верхняя панель для ввода интервала и шага
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        JLabel startLabel = new JLabel("X изменяется на интервале от:");
        JTextField startField = new JTextField("0.0");
        JLabel endLabel = new JLabel("до:");
        JTextField endField = new JTextField("1.0");
        JLabel stepLabel = new JLabel("с шагом:");
        JTextField stepField = new JTextField("0.1");
        JButton calculateButton = new JButton("Вычислить");
        JButton clearButton = new JButton("Очистить поля");

        inputPanel.add(startLabel);
        inputPanel.add(startField);
        inputPanel.add(endLabel);
        inputPanel.add(endField);
        inputPanel.add(stepLabel);
        inputPanel.add(stepField);
        inputPanel.add(calculateButton);
        inputPanel.add(clearButton);

        // Модель таблицы с тремя столбцами
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Значения X", "Значение многочлена", "Точное значение?"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class; // Третий столбец — булевский
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 2; // Запрещаем редактирование чекбоксов в третьем столбце
            }
        };

        JTable table = new JTable(tableModel);

        // Установка рендера для отображения булевых значений как флажков
        table.setDefaultRenderer(Boolean.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setHorizontalAlignment(SwingConstants.CENTER);
                checkBox.setSelected(value != null && (Boolean) value);
                checkBox.setEnabled(false); // Делаем чекбокс недоступным для редактирования
                return checkBox;
            }
        });

        // Панель прокрутки для таблицы
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Справка
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Справка");
        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Программа табулирования многочлена по схеме Горнера.\n" +
                        "Автор: Черник Алексей, группа: 7",
                "О программе", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        // Установка меню
        frame.setJMenuBar(menuBar);

        // Обработчик кнопки "Вычислить"
        calculateButton.addActionListener((ActionEvent e) -> {
            tableModel.setRowCount(0); // Очищаем предыдущие результаты
            try {
                double start = Double.parseDouble(startField.getText());
                double end = Double.parseDouble(endField.getText());
                double step = Double.parseDouble(stepField.getText());

                if (step <= 0 || start > end) {
                    JOptionPane.showMessageDialog(frame, "Введите корректные значения интервала и шага!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Вычисление значений
                for (double x = start; x <= end; x += step) {
                    double polynomialValue = calculatePolynomial(x);
                    boolean isExact = Math.abs(polynomialValue - Math.round(polynomialValue)) < 1e-10; // Проверка, является ли дробная часть нулевой
                    tableModel.addRow(new Object[]{x, polynomialValue, isExact});
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Введите числовые значения!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Обработчик кнопки "Очистить"
        clearButton.addActionListener(e -> {
            startField.setText("");
            endField.setText("");
            stepField.setText("");
            tableModel.setRowCount(0);
        });

        // Компоновка компонентов
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        // Отображение окна
        frame.setVisible(true);
    }

    /**
     * Метод вычисления значения многочлена P(x) = 2x^3 - 3x^2 + x - 5
     * по схеме Горнера.
     */
    private static double calculatePolynomial(double x) {
        // Коэффициенты многочлена: 2x^3 - 3x^2 + x - 5
        double[] coefficients = {2, -3, 1, -5};
        double result = 0;

        for (double coefficient : coefficients) {
            result = result * x + coefficient;
        }
        return result;
    }
}
