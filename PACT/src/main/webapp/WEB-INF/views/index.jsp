<%@ page import="com.appointment.management.pact.entity.UserAppointment" %>
<%@ page import="java.util.List" %>
<%@ page import="com.appointment.management.pact.services.HelperService" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="_partials/headerLink.jsp" %>
    <title>Dashboard - PACT</title>
</head>
<body class="sb-nav-fixed">

<%@include file="_partials/navbar.jsp" %>

<div id="layoutSidenav">

    <%@include file="_partials/sideNav.jsp" %>

    <div id="layoutSidenav_content">
        <main>
            <div class="container-fluid px-4">
                <h1 class="mt-4">Dashboard</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item active">Dashboard</li>
                </ol>
                <div class="row">
                    <%
                        List<UserAppointment> upcomingAppointments = (List<UserAppointment>) request.getAttribute("upcomingAppointments");
                        if (upcomingAppointments != null && !upcomingAppointments.isEmpty()) {
                            out.println("<h3>Upcoming Appointments: " + upcomingAppointments.size() + "</h3>");

                            LocalDateTime now = LocalDateTime.now(); // Current date and time

                            for (UserAppointment appointment : upcomingAppointments) {
                                Integer userId = appointment.getAuthor().getUserId();
                                Integer logged = HelperService.getLoggedInUser().getUserId();

                                // Format date and time
                                LocalDateTime start = appointment.getAppointment().getStartTime();
                                LocalDateTime end = appointment.getAppointment().getEndTime();
                                java.time.Duration duration = java.time.Duration.between(start, end);

                                String date = start.format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
                                String startTime = start.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
                                String endTime = end.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
                                long hours = duration.toHours();
                                long minutes = duration.toMinutes() % 60;

                                // Check if the appointment is currently running
                                boolean isRunning = now.isAfter(start) && now.isBefore(end);

                    %>
                    <div class="col-md-4">
                        <div class="alert <%= isRunning ? "alert-success" : "alert-primary" %>">
                            <h2><%= appointment.getAppointment().getDescription() %></h2>
                            <div>Appointment With:
                                <b><%= (userId.equals(logged) ? appointment.getRequested_user().getFullname() : appointment.getAuthor().getFullname()) %></b>
                            </div>
                            <div>Location: <%= appointment.getAppointment().getLocation() %></div>
                            <br>
                            <h5>Schedule Info <%=isRunning ? "<span class='text-muted'>(On Going)<span>" :""%></h5>
                            <div>Date: <%= date %></div>
                            <div>From <%= startTime %> to <%= endTime %></div>
                            <div>Duration:
                                <%= (hours > 0 ? hours + " Hour" + (hours > 1 ? "s " : " ") : "") %>
                                <%= (minutes > 0 ? minutes + " Minute" + (minutes > 1 ? "s" : "") : "") %>
                            </div>
                        </div>
                    </div>
                    <%
                            }
                        }else{
                            out.println("<h3>No upcoming appointments found.</h3>");
                            }
                    %>


                </div>
        </main>
        <%@include file="_partials/footer.jsp" %>
    </div>
</div>

<%--        footer links--%>
<%@include file="_partials/footerLink.jsp" %>
</body>
</html>
