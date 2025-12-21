import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launches the application in the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(JobPortalApp::new);
    }
}