import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskTimerApp extends JFrame implements ActionListener {
    private JLabel taskLabel, timeLabel, detailsLabel;
    private JTextField taskField;
    private JButton startButton, stopButton, resetButton;
    private Timer timer;
    private LocalDateTime startTime;
    private String currentTask;

    public TaskTimerApp() {
        setTitle("Task Timer");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Panel for task input
        taskLabel = new JLabel("Task:");
        taskLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        taskLabel.setBounds(10, 20, 100, 30);
        add(taskLabel);

        taskField = new JTextField(30);
        taskField.setFont(new Font("Arial", Font.PLAIN, 20));
        taskField.setBounds(120, 20, 400, 30);
        add(taskField);

        // Panel for buttons
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.setBounds(50, 70, 100, 30);
        add(startButton);

        stopButton = new JButton("Stop");
        stopButton.setFont(new Font("Arial", Font.PLAIN, 20));
        stopButton.setBounds(200, 70, 100, 30);
        add(stopButton);

        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 20));
        resetButton.setBounds(350, 70, 100, 30);
        add(resetButton);

        // Panel for displaying elapsed time
        timeLabel = new JLabel("Time Elapsed: 0 seconds");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        timeLabel.setBounds(10, 120, 300, 30);
        add(timeLabel);

        // Panel for displaying task details
        detailsLabel = new JLabel("");
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsLabel.setBounds(10, 160, 500, 30);
        add(detailsLabel);

        // Add action listeners
        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        resetButton.addActionListener(this);

        // Create timer
        timer = new Timer(1000, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startTask();
        } else if (e.getSource() == stopButton) {
            stopTask();
        } else if (e.getSource() == resetButton) {
            resetTask();
        } else if (e.getSource() == timer) {
            updateTime();
        }
    }

    private void startTask() {
        currentTask = taskField.getText();
        if (!currentTask.isEmpty()) {
            startTime = LocalDateTime.now();
            timer.start();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a task name.");
        }
    }

    private void stopTask() {
        if (startTime != null) {
            timer.stop();
            saveTaskDetails();
            startTime = null;
            currentTask = null;
            taskField.setText("");
        }
    }

    private void resetTask() {
        taskField.setText("");
        timeLabel.setText("Time Elapsed: 0 seconds");
        detailsLabel.setText("");
        timer.stop();
    }

    private void updateTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        long seconds = Duration.between(startTime, currentTime).getSeconds();
        timeLabel.setText("Time Elapsed: " + seconds + " seconds");
    }

    private void saveTaskDetails() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("task_details.txt", true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeStr = startTime.format(formatter);
            String endTimeStr = LocalDateTime.now().format(formatter);
            long seconds = Duration.between(startTime, LocalDateTime.now()).getSeconds();
            String line = String.format("%s - %s: %s (Time spent: %d seconds)%n", startTimeStr, endTimeStr, currentTask, seconds);
            writer.write(line);
            detailsLabel.setText("Task details saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskTimerApp app = new TaskTimerApp();
            app.setVisible(true);
        });
    }
}





