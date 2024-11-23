<%@ page import="com.appointment.management.pact.entity.User" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <%@include file="_partials/headerLink.jsp"%>
        <title>Password Reset - PACT</title>
    </head>
    <body class="bg-primary">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-5">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header">
                                        <h3 class="text-center font-weight-light my-4">Reset Password</h3>
                                    </div>
                                    <%
                                        User user = (User) request.getAttribute("user");
                                    %>
                                    <div class="card-body">
                                        <div class="small mb-3 text-muted">
                                            Hello <%= user != null ? user.getFullname() : "User" %>, kindly enter your password and confirm password to reset your password.
                                        </div>
                                        <%
                                            String errorMessage = (String) request.getAttribute("errorMessage");
                                            String successMessage = (String) request.getAttribute("successMessage");
                                            if (errorMessage != null) {
                                        %>
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            <strong>Error!</strong> <%= errorMessage %>
                                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <%
                                            }

                                            if (successMessage != null) {
                                        %>
                                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                                            <strong>Success!</strong> <%= successMessage %>
                                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                <span aria-hidden="true">&times;</span>
                                            </button>
                                        </div>
                                        <%
                                            }
                                        %>
                                        <form action="reset-password" method="post">
                                            <div class="form-floating mb-3">
                                                <input class="form-control" id="password" type="password" name="password" placeholder="New Password"  />
                                                <label for="password">New Password</label>
                                            </div>
                                            <div class="form-floating mb-3">
                                                <input class="form-control" id="confirmPassword" type="password" name="confirmPassword" placeholder="Confirm Password"  />
                                                <label for="confirmPassword">Confirm Password</label>
                                            </div>
                                            <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <a class="small" href="/login">Return to login</a>
                                                <button class="btn btn-primary" type="submit">Reset Password</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="card-footer text-center py-3">
                                        <div class="small"><a href="/register">Need an account? Sign up!</a></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </main>
            </div>
            <div id="layoutAuthentication_footer">
                <%@include file="_partials/footer.jsp"%>
            </div>
        </div>

        <%@include file="_partials/footerLink.jsp"%>
    </body>
</html>
