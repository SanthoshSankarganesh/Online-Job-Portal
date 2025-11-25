import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class JobPortalApp extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private User currentUser;

    // Professional Colors
    private final Color PRIMARY = new Color(33, 150, 243);    // Blue
    private final Color ACCENT = new Color(76, 175, 80);      // Green
    private final Color DARK = new Color(66, 66, 66);         // Dark Grey
    private final Color INPUT_BG = new Color(245, 245, 245);  // Light Grey for inputs
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public JobPortalApp() {
        setTitle("Enterprise Job Portal System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set clean layout
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");

        add(mainPanel);
        setVisible(true);
    }

    // ================= LOGIN SCREEN (FIXED LAYOUT) =================
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Card Container for Login
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fix: Stretches components to align
        gbc.weightx = 1.0;

        // Header
        JLabel title = new JLabel("Job Portal Login", SwingConstants.CENTER);
        title.setFont(HEADER_FONT);
        title.setForeground(PRIMARY);

        // Inputs
        JTextField email = createStyledInput("hr@tech.com");
        JPasswordField pass = createStyledPassword("emp123");
        JButton loginBtn = createStyledButton("Sign In", PRIMARY);
        JButton regBtn = createStyledButton("Create New Account", DARK);

        // Layout Assembly
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(title, gbc);

        gbc.gridy = 1;
        card.add(new JLabel("Email Address"), gbc);

        gbc.gridy = 2;
        card.add(email, gbc);

        gbc.gridy = 3;
        card.add(new JLabel("Password"), gbc);

        gbc.gridy = 4;
        card.add(pass, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10); // Extra space before buttons
        card.add(loginBtn, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        card.add(regBtn, gbc);

        panel.add(card); // Add card to center of screen

        // Logic
        loginBtn.addActionListener(e -> {
            currentUser = DatabaseService.login(email.getText(), new String(pass.getPassword()));
            if (currentUser != null) {
                loadDashboard(currentUser);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        });

        regBtn.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));

        return panel;
    }

    // ================= REGISTER SCREEN (FIXED LAYOUT) =================
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("Register New User", SwingConstants.CENTER);
        title.setFont(HEADER_FONT);
        title.setForeground(ACCENT);

        JTextField name = createStyledInput("");
        JTextField email = createStyledInput("");
        JPasswordField pass = createStyledPassword("");

        String[] roles = {"JOB_SEEKER", "EMPLOYER"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setFont(MAIN_FONT);
        roleBox.setBackground(Color.WHITE);

        JTextField extraInfo = createStyledInput("");
        extraInfo.setBorder(BorderFactory.createTitledBorder("Skills (Seeker) / Company (Employer)"));

        JButton submit = createStyledButton("Complete Registration", ACCENT);
        JButton back = createStyledButton("Back to Login", DARK);

        // Assembly
        gbc.gridx = 0; gbc.gridy = 0; card.add(title, gbc);

        gbc.gridy = 1; card.add(new JLabel("Full Name"), gbc);
        gbc.gridy = 2; card.add(name, gbc);

        gbc.gridy = 3; card.add(new JLabel("Email"), gbc);
        gbc.gridy = 4; card.add(email, gbc);

        gbc.gridy = 5; card.add(new JLabel("Password"), gbc);
        gbc.gridy = 6; card.add(pass, gbc);

        gbc.gridy = 7; card.add(new JLabel("Role"), gbc);
        gbc.gridy = 8; card.add(roleBox, gbc);

        gbc.gridy = 9; card.add(extraInfo, gbc);

        gbc.gridy = 10; gbc.insets = new Insets(20, 5, 5, 5);
        card.add(submit, gbc);

        gbc.gridy = 11; gbc.insets = new Insets(5, 5, 5, 5);
        card.add(back, gbc);

        panel.add(card);

        // Logic
        submit.addActionListener(e -> {
            if(DatabaseService.registerUser(name.getText(), email.getText(), new String(pass.getPassword()),
                    (String)roleBox.getSelectedItem(), extraInfo.getText())) {
                JOptionPane.showMessageDialog(this, "Registration Successful! Please Login.");
                cardLayout.show(mainPanel, "LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed. Email might be taken.");
            }
        });
        back.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        return panel;
    }

    // ================= DASHBOARD LOADER =================
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

    // ================= JOB SEEKER DASHBOARD =================
    private JPanel createSeekerDashboard() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(MAIN_FONT);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        DefaultTableModel jobModel = new DefaultTableModel(new String[]{"ID", "Title", "Desc", "Salary"}, 0);
        JTable jobTable = styleTable(new JTable(jobModel));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = createStyledButton("Search", PRIMARY);
        JButton applyBtn = createStyledButton("Apply Selected", ACCENT);
        JButton refreshBtn = createStyledButton("Refresh", DARK);

        top.add(new JLabel("Find: ")); top.add(searchField); top.add(searchBtn); top.add(applyBtn); top.add(refreshBtn);
        searchPanel.add(top, BorderLayout.NORTH);
        searchPanel.add(new JScrollPane(jobTable), BorderLayout.CENTER);

        // My Apps Panel
        JPanel myAppsPanel = new JPanel(new BorderLayout());
        DefaultTableModel appModel = new DefaultTableModel(new String[]{"App ID", "Job Title", "My Name", "Status"}, 0);
        JTable appTable = styleTable(new JTable(appModel));
        JButton refreshApps = createStyledButton("Check Status Updates", DARK);
        myAppsPanel.add(refreshApps, BorderLayout.NORTH);
        myAppsPanel.add(new JScrollPane(appTable), BorderLayout.CENTER);

        // Logic
        Runnable loadJobs = () -> {
            jobModel.setRowCount(0);
            List<Job> jobs = DatabaseService.getAllJobs();
            for(Job j : jobs) if(j.title.toLowerCase().contains(searchField.getText().toLowerCase()))
                jobModel.addRow(new Object[]{j.id, j.title, j.description, j.salary});
        };

        Runnable loadApps = () -> {
            appModel.setRowCount(0);
            for(Application a : DatabaseService.getSeekerApplications(currentUser.getId()))
                appModel.addRow(new Object[]{a.id, a.jobTitle, a.applicantName, a.status});
        };

        searchBtn.addActionListener(e -> loadJobs.run());
        refreshBtn.addActionListener(e -> loadJobs.run());
        refreshApps.addActionListener(e -> loadApps.run());

        applyBtn.addActionListener(e -> {
            int r = jobTable.getSelectedRow();
            if(r != -1 && DatabaseService.applyJob((int)jobModel.getValueAt(r, 0), currentUser.getId()))
                JOptionPane.showMessageDialog(this, "Applied Successfully!");
            else JOptionPane.showMessageDialog(this, "Select a job / Already Applied");
        });

        // Initial Load
        loadJobs.run();
        loadApps.run();

        tabs.addTab("🔍 Find Jobs", searchPanel);
        tabs.addTab("📋 Track Applications", myAppsPanel);

        JPanel container = new JPanel(new BorderLayout());
        container.add(createHeader(), BorderLayout.NORTH);
        container.add(tabs, BorderLayout.CENTER);
        return container;
    }

    // ================= EMPLOYER DASHBOARD =================
    private JPanel createEmployerDashboard() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(MAIN_FONT);

        // Post Job Panel
        JPanel postPanel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField t = createStyledInput("");
        JTextField s = createStyledInput("");
        JTextArea d = new JTextArea(5, 20);
        d.setBorder(new LineBorder(Color.LIGHT_GRAY));
        JButton post = createStyledButton("Post Vacancy", ACCENT);

        g.gridx=0; g.gridy=0; postPanel.add(new JLabel("Job Title"), g);
        g.gridy=1; postPanel.add(t, g);
        g.gridy=2; postPanel.add(new JLabel("Salary"), g);
        g.gridy=3; postPanel.add(s, g);
        g.gridy=4; postPanel.add(new JLabel("Description"), g);
        g.gridy=5; postPanel.add(new JScrollPane(d), g);
        g.gridy=6; postPanel.add(post, g);

        post.addActionListener(e -> {
            try {
                if(DatabaseService.postJob(currentUser.getId(), t.getText(), d.getText(), Double.parseDouble(s.getText()))) {
                    JOptionPane.showMessageDialog(this, "Job Posted!");
                    t.setText(""); s.setText(""); d.setText("");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid Salary"); }
        });

        // Manage Apps Panel
        JPanel applicantPanel = new JPanel(new BorderLayout());
        DefaultTableModel appModel = new DefaultTableModel(new String[]{"App ID", "Job Title", "Candidate", "Status"}, 0);
        JTable appTable = styleTable(new JTable(appModel));

        JPanel btnPanel = new JPanel();
        JButton loadApps = createStyledButton("Load Applicants", DARK);
        JButton accept = createStyledButton("Accept", ACCENT);
        JButton reject = createStyledButton("Reject", new Color(211, 47, 47));
        btnPanel.add(loadApps); btnPanel.add(accept); btnPanel.add(reject);

        applicantPanel.add(btnPanel, BorderLayout.NORTH);
        applicantPanel.add(new JScrollPane(appTable), BorderLayout.CENTER);

        Runnable refreshApps = () -> {
            appModel.setRowCount(0);
            for(Application a : DatabaseService.getEmployerApplications(currentUser.getId()))
                appModel.addRow(new Object[]{a.id, a.jobTitle, a.applicantName, a.status});
        };

        loadApps.addActionListener(e -> refreshApps.run());
        accept.addActionListener(e -> updateStatus(appTable, appModel, "ACCEPTED", refreshApps));
        reject.addActionListener(e -> updateStatus(appTable, appModel, "REJECTED", refreshApps));

        refreshApps.run();

        tabs.addTab("✍️ Post Vacancy", postPanel);
        tabs.addTab("👥 Manage Applicants", applicantPanel);

        JPanel container = new JPanel(new BorderLayout());
        container.add(createHeader(), BorderLayout.NORTH);
        container.add(tabs, BorderLayout.CENTER);
        return container;
    }

    // ================= ADMIN DASHBOARD =================
    private JPanel createAdminDashboard() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel m = new DefaultTableModel(new String[]{"ID", "Title", "Salary"}, 0);
        JTable t = styleTable(new JTable(m));
        JButton r = createStyledButton("Refresh Jobs", DARK);
        JButton d = createStyledButton("Delete Selected", new Color(211, 47, 47));

        JPanel top = new JPanel(); top.add(r); top.add(d);
        p.add(createHeader(), BorderLayout.NORTH);
        p.add(top, BorderLayout.CENTER);
        p.add(new JScrollPane(t), BorderLayout.SOUTH);

        r.addActionListener(e -> {
            m.setRowCount(0);
            for(Job j : DatabaseService.getAllJobs()) m.addRow(new Object[]{j.id, j.title, j.salary});
        });
        d.addActionListener(e -> {
            if(t.getSelectedRow() != -1) m.removeRow(t.getSelectedRow());
        });
        r.doClick();
        return p;
    }

    // ================= STYLING HELPERS (FIXED) =================

    // Fixes the issue where buttons were not showing text unless hovered
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE); // FORCE WHITE TEXT
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setOpaque(true); // FORCE OPAQUE SO BG COLOR SHOWS
        btn.setMargin(new Insets(10, 20, 10, 20)); // Bigger buttons

        // Hover Effect: Darken background, keep text white
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JTextField createStyledInput(String text) {
        JTextField f = new JTextField(text, 20);
        f.setFont(MAIN_FONT);
        f.setBackground(INPUT_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(8, 8, 8, 8) // Internal Padding for neat typing
        ));
        return f;
    }

    private JPasswordField createStyledPassword(String text) {
        JPasswordField f = new JPasswordField(text, 20);
        f.setFont(MAIN_FONT);
        f.setBackground(INPUT_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(8, 8, 8, 8)
        ));
        return f;
    }

    private void updateStatus(JTable table, DefaultTableModel model, String status, Runnable refresh) {
        int r = table.getSelectedRow();
        if(r != -1) {
            int appId = (int)model.getValueAt(r, 0);
            DatabaseService.updateApplicationStatus(appId, status);
            refresh.run();
        } else JOptionPane.showMessageDialog(this, "Select an applicant first");
    }

    private JPanel createHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PRIMARY);
        p.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel l = new JLabel("Welcome, " + (currentUser != null ? currentUser.getName() : "Guest"));
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JButton logout = createStyledButton("Logout", new Color(255, 82, 82));
        logout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logout.setMargin(new Insets(5, 10, 5, 10));

        logout.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        p.add(l, BorderLayout.WEST); p.add(logout, BorderLayout.EAST);
        return p;
    }

    private JTable styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(MAIN_FONT);
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setSelectionForeground(Color.BLACK);
        JTableHeader h = table.getTableHeader();
        h.setBackground(DARK);
        h.setForeground(Color.WHITE);
        h.setFont(new Font("Segoe UI", Font.BOLD, 14));
        h.setPreferredSize(new Dimension(0, 40));
        return table;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobPortalApp::new);
    }
}