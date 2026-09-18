package main;

import assessment.HealthAssessment;
import assessment.BMIAssessment;
import assessment.BPAssessment;
import assessment.SubstanceAssessment;

import exception.DatabaseException;
import exception.InvalidHealthDataException;

import model.RiskResult;
import model.User;

import service.UserService;
import service.CheckupService;
import service.HistoryService;

import tips.HealthTipService;

import javax.swing.*; //gui toolkit
import java.awt.*; //to create gui
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;


public class QuickHealthCheck extends JFrame {

    private User currentUser;

    private final UserService userService = new UserService();
    private final CheckupService checkupService = new CheckupService();
    private final HistoryService historyService = new HistoryService();
    private final HealthTipService tipService = new HealthTipService();

    private JTextArea outputArea;
    private JLabel currentUserLabel;

    private CardLayout cardLayout;
    private JPanel cardsPanel;

    private static final String CARD_MAIN = "MAIN";
    private static final String CARD_USER = "USER";
    private static final String CARD_EDIT_USER = "EDIT_USER";
    private static final String CARD_CHECKUP = "CHECKUP";
    private static final String CARD_TIPS = "TIPS";

    public QuickHealthCheck() {
        super("Quick Health Check");
        initUI();
    }

    // ========================================
    // UI SETUP
    // ========================================

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // ---- Header ----
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("QUICK HEALTH CHECK", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        currentUserLabel = new JLabel(" ", SwingConstants.CENTER);
        currentUserLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        headerPanel.add(titleLabel);
        headerPanel.add(currentUserLabel);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(headerPanel, BorderLayout.NORTH);

        // ---- Center: card layout with the different menus ----
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(buildMainMenuPanel(), CARD_MAIN);
        cardsPanel.add(buildUserMenuPanel(), CARD_USER);
        cardsPanel.add(buildEditUserPanel(), CARD_EDIT_USER);
        cardsPanel.add(buildCheckupMenuPanel(), CARD_CHECKUP);
        cardsPanel.add(buildTipsMenuPanel(), CARD_TIPS);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(cardsPanel, BorderLayout.CENTER);

        // ---- South: output console (mirrors what used to be System.out) ----
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(780, 230));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        JButton clearButton = new JButton("Clear Output");
        clearButton.addActionListener(e -> outputArea.setText(""));

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel clearButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        clearButtonPanel.add(clearButton);
        southPanel.add(clearButtonPanel, BorderLayout.SOUTH);

        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        add(southPanel, BorderLayout.SOUTH);

        // Redirect System.out so every existing println() in model/service
        // classes (display(), displayProfile(), etc.) shows up in the GUI,
        // exactly as before, with zero changes to those classes.
        PrintStream original = System.out;
        PrintStream teeStream = new PrintStream(new ConsoleOutputStream(outputArea, original), true);
        System.setOut(teeStream);

        updateHeader();

