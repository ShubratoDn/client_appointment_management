<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Login - PACT</title>
        <%@include file="_partials/headerLink.jsp"%>
    </head>
    <body class="bg-primary">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-5">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header"><h3 class="text-center font-weight-light my-4">Login</h3></div>
                                    <div class="card-body">
                                        <form action="/login" method="post">

                                            <%
                                                // Retrieve the error message from the session or request
                                                String errorMessage = (String) session.getAttribute("error");
                                                String successMessage = (String) request.getAttribute("successMessage");
                                                String err = (String) request.getAttribute("errorMessage");
                                                if (errorMessage != null) {
                                            %>
                                            <div class="alert alert-danger" role="alert">
                                                <%= errorMessage %>
                                            </div>
                                            <%
                                                    // Clear the error message after displaying it
                                                    session.removeAttribute("error");
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

                                                if (err != null) {
                                            %>
                                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                                <strong>Error!</strong> <%= err %>
                                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                            <%
                                                }
                                            %>

                                            <div class="form-floating mb-3">
                                                <input class="form-control" value="" id="inputEmail" type="text" placeholder="name@example.com" name="username"/>
                                                <label for="inputEmail">Email address</label>
                                            </div>
                                            <div class="form-floating mb-3">
                                                <input class="form-control" value="" id="inputPassword" type="password" placeholder="Password" name="password" />
                                                <label for="inputPassword">Password</label>
                                            </div>
                                            <div class="form-check mb-3">
                                                <input class="form-check-input" id="inputRememberPassword" type="checkbox" value="" />
                                                <label class="form-check-label" for="inputRememberPassword">Remember Password</label>
                                            </div>
                                            <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <a class="small" href="/forgot-password">Forgot Password?</a>
                                                <button class="btn btn-primary" type="submit">Login</button>
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
