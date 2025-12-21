import java.io.Serializable;

// Interface for Role Management (OOP Principle)
interface UserRole {
    String getRoleName();
}

// Abstract Base Class (Inheritance)
abstract class User implements UserRole, Serializable {
    protected int id;
    protected String name;
    protected String email;
    protected String role;

    public User(int id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String getRoleName() { return role; }
}

class Employer extends User {
    private String companyName;
    public Employer(int id, String name, String email, String companyName) {
        super(id, name, email, "EMPLOYER");
        this.companyName = companyName;
    }
    public String getCompanyName() { return companyName; }
}

class JobSeeker extends User {
    private String skills;
    private int experience;
    public JobSeeker(int id, String name, String email, String skills, int experience) {
        super(id, name, email, "JOB_SEEKER");
        this.skills = skills;
        this.experience = experience;
    }
    public String getSkills() { return skills; }
}

class Admin extends User {
    public Admin(int id, String name, String email) {
        super(id, name, email, "ADMIN");
    }
}

class Job {
    int id;
    int employerId;
    String title;
    String description;
    double salary;

    public Job(int id, int employerId, String title, String description, double salary) {
        this.id = id;
        this.employerId = employerId;
        this.title = title;
        this.description = description;
        this.salary = salary;
    }

    @Override
    public String toString() { return title; }
}

class Application {
    int id;
    String jobTitle;
    String applicantName;
    String status;

    public Application(int id, String jobTitle, String applicantName, String status) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.applicantName = applicantName;
        this.status = status;
    }
}