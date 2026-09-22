import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StudentForm extends JFrame implements ActionListener, ItemListener {

    // ---- form fields (components) ----
    private JTextField nameField, rollField, emailField;
    private JComboBox<String> courseBox;
    private JRadioButton maleBtn, femaleBtn, otherBtn;
    private ButtonGroup genderGroup;
    private JCheckBox hobbyReading, hobbySports, hobbyMusic, hobbyCoding;
    private JButton submitBtn, resetBtn, exitBtn;
    private JTextArea outputArea;

    public StudentForm() {
        setTitle("Student Registration Form - Event Delegation Model Demo");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildOutputPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ---------------------------------------------------------------
    // Build the input form
    // ---------------------------------------------------------------
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Name
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(18);
        gbc.gridx = 1; panel.add(nameField, gbc);
        row++;

        // Roll Number
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Roll No:"), gbc);
        rollField = new JTextField(18);
        gbc.gridx = 1; panel.add(rollField, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(18);
        gbc.gridx = 1; panel.add(emailField, gbc);
        row++;

        // Course (JComboBox -> ItemListener)
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Course:"), gbc);
        courseBox = new JComboBox<>(new String[]{"Select...", "B.Tech CSE", "B.Tech IT", "BCA", "MCA", "B.Sc CS"});
        courseBox.addItemListener(this);   // register listener (delegation)
        gbc.gridx = 1; panel.add(courseBox, gbc);
        row++;

        // Gender (JRadioButton -> ActionListener)
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Gender:"), gbc);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        maleBtn = new JRadioButton("Male");
        femaleBtn = new JRadioButton("Female");
        otherBtn = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);
        genderGroup.add(otherBtn);
        maleBtn.addActionListener(this);
        femaleBtn.addActionListener(this);
        otherBtn.addActionListener(this);
        genderPanel.add(maleBtn);
        genderPanel.add(femaleBtn);
        genderPanel.add(otherBtn);
        gbc.gridx = 1; panel.add(genderPanel, gbc);
        row++;

        // Hobbies (JCheckBox -> ItemListener)
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Hobbies:"), gbc);
        JPanel hobbyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        hobbyReading = new JCheckBox("Reading");
        hobbySports = new JCheckBox("Sports");
        hobbyMusic = new JCheckBox("Music");
        hobbyCoding = new JCheckBox("Coding");
        hobbyReading.addItemListener(this);
        hobbySports.addItemListener(this);
        hobbyMusic.addItemListener(this);
        hobbyCoding.addItemListener(this);
        hobbyPanel.add(hobbyReading);
        hobbyPanel.add(hobbySports);
        hobbyPanel.add(hobbyMusic);
        hobbyPanel.add(hobbyCoding);
        gbc.gridx = 1; panel.add(hobbyPanel, gbc);

        return panel;
    }

    private JScrollPane buildOutputPanel() {
        outputArea = new JTextArea(12, 50);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createTitledBorder("Form Output / Event Log"));
        return new JScrollPane(outputArea);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        submitBtn = new JButton("Submit");
        resetBtn = new JButton("Reset");
        exitBtn = new JButton("Exit");

        // Register 'this' as the listener for each button.
        // Clicking any of these delegates handling to actionPerformed().
        submitBtn.addActionListener(this);
        resetBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        panel.add(submitBtn);
        panel.add(resetBtn);
        panel.add(exitBtn);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();   // identify which component fired the event

        if (source == submitBtn) {
            handleSubmit();
        } else if (source == resetBtn) {
            handleReset();
            log("Form has been reset.");
        } else if (source == exitBtn) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (source == maleBtn || source == femaleBtn || source == otherBtn) {
            JRadioButton rb = (JRadioButton) source;
            log("Gender selected: " + rb.getText());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();

        if (source == courseBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                log("Course selected: " + courseBox.getSelectedItem());
            }
        } else if (source instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) source;
            String state = (e.getStateChange() == ItemEvent.SELECTED) ? "checked" : "unchecked";
            log("Hobby '" + cb.getText() + "' " + state);
        }
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String email = emailField.getText().trim();
        String course = (String) courseBox.getSelectedItem();

        if (name.isEmpty() || roll.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Roll No are required!",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (course == null || course.equals("Select...")) {
            JOptionPane.showMessageDialog(this, "Please select a course!",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gender = genderGroup.getSelection() != null ? getSelectedGender() : "Not specified";
        String hobbies = getSelectedHobbies();

        StringBuilder sb = new StringBuilder();
        sb.append("----- Student Registered -----\n");
        sb.append("Name    : ").append(name).append("\n");
        sb.append("Roll No : ").append(roll).append("\n");
        sb.append("Email   : ").append(email).append("\n");
        sb.append("Course  : ").append(course).append("\n");
        sb.append("Gender  : ").append(gender).append("\n");
        sb.append("Hobbies : ").append(hobbies.isEmpty() ? "None" : hobbies).append("\n");
        sb.append("-------------------------------\n");

        log(sb.toString());
    }

    private String getSelectedGender() {
        if (maleBtn.isSelected()) return "Male";
        if (femaleBtn.isSelected()) return "Female";
        if (otherBtn.isSelected()) return "Other";
        return "Not specified";
    }

    private String getSelectedHobbies() {
        StringBuilder sb = new StringBuilder();
        if (hobbyReading.isSelected()) sb.append("Reading, ");
        if (hobbySports.isSelected()) sb.append("Sports, ");
        if (hobbyMusic.isSelected()) sb.append("Music, ");
        if (hobbyCoding.isSelected()) sb.append("Coding, ");
        if (sb.length() > 0) sb.setLength(sb.length() - 2); // trim trailing ", "
        return sb.toString();
    }

    private void handleReset() {
        nameField.setText("");
        rollField.setText("");
        emailField.setText("");
        courseBox.setSelectedIndex(0);
        genderGroup.clearSelection();
        hobbyReading.setSelected(false);
        hobbySports.setSelected(false);
        hobbyMusic.setSelected(false);
        hobbyCoding.setSelected(false);
    }

    private void log(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread (best practice for Swing)
        SwingUtilities.invokeLater(StudentForm::new);
    }
}
