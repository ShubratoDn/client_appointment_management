<%@ page import="com.appointment.management.pact.entity.User" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <%@include file="_partials/headerLink.jsp"%>
        <title>OTP Verification - PACT</title>
    </head>
    <body class="bg-primary">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-5">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header"><h3 class="text-center font-weight-light my-4">OTP Verification</h3></div>

                                    <%
                                        User user = (User) request.getAttribute("user");
                                        String verificationFor = (String) request.getAttribute("verificationFor");
                                    %>

                                    <div class="card-body">
                                        <div class="small mb-3 text-muted">Hello  <%=user == null ? "User, " : (user.getFullname() == null ? " User " : user.getFullname())  %>, kindly check your email. Use otp or click the button showing inside the mail to verify your account</div>



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



                                        <form action="/verify-otp" method="post">
                                            <input type="hidden" name="username" value="<%=user.getUsername()%>">
                                            <input type="hidden" name="verificationFor" value="<%=verificationFor%>">
                                            <div class="form-floating mb-3">
                                                <input class="form-control" id="inputEmail" type="text" name="otp" placeholder="123456" />
                                                <label for="inputEmail">Enter the OTP</label>
                                            </div>
                                            <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <a class="small" href="/login">Return to login</a>
                                                <button class="btn btn-primary" type="submit">Verify OTP</button>
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
