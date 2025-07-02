import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class StudentGUI extends JFrame {

    JTextField rollField, nameField, tamilField, englishField, mathsField, scienceField, socialField, searchField;
    JButton addButton, viewButton, searchButton, deleteButton;

    public StudentGUI() {
        setTitle("Student Record System - Java GUI");
        setSize(550, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(11, 2, 10, 10));

        // Input fields
        rollField = new JTextField();
        nameField = new JTextField();
        tamilField = new JTextField();
        englishField = new JTextField();
        mathsField = new JTextField();
        scienceField = new JTextField();
        socialField = new JTextField();
        searchField = new JTextField();

        // Buttons
        addButton = new JButton("Add Student");
        viewButton = new JButton("View All");
        searchButton = new JButton("Search by Roll No");
        deleteButton = new JButton("Delete by Roll No");

        // Add to layout
        add(new JLabel("Roll Number:")); add(rollField);
        add(new JLabel("Name:")); add(nameField);
        add(new JLabel("Tamil:")); add(tamilField);
        add(new JLabel("English:")); add(englishField);
        add(new JLabel("Maths:")); add(mathsField);
        add(new JLabel("Science:")); add(scienceField);
        add(new JLabel("Social:")); add(socialField);
        add(addButton); add(viewButton);
        add(new JLabel("Roll No (for Search/Delete):")); add(searchField);
        add(searchButton); add(deleteButton);

        // Button actions
        addButton.addActionListener(e -> addStudent());
        viewButton.addActionListener(e -> viewStudents());
        searchButton.addActionListener(e -> searchStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        setVisible(true);
    }

    void addStudent() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("students.txt", true))) {
            int roll = Integer.parseInt(rollField.getText().trim());
            String name = nameField.getText().trim();
            int tamil = Integer.parseInt(tamilField.getText().trim());
            int english = Integer.parseInt(englishField.getText().trim());
            int maths = Integer.parseInt(mathsField.getText().trim());
            int science = Integer.parseInt(scienceField.getText().trim());
            int social = Integer.parseInt(socialField.getText().trim());

            int total = tamil + english + maths + science + social;
            float average = total / 5.0f;
            String grade = calculateGrade(average);

            String line = roll + "|" + name + "|" + tamil + "|" + english + "|" + maths + "|" + science + "|" + social + "|" + total + "|" + average + "|" + grade;
            bw.write(line); bw.newLine();

            JOptionPane.showMessageDialog(this, "✅ Student added successfully!");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    String calculateGrade(float avg) {
        if (avg >= 90) return "A+";
        else if (avg >= 80) return "A";
        else if (avg >= 70) return "B";
        else if (avg >= 60) return "C";
        else return "D";
    }

    void clearFields() {
        rollField.setText(""); nameField.setText("");
        tamilField.setText(""); englishField.setText(""); mathsField.setText("");
        scienceField.setText(""); socialField.setText("");
    }

    void viewStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            JTextArea area = new JTextArea(20, 60);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.setEditable(false);
            area.append("Roll\tName\tTam\tEng\tMat\tSci\tSoc\tTot\tAvg\tGrade\n");
            area.append("--------------------------------------------------------------------------\n");

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 10) {
                    for (String part : parts) area.append(part + "\t");
                    area.append("\n");
                }
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(area), "All Students", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "❌ Could not read file.");
        }
    }

    void searchStudent() {
        String rollInput = searchField.getText().trim();
        if (rollInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Roll Number.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(rollInput + "|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 10) {
                        String msg = "Roll: " + parts[0] + "\nName: " + parts[1] +
                                     "\nTamil: " + parts[2] + "\nEnglish: " + parts[3] +
                                     "\nMaths: " + parts[4] + "\nScience: " + parts[5] +
                                     "\nSocial: " + parts[6] + "\nTotal: " + parts[7] +
                                     "\nAverage: " + parts[8] + "\nGrade: " + parts[9];
                        JOptionPane.showMessageDialog(this, msg, "Student Found", JOptionPane.INFORMATION_MESSAGE);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "❌ Student not found.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "❌ Could not read file.");
        }
    }

    void deleteStudent() {
        String rollInput = searchField.getText().trim();
        if (rollInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Roll Number.");
            return;
        }

        File inputFile = new File("students.txt");
        File tempFile = new File("temp.txt");

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(rollInput + "|")) {
                    found = true; // skip this line
                    continue;
                }
                bw.write(line); bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error during deletion: " + ex.getMessage());
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this, "❌ Error updating the file.");
            return;
        }

        if (found)
            JOptionPane.showMessageDialog(this, "🗑️ Student deleted successfully.");
        else
            JOptionPane.showMessageDialog(this, "❌ Student not found.");
    }

    public static void main(String[] args) {
        new StudentGUI();
    }
}
