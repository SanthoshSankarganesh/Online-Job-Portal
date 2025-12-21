import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class JobPortalApp extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private User currentUser;

    // --- PROFESSIONAL THEME ---
    private final Color BG_COLOR = new Color(24, 26, 31);       // Deep Dark Grey
    private final Color PANEL_COLOR = new Color(32, 34, 40);    // Lighter Grey for Cards
    private final Color ACCENT_COLOR = new Color(0, 184, 148);  // Professional Emerald
    private final Color TEXT_PRIMARY = new Color(223, 230, 233);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 32); // Bigger Header
    private final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public JobPortalApp() {
        setTitle("Enterprise Job Portal | v2.0");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");

        add(mainPanel);
        setVisible(true);
    }

    // ================= 1. LOGIN SCREEN (FIXED LAYOUT) =================
    private JPanel createLoginPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(BG_COLOR);

        JPanel card = createCardPanel();
        card.setLayout(new GridBagLayout()); // Stack components vertically inside the card

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20); // Spacing around elements
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // 1. Big Title at Top
        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setFont(HEADER_FONT);
        title.setForeground(TEXT_PRIMARY);

        // 2. Inputs in Middle
        JTextField email = new ModernField("Email Address");
        JPasswordField pass = new ModernPasswordField();

        // 3. Buttons at Bottom
        JButton loginBtn = new RoundedButton("SECURE LOGIN", ACCENT_COLOR);
        JButton regBtn = new RoundedButton("CREATE ACCOUNT", new Color(99, 110, 114));

        // Add to Card with stacking constraints
        gbc.gridy = 0; card.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(5, 20, 15, 20); // Tighter spacing for inputs
        card.add(email, gbc);

        gbc.gridy = 2;
        card.add(pass, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(25, 20, 10, 20); // Gap before buttons
        card.add(loginBtn, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 20, 20, 20);
        card.add(regBtn, gbc);

        container.add(card);

        // Actions
        loginBtn.addActionListener(e -> {
            String p = new String(pass.getPassword());
            currentUser = DatabaseService.login(email.getText(), p);
            if (currentUser != null) loadDashboard(currentUser);
            else JOptionPane.showMessageDialog(this, "Invalid Credentials", "Access Denied", JOptionPane.ERROR_MESSAGE);
        });

        regBtn.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));

        return container;
    }

    // ================= 2. REGISTER SCREEN =================
    private JPanel createRegisterPanel() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(BG_COLOR);

        JPanel card = createCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel title = new JLabel("Join the Network", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(ACCENT_COLOR);

        JTextField name = new ModernField("Full Name");
        JTextField email = new ModernField("Email Address");
        JPasswordField pass = new ModernPasswordField();

        JComboBox<String> roleBox = new JComboBox<>(new String[]{"JOB_SEEKER", "EMPLOYER"});
        styleComboBox(roleBox);

        JTextField extra = new ModernField("Skills (Seeker) / Company (Employer)");

        JButton submit = new RoundedButton("REGISTER USER", ACCENT_COLOR);
        JButton back = new RoundedButton("BACK TO LOGIN", new Color(99, 110, 114));

        gbc.gridy=0; card.add(title, gbc);
        gbc.gridy=1; card.add(name, gbc);
        gbc.gridy=2; card.add(email, gbc);
        gbc.gridy=3; card.add(pass, gbc);
        gbc.gridy=4; card.add(roleBox, gbc);
        gbc.gridy=5; card.add(extra, gbc);
        gbc.gridy=6; gbc.insets = new Insets(20,20,5,20); card.add(submit, gbc);
        gbc.gridy=7; gbc.insets = new Insets(5,20,20,20); card.add(back, gbc);

        container.add(card);

        submit.addActionListener(e -> {
            if(DatabaseService.registerUser(name.getText(), email.getText(), new String(pass.getPassword()),
                    (String)roleBox.getSelectedItem(), extra.getText())) {
                JOptionPane.showMessageDialog(this, "Account Created! Login Now.");
                cardLayout.show(mainPanel, "LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed. Check inputs.");
            }
        });
        back.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        return container;
    }

    // ================= 3. DASHBOARDS =================
    private void loadDashboard(User user) {
        if (user instanceof Employer) {
            mainPanel.add(createEmployerDashboard(), "DASH_EMP");
            cardLayout.show(mainPanel, "DASH_EMP");
        } else if (user instanceof JobSeeker) {
            mainPanel.add(createSeekerDashboard(), "DASH_SEEK");
            cardLayout.show(mainPanel, "DASH_SEEK");
        } else {
            mainPanel.add(createAdminDashboard(), "DASH_ADMIN");
            cardLayout.show(mainPanel, "DASH_ADMIN");
        }
    }

    private JPanel createSeekerDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_COLOR);
        p.add(createNavBar("CANDIDATE PORTAL"), BorderLayout.NORTH);

        JTabbedPane tabs = createModernTabs();

        // Tab 1: Search
        JPanel searchTab = new JPanel(new BorderLayout());
        searchTab.setBackground(BG_COLOR);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Job ID", "Role Title", "Description", "Salary ($)"}, 0);
        JTable table = createModernTable(model);

        JButton apply = new RoundedButton("APPLY FOR SELECTED", ACCENT_COLOR);
        apply.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) {
                int id = (int)model.getValueAt(r,0);
                if(DatabaseService.applyJob(id, currentUser.getId())) JOptionPane.showMessageDialog(this, "Application Sent Successfully!");
                else JOptionPane.showMessageDialog(this, "Already Applied or Error.");
            }
        });

        // Load Data
        List<Job> jobs = DatabaseService.getAllJobs();
        for(Job j : jobs) model.addRow(new Object[]{j.id, j.title, j.description, j.salary});

        searchTab.add(new JScrollPane(table), BorderLayout.CENTER);
        searchTab.add(apply, BorderLayout.SOUTH);

        // Tab 2: Status
        JPanel statusTab = new JPanel(new BorderLayout());
        statusTab.setBackground(BG_COLOR);
        DefaultTableModel statModel = new DefaultTableModel(new String[]{"ID", "Job Title", "Company", "Status"}, 0);
        JTable statTable = createModernTable(statModel);

        JButton refresh = new RoundedButton("REFRESH STATUS", new Color(108, 92, 231));
        refresh.addActionListener(e -> {
            statModel.setRowCount(0);
            for(Application a : DatabaseService.getSeekerApplications(currentUser.getId()))
                statModel.addRow(new Object[]{a.id, a.jobTitle, a.applicantName, a.status});
        });
        refresh.doClick();

        statusTab.add(new JScrollPane(statTable), BorderLayout.CENTER);
        statusTab.add(refresh, BorderLayout.SOUTH);

        tabs.addTab("EXPLORE JOBS", searchTab);
        tabs.addTab("MY APPLICATIONS", statusTab);
        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    private JPanel createEmployerDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_COLOR);
        p.add(createNavBar("RECRUITER DASHBOARD"), BorderLayout.NORTH);

        JTabbedPane tabs = createModernTabs();

        // Post Job
        JPanel postPanel = createCardPanel();
        postPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10,0,10,0); g.fill = GridBagConstraints.HORIZONTAL; g.gridx=0;

        JTextField title = new ModernField("Job Title");
        JTextField salary = new ModernField("Salary (e.g. 90000)");
        JTextArea desc = new JTextArea(5, 20);
        desc.setBackground(new Color(45, 52, 54)); desc.setForeground(TEXT_PRIMARY); desc.setBorder(new LineBorder(Color.GRAY));
        JButton postBtn = new RoundedButton("PUBLISH VACANCY", ACCENT_COLOR);

        postPanel.add(new JLabel("Create New Position"), g);
        g.gridy=1; postPanel.add(title, g);
        g.gridy=2; postPanel.add(salary, g);
        g.gridy=3; postPanel.add(new JScrollPane(desc), g);
        g.gridy=4; postPanel.add(postBtn, g);

        postBtn.addActionListener(e -> {
            try {
                if(DatabaseService.postJob(currentUser.getId(), title.getText(), desc.getText(), Double.parseDouble(salary.getText())))
                    JOptionPane.showMessageDialog(this, "Job Posted Successfully!");
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Invalid Data"); }
        });

        // Manage
        JPanel managePanel = new JPanel(new BorderLayout());
        managePanel.setBackground(BG_COLOR);
        DefaultTableModel appModel = new DefaultTableModel(new String[]{"App ID", "Job", "Candidate", "Status"}, 0);
        JTable appTable = createModernTable(appModel);

        JPanel btnP = new JPanel(new FlowLayout()); btnP.setBackground(BG_COLOR);
        JButton accept = new RoundedButton("ACCEPT", ACCENT_COLOR);
        JButton reject = new RoundedButton("REJECT", new Color(214, 48, 49));

        ActionListener update = (evt) -> {
            int r = appTable.getSelectedRow();
            if(r!=-1) {
                DatabaseService.updateApplicationStatus((int)appModel.getValueAt(r,0), evt.getActionCommand());
                // Simple refresh logic
                appModel.setRowCount(0);
                for(Application a : DatabaseService.getEmployerApplications(currentUser.getId()))
                    appModel.addRow(new Object[]{a.id, a.jobTitle, a.applicantName, a.status});
            }
        };
        accept.setActionCommand("ACCEPTED"); accept.addActionListener(update);
        reject.setActionCommand("REJECTED"); reject.addActionListener(update);

        // Initial load
        for(Application a : DatabaseService.getEmployerApplications(currentUser.getId()))
            appModel.addRow(new Object[]{a.id, a.jobTitle, a.applicantName, a.status});

        btnP.add(accept); btnP.add(reject);
        managePanel.add(new JScrollPane(appTable), BorderLayout.CENTER);
        managePanel.add(btnP, BorderLayout.SOUTH);

        // Center the form
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(BG_COLOR);
        formWrapper.add(postPanel);

        tabs.addTab("POST JOBS", formWrapper);
        tabs.addTab("MANAGE APPLICANTS", managePanel);
        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    private JPanel createAdminDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_COLOR);
        p.add(createNavBar("SYSTEM ADMIN"), BorderLayout.NORTH);
        JLabel l = new JLabel("System Operational. Logs monitored.", SwingConstants.CENTER);
        l.setForeground(TEXT_PRIMARY); l.setFont(HEADER_FONT);
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    // ================= 4. UI HELPERS (THE SEXY PART) =================

    private JPanel createCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(PANEL_COLOR);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(60,60,60), 1), new EmptyBorder(20, 20, 20, 20)));
        return p;
    }

    private JPanel createNavBar(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(15, 15, 20)); // Darker header
        p.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 20));
        l.setForeground(ACCENT_COLOR);

        JButton out = new RoundedButton("LOGOUT", new Color(214, 48, 49));
        out.setPreferredSize(new Dimension(100, 35));
        out.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        p.add(l, BorderLayout.WEST);
        p.add(out, BorderLayout.EAST);
        return p;
    }

    private JTable createModernTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(40);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setShowGrid(false);
        t.setBackground(PANEL_COLOR);
        t.setForeground(TEXT_PRIMARY);
        t.setSelectionBackground(ACCENT_COLOR);
        t.setSelectionForeground(Color.WHITE);

        JTableHeader h = t.getTableHeader();
        h.setBackground(new Color(45, 52, 54));
        h.setForeground(TEXT_PRIMARY);
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        h.setBorder(null);
        return t;
    }

    private JTabbedPane createModernTabs() {
        JTabbedPane t = new JTabbedPane();
        t.setBackground(BG_COLOR);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return t;
    }

    private void styleComboBox(JComboBox box) {
        box.setBackground(PANEL_COLOR);
        box.setForeground(Color.BLACK); // Combo text usually needs contrast
        box.setFont(BODY_FONT);
        box.setBorder(new LineBorder(Color.GRAY));
    }

    // --- CUSTOM ROUNDED BUTTON ---
    class RoundedButton extends JButton {
        private Color bgColor;
        public RoundedButton(String text, Color bg) {
            super(text);
            this.bgColor = bg;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(280, 45));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15)); // Rounded 15px
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 4;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    // --- MODERN TEXT FIELD ---
    class ModernField extends JTextField {
        private String placeholder;
        public ModernField(String placeholder) {
            this.placeholder = placeholder;
            setBorder(new CompoundBorder(new LineBorder(new Color(100,100,100)), new EmptyBorder(10, 10, 10, 10)));
            setBackground(new Color(45, 52, 54));
            setForeground(TEXT_PRIMARY);
            setFont(BODY_FONT);
            setCaretColor(ACCENT_COLOR);
            setText(placeholder);
            setForeground(Color.GRAY);

            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if(getText().equals(placeholder)) { setText(""); setForeground(TEXT_PRIMARY); }
                    setBorder(new CompoundBorder(new LineBorder(ACCENT_COLOR), new EmptyBorder(10, 10, 10, 10)));
                }
                public void focusLost(FocusEvent e) {
                    if(getText().isEmpty()) { setText(placeholder); setForeground(Color.GRAY); }
                    setBorder(new CompoundBorder(new LineBorder(new Color(100,100,100)), new EmptyBorder(10, 10, 10, 10)));
                }
            });
        }
    }

    class ModernPasswordField extends JPasswordField {
        public ModernPasswordField() {
            setBorder(new CompoundBorder(new LineBorder(new Color(100,100,100)), new EmptyBorder(10, 10, 10, 10)));
            setBackground(new Color(45, 52, 54));
            setForeground(TEXT_PRIMARY);
            setFont(BODY_FONT);
            setCaretColor(ACCENT_COLOR);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobPortalApp::new);
    }
}