        log("========================================");
        log("          QUICK HEALTH CHECK");
        log("========================================");
    }

    private void updateHeader() {
        if (currentUser != null) {
            currentUserLabel.setText("Current User: " + currentUser.getName());
        } else {
            currentUserLabel.setText(" ");
        }
    }

    private void showCard(String name) {
        cardLayout.show(cardsPanel, name);
    }

    private void log(String message) {
        System.out.println(message);
    }

    // ========================================
    // MAIN MENU
    // ========================================

    private JPanel buildMainMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(sectionLabel("MAIN MENU"));

        panel.add(menuButton("1. User Management", e -> showCard(CARD_USER)));

        panel.add(menuButton("2. Health Checkup", e -> healthCheckupMenu()));

        panel.add(menuButton("3. Health History & Results", e -> healthHistory()));

        panel.add(menuButton("4. Health Tips", e -> healthTipsMenu()));

        panel.add(menuButton("0. Exit", e -> {
            log("\nThank you for using Quick Health Check!");
            dispose();
            System.exit(0);
        }));

        return panel;
    }

    // ========================================
    // MODULE 1 - USER MENU
    // ========================================

    private JPanel buildUserMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(sectionLabel("USER MANAGEMENT"));

        panel.add(menuButton("1. Select Existing User", e -> selectExistingUser()));
        panel.add(menuButton("2. New User", e -> createNewUser()));
        panel.add(menuButton("3. View User Details", e -> viewUserDetails()));
        panel.add(menuButton("4. Edit User Details", e -> {
            if (!checkUser()) {
                return;
            }
            showCard(CARD_EDIT_USER);
        }));
        panel.add(menuButton("0. Back", e -> showCard(CARD_MAIN)));

        return panel;
    }

    // SELECT EXISTING USER
    private void selectExistingUser() {
        try {
            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                log("\nNo users found.");
                log("Please create a new user first.");
                return;
            }

            log("\n========== SELECT USER ==========");

            String[] names = new String[users.size()];
            for (int i = 0; i < users.size(); i++) {
                names[i] = users.get(i).getName();
                log((i + 1) + ". " + names[i]);
            }
            log("0. Back");

            String selectedName = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a user:",
                    "Select Existing User",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    names,
                    names[0]
            );

            if (selectedName == null) {
                // Cancelled - equivalent to choosing "0. Back"
                return;
            }

            User selectedUser = null;
            for (User u : users) {
                if (u.getName().equals(selectedName)) {
                    selectedUser = u;
                    break;
                }
            }

            if (selectedUser == null) {
                log("Invalid choice.");
                return;
            }

            String password = readPassword("Enter password: ");

            User verifiedUser = userService.getUserByIdAndPassword(selectedUser.getUserId(), password);

            if (verifiedUser != null) {
                currentUser = verifiedUser;
                updateHeader();

                log("\nUser selected successfully!");
                log("Welcome, " + currentUser.getName() + "!");
            } else {
                log("\nIncorrect password.");
                log("User was not selected.");
            }

        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // VIEW USER DETAILS
    private void viewUserDetails() {
        if (!checkUser()) {
            return;
        }

        try {
            User user = userService.getUserById(currentUser.getUserId());

            if (user != null) {
                currentUser = user;
                updateHeader();
                currentUser.displayProfile();
            } else {
                log("User not found.");
            }

        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // EDIT USER DETAILS
    private JPanel buildEditUserPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(sectionLabel("EDIT USER"));

        panel.add(menuButton("1. Name", e -> {
            currentUser.setName(readString("Enter new name: "));
            updateHeader();
        }));

        panel.add(menuButton("2. Email", e ->
                currentUser.setEmail(readString("Enter new email: "))));

        panel.add(menuButton("3. Phone Number", e ->
                currentUser.setPhone(readString("Enter new phone number: "))));

        panel.add(menuButton("4. Password", e ->
                currentUser.setPassword(readString("Enter new password: "))));

        panel.add(menuButton("5. DOB / Age", e ->
                currentUser.setDobOrAge(readString("Enter new DOB / Age: "))));

        panel.add(menuButton("6. Height", e -> {
            double newHeight = readDouble("Enter new height in metres: ");

            if (newHeight <= 0) {
                log("Height must be greater than zero.");
            } else {
                currentUser.setHeight(newHeight);
            }
        }));

        panel.add(menuButton("7. Weight", e -> {
            double newWeight = readDouble("Enter new weight in kilograms: ");

            if (newWeight <= 0) {
                log("Weight must be greater than zero.");
            } else {
                currentUser.setWeight(newWeight);
            }
        }));

        panel.add(menuButton("0. Save & Back", e -> {
            try {
                userService.updateUser(currentUser);
                log("\nUser details updated successfully!");
            } catch (DatabaseException ex) {
                log("Database Error: " + ex.getMessage());
            }
            showCard(CARD_USER);
        }));

        return panel;
    }

    // NEW USER
    private void createNewUser() {
        log("\n========== NEW USER ==========");

        String name = readString("Name (enter Guest if preferred): ");
        String email = readString("Email: ");
        String phone = readString("Phone Number: ");
        String password = readString("Password: ");
        String dobAge = readString("DOB / Age: ");

        double height = readDouble("Height in metres: ");
        double weight = readDouble("Weight in kilograms: ");

        if (height <= 0 || weight <= 0) {
            log("Height and weight must be greater than zero.");
            return;
        }

        User user = new User(
                0,
                name,
                email,
                phone,
                password,
                dobAge,
                height,
                weight
        );

        try {
            currentUser = userService.registerUser(user);
            updateHeader();
            log("\nUser created successfully!");
            log("Your User ID: " + currentUser.getUserId());
        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // MODULE 2 - HEALTH CHECKUP
    // ========================================

    private JPanel buildCheckupMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(sectionLabel("HEALTH CHECKUP"));

        panel.add(menuButton("1. BMI", e -> performBMI()));
        panel.add(menuButton("2. Blood Pressure", e -> performBP()));
        panel.add(menuButton("3. Smoking", e -> performSmoking()));
        panel.add(menuButton("4. Alcohol", e -> performAlcohol()));
        panel.add(menuButton("5. Other Drugs (Opioids, Synthetic Drugs, Inhalants, Cannabis, ...)", e -> performOtherDrugs()));
        panel.add(menuButton("6. Oxygen Saturation (SpO\u2082)", e -> performOxygen()));
        panel.add(menuButton("7. Cholesterol", e -> performCholesterol()));
        panel.add(menuButton("8. Overall Risk Summary", e -> displayOverallRisk()));
        panel.add(menuButton("0. Back", e -> showCard(CARD_MAIN)));

        return panel;
    }

    private void healthCheckupMenu() {
        if (!checkUser()) {
            return;
        }
        showCard(CARD_CHECKUP);
    }

    // ========================================
    // BMI
    // ========================================
    private void performBMI() {
        if (!checkUser()) {
            return;
        }

        try {
            double height = currentUser.getHeight();
            double weight = currentUser.getWeight();

            RiskResult result = checkupService.checkBMI(height, weight);

            log("\n========== BMI RESULT ==========");
            log("Height: " + height + " m");
            log("Weight: " + weight + " kg");
            result.display();

            checkupService.saveHealthRecord(
                    currentUser.getUserId(),
                    "BMI",
                    result.getValue(),
                    result.getRiskLevel()
            );

            log("BMI result saved successfully.");

        } catch (InvalidHealthDataException e) {
            log("Invalid Data: " + e.getMessage());
        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // BLOOD PRESSURE
    // ========================================
    private void performBP() {
        try {
            int systolic = readInt("Enter systolic BP: ");
            int diastolic = readInt("Enter diastolic BP: ");

            RiskResult result = checkupService.checkBloodPressure(systolic, diastolic);

            log("\n====== BLOOD PRESSURE RESULT ======");
            log("Systolic: " + systolic);
            log("Diastolic: " + diastolic);
            result.display();

            checkupService.saveHealthRecord(
                    currentUser.getUserId(),
                    "Blood Pressure",
                    result.getValue(),
                    result.getRiskLevel()
            );

            log("Blood pressure result saved successfully.");

        } catch (InvalidHealthDataException e) {
            log("Invalid Data: " + e.getMessage());
        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // SMOKING
    // ========================================
    private void performSmoking() {
        int intensity = readIntensity("Smoking intensity (0-4): ");
        int risk = calculateIntensityRisk(intensity);

        log("\nSmoking Risk Level: " + risk + "/4");
        log("Risk Percentage: " + (risk * 25) + "%");

        try {
            checkupService.saveHealthRecord(
                    currentUser.getUserId(),
                    "Smoking",
                    intensity,
                    risk
            );

            log("Smoking result saved successfully.");

        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // ALCOHOL
    // ========================================
    private void performAlcohol() {
        int intensity = readIntensity("Alcohol intensity (0-4): ");
        int risk = calculateIntensityRisk(intensity);

        log("\nAlcohol Risk Level: " + risk + "/4");
        log("Risk Percentage: " + (risk * 25) + "%");

        try {
            checkupService.saveHealthRecord(
                    currentUser.getUserId(),
                    "Alcohol",
                    intensity,
                    risk
            );

            log("Alcohol result saved successfully.");

        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // OTHER DRUGS
    // ========================================
    private void performOtherDrugs() {
        log("\nOther Drugs (Opioids, Synthetic Drugs, Inhalants, Cannabis, ...)");

        int intensity = readIntensity("Other drug intensity (0-4): ");
        int risk = calculateIntensityRisk(intensity);

        log("\nOther Drugs Risk Level: " + risk + "/4");
        log("Risk Percentage: " + (risk * 25) + "%");

        try {
            checkupService.saveHealthRecord(
                    currentUser.getUserId(),
                    "Other Drugs (Opioids, Synthetic Drugs, Inhalants, Cannabis, ...)",
                    intensity,
                    risk
            );

            log("Other drugs result saved successfully.");

        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // OXYGEN
    // ========================================
    private void performOxygen() {
        try {
            double oxygen = readDouble("Enter Oxygen Saturation (SpO\u2082 %): ");

            RiskResult result = checkupService.checkOxygen(oxygen);

            log("\n====== OXYGEN RESULT ======");
            result.display();

            checkupService.saveHealthRecord(
                    currentUser.getUserId(),
                    "Oxygen Saturation (SpO\u2082)",
                    result.getValue(),
                    result.getRiskLevel()
            );

            log("Oxygen result saved successfully.");

        } catch (InvalidHealthDataException e) {
            log("Invalid Data: " + e.getMessage());
        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // CHOLESTEROL
    // ========================================
    private void performCholesterol() {
        try {
            double cholesterol = readDouble("Enter total cholesterol (mg/dL): ");

            RiskResult result = checkupService.checkCholesterol(cholesterol);

            log("\n====== CHOLESTEROL RESULT ======");
            result.display();

            checkupService.saveHealthRecord(
                    currentUser.getUserId(),
                    "Cholesterol",
                    result.getValue(),
                    result.getRiskLevel()
            );

            log("Cholesterol result saved successfully.");

        } catch (InvalidHealthDataException e) {
            log("Invalid Data: " + e.getMessage());
        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // OVERALL RISK
    // ========================================
    private void displayOverallRisk() {

        if (!checkUser()) {
            return;
        }

        try {
            int[] risks = historyService.getLatestRiskLevels(currentUser.getUserId());

            int totalRisk = 0;

            for (int risk : risks) {
                totalRisk += risk;
            }

            double overallRisk = totalRisk / 7.0;
            double riskPercentage = (totalRisk / 28.0) * 100;

            log("\n========== OVERALL RISK ==========");

            log("BMI                  : " + risks[0] + "/4");
            log("Blood Pressure       : " + risks[1] + "/4");
            log("Smoking              : " + risks[2] + "/4");
            log("Alcohol              : " + risks[3] + "/4");
            log("Other Drugs          : " + risks[4] + "/4");
            log("Oxygen Saturation    : " + risks[5] + "/4");
            log("Cholesterol          : " + risks[6] + "/4");

            log("----------------------------------");

            log("Total Risk           : " + totalRisk + "/28");
            log(String.format("Overall Risk         : %.2f/4", overallRisk));
            log(String.format("Risk Percentage      : %.2f%%", riskPercentage));

            String status;

            if (riskPercentage == 0) {
                status = "No Risk";
            } else if (riskPercentage <= 25) {
                status = "Low Risk";
            } else if (riskPercentage <= 50) {
                status = "Moderate Risk";
            } else if (riskPercentage <= 75) {
                status = "High Risk";
            } else {
                status = "Very High Risk";
            }

            log("Overall Status       : " + status);
            log("==================================");

        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // MODULE 3 - HISTORY
    // ========================================

    private void healthHistory() {
        if (!checkUser()) {
            return;
        }

        try {
            historyService.displayHistory(currentUser.getUserId());
        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // MODULE 4 - TIPS
    // ========================================

    private JPanel buildTipsMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(sectionLabel("HEALTH TIPS"));

        panel.add(menuButton("1. BMI", e -> showTip("BMI")));
        panel.add(menuButton("2. Blood Pressure", e -> showTip("Blood Pressure")));
        panel.add(menuButton("3. Smoking", e -> showTip("Smoking")));
        panel.add(menuButton("4. Alcohol", e -> showTip("Alcohol")));
        panel.add(menuButton("5. Other Drugs (Opioids, Synthetic Drugs, Inhalants, Cannabis, ...)",
                e -> showTip("Other Drugs (Opioids, Synthetic Drugs, Inhalants, Cannabis, ...)")));
        panel.add(menuButton("6. Oxygen Saturation (SpO\u2082)", e -> showTip("Oxygen")));
        panel.add(menuButton("7. Cholesterol", e -> showTip("Cholesterol")));
        panel.add(menuButton("0. Back", e -> showCard(CARD_MAIN)));

        return panel;
    }

    private void healthTipsMenu() {
        if (!checkUser()) {
            return;
        }
        showCard(CARD_TIPS);
    }

    private void showTip(String category) {
        if (!checkUser()) {
            return;
        }

        try {
            int[] risks = historyService.getLatestRiskLevels(currentUser.getUserId());

            int risk = -1;

            if (category.equals("BMI")) {
                risk = risks[0];
            } else if (category.equals("Blood Pressure")) {
                risk = risks[1];
            } else if (category.equals("Smoking")) {
                risk = risks[2];
            } else if (category.equals("Alcohol")) {
                risk = risks[3];
            } else if (category.startsWith("Other Drugs")) {
                risk = risks[4];
            } else if (category.equals("Oxygen")) {
                risk = risks[5];
            } else if (category.equals("Cholesterol")) {
                risk = risks[6];
            }

            if (risk == -1) {
                log("No health record found for this category.");
                return;
            }

            log("\n========== " + category + " TIPS ==========");
            log("Current Risk Level: " + risk + "/4");
            log("Risk Percentage: " + (risk * 25) + "%");
            log("\nTip:");
            log(tipService.getTip(category, risk));
        } catch (DatabaseException e) {
            log("Database Error: " + e.getMessage());
        }
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    private boolean checkUser() {
        if (currentUser == null) {
            log("\nPlease create a user first.");
            return false;
        }
        return true;
    }

    private int readIntensity(String message) {
        int value;

        do {
            value = readInt(message);

            if (value < 0 || value > 4) {
                log("Please enter a value from 0 to 4.");
            }
        } while (value < 0 || value > 4);
        return value;
    }

    private int calculateIntensityRisk(int intensity) {
        return intensity;
    }

    // ---- GUI-backed replacements for the old Scanner-based input helpers ----
    // Same purpose and same retry-on-invalid-input behaviour as the
    // original CLI helpers; input now comes from a dialog box instead of
    // System.in. Cancelling a dialog behaves like entering "0" (back/no-op)
    // since the CLI had no equivalent of a cancel action.

    private String readString(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        return input == null ? "" : input;
    }

    private String readPassword(String message) {
        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(
                this,
                new Object[]{message, passwordField},
                "Password Required",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return new String(passwordField.getPassword());
        }
        return "";
    }

    private int readInt(String message) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, message);

            if (input == null) {
                return 0;
            }

            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                log("Please enter a valid number.");
            }
        }
    }

    private double readDouble(String message) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, message);

            if (input == null) {
                return 0;
            }

            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                log("Please enter a valid number.");
            }
        }
    }

    // ---- Small UI-building helpers ----

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return label;
    }

    private JButton menuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(500, 40));
        button.addActionListener(listener);
        return button;
    }

    /**
     * OutputStream that mirrors everything written to it into a JTextArea
     * (on the Swing event thread) while also passing it through to the
     * real underlying stream, so nothing that any existing class prints
     * via System.out is lost or changed - it just becomes visible in the
     * GUI as well.
     */
    private static class ConsoleOutputStream extends OutputStream {
        private final JTextArea textArea;
        private final PrintStream passThrough;
        private final StringBuilder buffer = new StringBuilder();

        ConsoleOutputStream(JTextArea textArea, PrintStream passThrough) {
            this.textArea = textArea;
            this.passThrough = passThrough;
        }

        @Override
        public void write(int b) {
            passThrough.write(b);
            buffer.append((char) b);

            if (b == '\n') {
                final String text = buffer.toString();
                buffer.setLength(0);
                SwingUtilities.invokeLater(() -> {
                    textArea.append(text);
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                });
            }
        }
    }

    // ========================================
    // ENTRY POINT
    // ========================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            QuickHealthCheck frame = new QuickHealthCheck();
            frame.setVisible(true);
        });
    }
}
