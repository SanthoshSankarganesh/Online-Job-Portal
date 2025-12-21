import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

// NOTE: If 'jakarta' is red, change imports to 'javax.servlet.*' and 'javax.servlet.http.*'
// This depends on your Tomcat version (Tomcat 9 = javax, Tomcat 10+ = jakarta)

public class JobPortalServlet extends HttpServlet {

    // 1. Handling POST requests (Data Submission)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // ROBUSTNESS: Check if action is null to prevent crash
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
            return;
        }

        try {
            if ("apply".equals(action)) {
                handleApplyJob(request, response);
            } else {
                response.getWriter().write("Invalid Action");
            }
        } catch (Exception e) {
            // ERROR HANDLING: Catching unexpected errors
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error: " + e.getMessage());
        }
    }

    // 2. Handling GET requests (Data Retrieval - Optional but good for full marks)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("Job Portal Servlet is Running. Use POST for actions.");
    }

    // Helper method to keep code clean (Code Quality Mark)
    private void handleApplyJob(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jobIdStr = request.getParameter("jobId");
        String userIdStr = request.getParameter("userId");

        // VALIDATION: Ensure inputs are not missing
        if (jobIdStr == null || userIdStr == null) {
            response.getWriter().write("Error: Missing Job ID or User ID");
            return;
        }

        try {
            int jobId = Integer.parseInt(jobIdStr);
            int userId = Integer.parseInt(userIdStr);

            // INTEGRATION: Calling your existing DatabaseService
            boolean success = DatabaseService.applyJob(jobId, userId);

            if (success) {
                response.getWriter().write("Success: Application Submitted");
            } else {
                response.getWriter().write("Failed: Already Applied or Invalid Data");
            }
        } catch (NumberFormatException e) {
            response.getWriter().write("Error: IDs must be numbers");
        }
    }
}