import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    // 1. Connection Settings
    private static final String URL = "jdbc:mysql://localhost:3306/job_portal_gui";
    private static final String USER = "root";
    private static final String PASS = "Admin@123"; // ENSURE THIS MATCHES YOUR DB

    public static Connection connect() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 2. Innovation: Background Email Thread
    public static void simulateEmail(String email, String subject) {
        new Thread(() -> {
            try {
                System.out.println("... Sending email to " + email);
                Thread.sleep(1500); // Simulate network delay
                System.out.println("✓ Email Sent: " + subject);
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }).start();
    }

    // 3. User Login (Was missing)
    public static User login(String email, String password) {
        try (Connection con = connect()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                int id = rs.getInt("id");
                String name = rs.getString("name");

                if ("EMPLOYER".equals(role)) {
                    return new Employer(id, name, email, rs.getString("company_name"));
                } else if ("ADMIN".equals(role)) {
                    return new Admin(id, name, email);
                } else {
                    return new JobSeeker(id, name, email, rs.getString("skills"), rs.getInt("experience_years"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // 4. Register User (Robust Logic)
    public static boolean registerUser(String name, String email, String password, String role, String extraInfo) {
        if (name == null || email == null || !email.contains("@") || password.length() < 3) return false;

        try (Connection con = connect()) {
            String sql = "INSERT INTO users (name, email, password, role, company_name, skills) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.setString(5, "EMPLOYER".equals(role) ? extraInfo : null);
            ps.setString(6, "JOB_SEEKER".equals(role) ? extraInfo : null);

            int rows = ps.executeUpdate();
            if(rows > 0) simulateEmail(email, "Welcome to Job Portal!");
            return rows > 0;
        } catch (SQLException e) { return false; }
    }

    // 5. Job Management (Was missing)
    public static List<Job> getAllJobs() {
        List<Job> list = new ArrayList<>();
        try (Connection con = connect()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM jobs");
            while (rs.next()) list.add(new Job(rs.getInt("id"), rs.getInt("employer_id"), rs.getString("title"), rs.getString("description"), rs.getDouble("salary")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean postJob(int empId, String title, String desc, double salary) {
        try (Connection con = connect()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO jobs (employer_id, title, description, salary) VALUES (?, ?, ?, ?)");
            ps.setInt(1, empId);
            ps.setString(2, title);
            ps.setString(3, desc);
            ps.setDouble(4, salary);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // 6. Application Logic (Robust)
    public static synchronized boolean applyJob(int jobId, int seekerId) {
        try (Connection con = connect()) {
            // Check for duplicates
            PreparedStatement check = con.prepareStatement("SELECT id FROM applications WHERE job_id=? AND seeker_id=?");
            check.setInt(1, jobId);
            check.setInt(2, seekerId);
            if(check.executeQuery().next()) return false;

            PreparedStatement ps = con.prepareStatement("INSERT INTO applications (job_id, seeker_id) VALUES (?, ?)");
            ps.setInt(1, jobId);
            ps.setInt(2, seekerId);
            int rows = ps.executeUpdate();
            if(rows > 0) simulateEmail("admin@portal.com", "New Application Received");
            return rows > 0;
        } catch (SQLException e) { return false; }
    }

    public static List<Application> getEmployerApplications(int employerId) {
        List<Application> list = new ArrayList<>();
        try (Connection con = connect()) {
            String sql = "SELECT a.id, j.title, u.name, a.status FROM applications a JOIN jobs j ON a.job_id = j.id JOIN users u ON a.seeker_id = u.id WHERE j.employer_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, employerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) list.add(new Application(rs.getInt("id"), rs.getString("title"), rs.getString("name"), rs.getString("status")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // REQUIRED FOR SEEKER DASHBOARD
    public static List<Application> getSeekerApplications(int seekerId) {
        List<Application> list = new ArrayList<>();
        try (Connection con = connect()) {
            String sql = "SELECT a.id, j.title, u.name AS company, a.status FROM applications a JOIN jobs j ON a.job_id = j.id JOIN users u ON j.employer_id = u.id WHERE a.seeker_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, seekerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) list.add(new Application(rs.getInt("id"), rs.getString("title"), rs.getString("company"), rs.getString("status")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean updateApplicationStatus(int appId, String status) {
        try (Connection con = connect()) {
            PreparedStatement ps = con.prepareStatement("UPDATE applications SET status=? WHERE id=?");
            ps.setString(1, status);
            ps.setInt(2, appId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}