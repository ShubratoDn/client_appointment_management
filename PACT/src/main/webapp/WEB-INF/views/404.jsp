<!DOCTYPE html>
<html lang="en">
    <head>
        <title>404 Error - NOT FOUND</title>
        <%@include file="_partials/headerLink.jsp"%>
    </head>
    <body>
        <div id="layoutError">
            <div id="layoutError_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-6">
                                <div class="text-center mt-4">
                                    <img class="mb-4 img-error" src="<%=request.getContextPath()%>/assets/img/error-404-monochrome.svg" />
                                    <p class="lead">This requested URL was not found on this server.</p>
                                    <a href="/">
                                        <i class="fas fa-arrow-left me-1"></i>
                                        Return to Dashboard
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
            <div id="layoutError_footer">
                <%@include file="_partials/footer.jsp"%>
            </div>
        </div>

        <%@include file="_partials/footerLink.jsp"%>
    </body>
</html>
