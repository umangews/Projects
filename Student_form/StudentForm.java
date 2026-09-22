import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Event-Based Student Registration Form
 *
 * Demonstrates the Java Event Delegation Model using:
 *  - ActionListener  → Button clicks, ComboBox selection
 *  - ItemListener    → Checkbox and RadioButton state changes
 *  - FocusListener   → Field focus (enter / exit)
 *  - KeyListener     → Real-time keystroke handling
 *  - MouseListener   → Table row hover & click
 *  - WindowListener  → Application close confirmation
 */
public class StudentForm extends JFrame
        implements ActionListener, ItemListener, FocusListener,
                   KeyListener, MouseListener, WindowListener {

    // ── Form Fields ──────────────────────────────────────────────
    private JTextField tfName, tfRoll, tfAge, tfEmail, tfSearch;
    private JComboBox<String> cbCourse, cbYear;
    private JRadioButton rbMale, rbFemale, rbOther;
    private ButtonGroup genderGroup;
    private JCheckBox chkJava, chkPython, chkSQL, chkML, chkWeb;
    private JTextArea taAddress;
    private JLabel lblCharCount, lblStatus;
    private JButton btnSubmit, btnClear, btnDelete, btnExport;

    // ── Table ─────────────────────────────────────────────────────
    private JTable table;
    private DefaultTableModel tableModel;

    // ── Colors / Fonts ────────────────────────────────────────────
    private static final Color PRIMARY   = new Color(63, 81, 181);
    private static final Color ACCENT    = new Color(255, 193, 7);
    private static final Color SUCCESS   = new Color(76, 175, 80);
    private static final Color DANGER    = new Color(244, 67, 54);
    private static final Color BG_LIGHT  = new Color(245, 247, 255);
    private static final Color CARD_BG   = Color.WHITE;
    private static final Font  TITLE_F   = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  LABEL_F   = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  FIELD_F   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  SMALL_F   = new Font("Segoe UI", Font.ITALIC, 11);

    // ── Data ──────────────────────────────────────────────────────
    private final List<String[]> studentData = new ArrayList<>();
    private int selectedRow = -1;

    // =============================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new StudentForm().setVisible(true);
        });
    }

    // =============================================================
    public StudentForm() {
        setTitle("📚 Event-Based Student Registration System");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // handled by WindowListener
        addWindowListener(this);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT);
        setLayout(new BorderLayout(10, 10));

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    // ── Header ────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(16, 24, 16, 24));

        JLabel title = new JLabel("🎓 Student Registration Portal", SwingConstants.LEFT);
        title.setFont(TITLE_F);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Event Delegation Model Demo — Java AWT/Swing", SwingConstants.RIGHT);
        subtitle.setFont(SMALL_F);
        subtitle.setForeground(new Color(200, 210, 255));

        header.add(title,    BorderLayout.WEST);
        header.add(subtitle, BorderLayout.EAST);
        return header;
    }

    // ── Center split: Form | Table ────────────────────────────────
    private JSplitPane buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(), buildTablePanel());
        split.setDividerLocation(440);
        split.setDividerSize(6);
        split.setContinuousLayout(true);
        split.setBorder(new EmptyBorder(10, 10, 10, 10));
        split.setBackground(BG_LIGHT);
        return split;
    }

    // ── Form Panel ────────────────────────────────────────────────
    private JScrollPane buildFormPanel() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(BG_LIGHT);
        form.setBorder(new EmptyBorder(0, 0, 0, 6));

        form.add(buildCard("👤  Personal Information",  buildPersonalSection()));
        form.add(Box.createVerticalStrut(10));
        form.add(buildCard("🎓  Academic Details",       buildAcademicSection()));
        form.add(Box.createVerticalStrut(10));
        form.add(buildCard("💻  Skills / Subjects",      buildSkillsSection()));
        form.add(Box.createVerticalStrut(10));
        form.add(buildCard("📍  Address",                buildAddressSection()));
        form.add(Box.createVerticalStrut(10));
        form.add(buildButtonBar());

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        return scroll;
    }

    private JPanel buildCard(String title, JPanel content) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 240), 1, true),
                new EmptyBorder(12, 16, 14, 16)));

        JLabel lbl = new JLabel(title);
        lbl.setFont(LABEL_F);
        lbl.setForeground(PRIMARY);
        lbl.setBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 225, 240)));

        card.add(lbl,     BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // ── Personal Section ─────────────────────────────────────────
    private JPanel buildPersonalSection() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        GridBagConstraints g = gbc();

        tfName  = styledField("Enter full name");
        tfRoll  = styledField("e.g. 23CS001");
        tfAge   = styledField("18–30");
        tfEmail = styledField("student@example.com");

        tfName.addFocusListener(this);  tfName.addKeyListener(this);
        tfRoll.addFocusListener(this);  tfRoll.addKeyListener(this);
        tfAge.addFocusListener(this);   tfAge.addKeyListener(this);
        tfEmail.addFocusListener(this); tfEmail.addKeyListener(this);

        addRow(p, g, "Full Name *",   tfName,  0);
        addRow(p, g, "Roll Number *", tfRoll,  1);
        addRow(p, g, "Age *",         tfAge,   2);
        addRow(p, g, "Email *",       tfEmail, 3);

        // Gender row
        g.gridy = 4; g.gridx = 0; g.weightx = 0;
        p.add(label("Gender *"), g);

        rbMale   = radioBtn("Male");
        rbFemale = radioBtn("Female");
        rbOther  = radioBtn("Other");
        rbMale.addItemListener(this);
        rbFemale.addItemListener(this);
        rbOther.addItemListener(this);

        genderGroup = new ButtonGroup();
        genderGroup.add(rbMale);
        genderGroup.add(rbFemale);
        genderGroup.add(rbOther);
        rbMale.setSelected(true);

        JPanel gp = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        gp.setBackground(CARD_BG);
        gp.add(rbMale); gp.add(rbFemale); gp.add(rbOther);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        p.add(gp, g);

        return p;
    }

    // ── Academic Section ─────────────────────────────────────────
    private JPanel buildAcademicSection() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CARD_BG);
        GridBagConstraints g = gbc();

        String[] courses = {"-- Select Course --", "B.Tech CSE", "B.Tech IT",
                "BCA", "MCA", "B.Sc CS", "MBA (IT)"};
        String[] years   = {"-- Select Year --", "1st Year", "2nd Year",
                "3rd Year", "4th Year"};

        cbCourse = new JComboBox<>(courses);
        cbYear   = new JComboBox<>(years);
        styleCombo(cbCourse); styleCombo(cbYear);
        cbCourse.addActionListener(this);
        cbCourse.addItemListener(this);
        cbYear.addActionListener(this);

        addRow(p, g, "Course *",      cbCourse, 0);
        addRow(p, g, "Academic Year", cbYear,   1);
        return p;
    }

    // ── Skills Section ────────────────────────────────────────────
    private JPanel buildSkillsSection() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 4));
        p.setBackground(CARD_BG);

        chkJava   = styledCheck("Java");
        chkPython = styledCheck("Python");
        chkSQL    = styledCheck("SQL");
        chkML     = styledCheck("Machine Learning");
        chkWeb    = styledCheck("Web Dev");

        for (JCheckBox cb : new JCheckBox[]{chkJava, chkPython, chkSQL, chkML, chkWeb}) {
            cb.addItemListener(this);
            p.add(cb);
        }
        return p;
    }

    // ── Address Section ───────────────────────────────────────────
    private JPanel buildAddressSection() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(CARD_BG);

        taAddress = new JTextArea(3, 20);
        taAddress.setFont(FIELD_F);
        taAddress.setLineWrap(true);
        taAddress.setWrapStyleWord(true);
        taAddress.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 190, 210), 1, true),
                new EmptyBorder(6, 8, 6, 8)));
        taAddress.addKeyListener(this);

        lblCharCount = new JLabel("0 / 200 characters");
        lblCharCount.setFont(SMALL_F);
        lblCharCount.setForeground(Color.GRAY);

        p.add(new JScrollPane(taAddress), BorderLayout.CENTER);
        p.add(lblCharCount, BorderLayout.SOUTH);
        return p;
    }

    // ── Button Bar ────────────────────────────────────────────────
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        bar.setBackground(BG_LIGHT);

        btnSubmit = actionBtn("✔  Submit",  SUCCESS, Color.WHITE);
        btnClear  = actionBtn("✖  Clear",   DANGER,  Color.WHITE);
        btnDelete = actionBtn("🗑  Delete",  new Color(96, 125, 139), Color.WHITE);
        btnExport = actionBtn("⬇  Export",  PRIMARY, Color.WHITE);

        btnSubmit.addActionListener(this);
        btnClear.addActionListener(this);
        btnDelete.addActionListener(this);
        btnExport.addActionListener(this);

        bar.add(btnSubmit);
        bar.add(btnClear);
        bar.add(btnDelete);
        bar.add(btnExport);
        return bar;
    }

    // ── Table Panel ───────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(new EmptyBorder(0, 6, 0, 0));

        // Search bar
        JPanel searchRow = new JPanel(new BorderLayout(6, 0));
        searchRow.setBackground(BG_LIGHT);
        tfSearch = styledField("🔍  Search by name or roll...");
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterTable(); }
        });
        JLabel sLbl = new JLabel("Search:");
        sLbl.setFont(LABEL_F);
        searchRow.add(sLbl,     BorderLayout.WEST);
        searchRow.add(tfSearch, BorderLayout.CENTER);

        // Table
        String[] cols = {"#", "Name", "Roll No", "Age", "Email",
                "Gender", "Course", "Year", "Skills"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(197, 202, 233));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 242, 255));
                    c.setForeground(Color.DARK_GRAY);
                }
                return c;
            }
        };

        table.setFont(FIELD_F);
        table.setRowHeight(26);
        table.setGridColor(new Color(220, 225, 240));
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Column widths
        int[] widths = {30, 130, 80, 40, 150, 60, 110, 70, 160};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.addMouseListener(this);

        JLabel tTitle = new JLabel("📋  Registered Students");
        tTitle.setFont(LABEL_F);
        tTitle.setForeground(PRIMARY);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(new Color(200, 210, 230), 1, true));

        panel.add(tTitle,   BorderLayout.NORTH);
        panel.add(searchRow, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(sp,        BorderLayout.CENTER);

        // Tweak layout order
        panel.removeAll();
        panel.add(tTitle,    BorderLayout.NORTH);

        JPanel top = new JPanel(new BorderLayout(0, 6));
        top.setBackground(BG_LIGHT);
        top.add(tTitle,    BorderLayout.NORTH);
        top.add(searchRow, BorderLayout.CENTER);
        panel.add(top, BorderLayout.NORTH);
        panel.add(sp,  BorderLayout.CENTER);

        return panel;
    }

    // ── Status Bar ────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 4));
        bar.setBackground(new Color(232, 234, 246));
        bar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(200, 210, 230)));
        lblStatus = new JLabel("ℹ  Welcome! Fill in the form and click Submit.");
        lblStatus.setFont(SMALL_F);
        lblStatus.setForeground(new Color(63, 81, 181));
        bar.add(lblStatus);
        return bar;
    }

    // =============================================================
    // EVENT HANDLERS
    // =============================================================

    // ── ActionListener ────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btnSubmit) {
            handleSubmit();
        } else if (src == btnClear) {
            handleClear();
        } else if (src == btnDelete) {
            handleDelete();
        } else if (src == btnExport) {
            handleExport();
        } else if (src == cbCourse) {
            String sel = (String) cbCourse.getSelectedItem();
            if (sel != null && !sel.startsWith("--"))
                setStatus("ℹ  Course selected: " + sel);
        } else if (src == cbYear) {
            String sel = (String) cbYear.getSelectedItem();
            if (sel != null && !sel.startsWith("--"))
                setStatus("ℹ  Year selected: " + sel);
        }
    }

    // ── ItemListener ─────────────────────────────────────────────
    @Override
    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        int    st  = e.getStateChange();

        if (src == rbMale || src == rbFemale || src == rbOther) {
            if (st == ItemEvent.SELECTED) {
                JRadioButton rb = (JRadioButton) src;
                setStatus("✔  Gender selected: " + rb.getText());
            }
        } else if (src instanceof JCheckBox) {
            JCheckBox cb = (JCheckBox) src;
            setStatus((st == ItemEvent.SELECTED ? "✔  Added skill: " : "✖  Removed skill: ")
                      + cb.getText());
        } else if (src == cbCourse) {
            // handled by ActionListener above
        }
    }

    // ── FocusListener ────────────────────────────────────────────
    @Override
    public void focusGained(FocusEvent e) {
        JTextField tf = (JTextField) e.getSource();
        tf.setBorder(new CompoundBorder(
                new LineBorder(PRIMARY, 2, true),
                new EmptyBorder(4, 8, 4, 8)));
        if (tf == tfName)  setStatus("✏  Enter student's full name.");
        if (tf == tfRoll)  setStatus("✏  Enter unique roll number.");
        if (tf == tfAge)   setStatus("✏  Enter age (numeric, 10–60).");
        if (tf == tfEmail) setStatus("✏  Enter a valid email address.");
    }

    @Override
    public void focusLost(FocusEvent e) {
        JTextField tf = (JTextField) e.getSource();
        tf.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 190, 210), 1, true),
                new EmptyBorder(5, 8, 5, 8)));
        validateField(tf);
    }

    // ── KeyListener ──────────────────────────────────────────────
    @Override
    public void keyReleased(KeyEvent e) {
        Object src = e.getSource();
        if (src == taAddress) {
            int len = taAddress.getText().length();
            if (len > 200) {
                taAddress.setText(taAddress.getText().substring(0, 200));
                len = 200;
            }
            lblCharCount.setText(len + " / 200 characters");
            lblCharCount.setForeground(len > 160 ? DANGER : Color.GRAY);
        } else if (src == tfAge) {
            String txt = tfAge.getText().replaceAll("[^0-9]", "");
            if (!tfAge.getText().equals(txt)) tfAge.setText(txt);
        }
    }

    @Override public void keyPressed(KeyEvent e)  {}
    @Override public void keyTyped(KeyEvent e)    {}

    // ── MouseListener (Table) ────────────────────────────────────
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == table) {
            selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                populateFormFromRow(selectedRow);
                setStatus("📌  Row " + (selectedRow + 1) + " selected — edit fields and re-submit to update.");
            }
        }
    }
    @Override public void mouseEntered(MouseEvent e) {
        if (e.getSource() == table) table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    @Override public void mouseExited(MouseEvent e) {
        if (e.getSource() == table) table.setCursor(Cursor.getDefaultCursor());
    }
    @Override public void mousePressed(MouseEvent e)  {}
    @Override public void mouseReleased(MouseEvent e) {}

    // ── WindowListener ────────────────────────────────────────────
    @Override
    public void windowClosing(WindowEvent e) {
        int ans = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?\nUnsaved data will be lost.",
                "Confirm Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.YES_OPTION) dispose();
    }
    @Override public void windowOpened(WindowEvent e)      {}
    @Override public void windowClosed(WindowEvent e)      {}
    @Override public void windowIconified(WindowEvent e)   {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowActivated(WindowEvent e)   {}
    @Override public void windowDeactivated(WindowEvent e) {}

    // =============================================================
    // BUSINESS LOGIC
    // =============================================================

    private void handleSubmit() {
        if (!validateAll()) return;

        String[] row = {
            String.valueOf(studentData.size() + 1),
            tfName.getText().trim(),
            tfRoll.getText().trim().toUpperCase(),
            tfAge.getText().trim(),
            tfEmail.getText().trim(),
            getSelectedGender(),
            (String) cbCourse.getSelectedItem(),
            (String) cbYear.getSelectedItem(),
            getSelectedSkills()
        };

        if (selectedRow >= 0) {
            // Update existing row
            row[0] = (String) tableModel.getValueAt(selectedRow, 0);
            studentData.set(selectedRow, row);
            for (int c = 0; c < row.length; c++)
                tableModel.setValueAt(row[c], selectedRow, c);
            setStatus("✅  Record #" + row[0] + " updated successfully.");
            selectedRow = -1;
        } else {
            studentData.add(row);
            tableModel.addRow(row);
            setStatus("✅  Student \"" + row[1] + "\" registered (Total: " + studentData.size() + ").");
        }
        handleClear();
    }

    private void handleClear() {
        tfName.setText(""); tfRoll.setText(""); tfAge.setText(""); tfEmail.setText("");
        taAddress.setText(""); lblCharCount.setText("0 / 200 characters");
        cbCourse.setSelectedIndex(0); cbYear.setSelectedIndex(0);
        rbMale.setSelected(true);
        for (JCheckBox cb : new JCheckBox[]{chkJava, chkPython, chkSQL, chkML, chkWeb})
            cb.setSelected(false);
        selectedRow = -1;
        tfName.requestFocus();
        setStatus("🔄  Form cleared. Ready for new entry.");
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Please select a row to delete.",
                "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int ans = JOptionPane.showConfirmDialog(this,
                "Delete record for \"" + tableModel.getValueAt(row, 1) + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.YES_OPTION) {
            studentData.remove(row);
            tableModel.removeRow(row);
            renumberTable();
            handleClear();
            setStatus("🗑  Record deleted.");
        }
    }

    private void handleExport() {
        if (studentData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data to export.", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#,Name,Roll,Age,Email,Gender,Course,Year,Skills\n");
        for (String[] r : studentData) sb.append(String.join(",", r)).append("\n");
        JOptionPane.showMessageDialog(this,
                new JTextArea(sb.toString()),
                "📋 Exported Data (CSV Preview)", JOptionPane.INFORMATION_MESSAGE);
        setStatus("⬇  Export preview shown (" + studentData.size() + " records).");
    }

    private boolean validateAll() {
        if (tfName.getText().trim().isEmpty())  { err(tfName,  "Name cannot be empty.");         return false; }
        if (tfRoll.getText().trim().isEmpty())  { err(tfRoll,  "Roll number cannot be empty.");  return false; }
        if (tfAge.getText().trim().isEmpty())   { err(tfAge,   "Age cannot be empty.");           return false; }
        int age;
        try { age = Integer.parseInt(tfAge.getText().trim()); }
        catch (NumberFormatException ex) { err(tfAge, "Age must be numeric."); return false; }
        if (age < 10 || age > 60)               { err(tfAge, "Age must be between 10 and 60."); return false; }
        if (!tfEmail.getText().contains("@"))   { err(tfEmail, "Enter a valid email.");          return false; }
        if (cbCourse.getSelectedIndex() == 0)  { JOptionPane.showMessageDialog(this, "Please select a course.", "Validation", JOptionPane.WARNING_MESSAGE); return false; }
        return true;
    }

    private void validateField(JTextField tf) {
        if (tf == tfAge && !tf.getText().isEmpty()) {
            try {
                int v = Integer.parseInt(tf.getText());
                if (v < 10 || v > 60) err(tf, "Age must be 10–60.");
            } catch (NumberFormatException ex) { err(tf, "Age must be numeric."); }
        }
        if (tf == tfEmail && !tf.getText().isEmpty() && !tf.getText().contains("@"))
            err(tf, "Invalid email.");
    }

    private void populateFormFromRow(int row) {
        tfName.setText((String) tableModel.getValueAt(row, 1));
        tfRoll.setText((String) tableModel.getValueAt(row, 2));
        tfAge.setText ((String) tableModel.getValueAt(row, 3));
        tfEmail.setText((String) tableModel.getValueAt(row, 4));
        String gender = (String) tableModel.getValueAt(row, 5);
        if ("Male".equals(gender))   rbMale.setSelected(true);
        else if ("Female".equals(gender)) rbFemale.setSelected(true);
        else rbOther.setSelected(true);
        cbCourse.setSelectedItem(tableModel.getValueAt(row, 6));
        cbYear.setSelectedItem(tableModel.getValueAt(row, 7));
        String skills = (String) tableModel.getValueAt(row, 8);
        chkJava.setSelected(skills.contains("Java"));
        chkPython.setSelected(skills.contains("Python"));
        chkSQL.setSelected(skills.contains("SQL"));
        chkML.setSelected(skills.contains("Machine Learning"));
        chkWeb.setSelected(skills.contains("Web Dev"));
    }

    private void filterTable() {
        String q = tfSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        for (String[] r : studentData) {
            if (q.isEmpty() || r[1].toLowerCase().contains(q) || r[2].toLowerCase().contains(q))
                tableModel.addRow(r);
        }
    }

    private void renumberTable() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(String.valueOf(i + 1), i, 0);
            studentData.get(i)[0] = String.valueOf(i + 1);
        }
    }

    private String getSelectedGender() {
        if (rbFemale.isSelected()) return "Female";
        if (rbOther.isSelected())  return "Other";
        return "Male";
    }

    private String getSelectedSkills() {
        List<String> s = new ArrayList<>();
        if (chkJava.isSelected())   s.add("Java");
        if (chkPython.isSelected()) s.add("Python");
        if (chkSQL.isSelected())    s.add("SQL");
        if (chkML.isSelected())     s.add("Machine Learning");
        if (chkWeb.isSelected())    s.add("Web Dev");
        return s.isEmpty() ? "None" : String.join(", ", s);
    }

    private void err(JTextField tf, String msg) {
        tf.setBorder(new CompoundBorder(new LineBorder(DANGER, 2, true), new EmptyBorder(4, 8, 4, 8)));
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
        tf.requestFocus();
    }

    private void setStatus(String msg) { if (lblStatus != null) lblStatus.setText(msg); }

    // =============================================================
    // UI HELPERS
    // =============================================================

    private JTextField styledField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(FIELD_F);
        tf.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 190, 210), 1, true),
                new EmptyBorder(5, 8, 5, 8)));
        tf.setToolTipText(placeholder);
        return tf;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(LABEL_F);
        l.setForeground(new Color(60, 70, 100));
        return l;
    }

    private JRadioButton radioBtn(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(FIELD_F);
        rb.setBackground(CARD_BG);
        return rb;
    }

    private JCheckBox styledCheck(String text) {
        JCheckBox cb = new JCheckBox(text);
        cb.setFont(FIELD_F);
        cb.setBackground(CARD_BG);
        return cb;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(FIELD_F);
        cb.setBackground(Color.WHITE);
    }

    private JButton actionBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(bg.darker(), 1, true),
                new EmptyBorder(8, 18, 8, 18)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 4, 5, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        return g;
    }

    private void addRow(JPanel p, GridBagConstraints g,
                        String labelText, JComponent field, int row) {
        g.gridy = row; g.gridx = 0; g.weightx = 0;
        p.add(label(labelText), g);
        g.gridx = 1; g.weightx = 1;
        p.add(field, g);
    }
}
