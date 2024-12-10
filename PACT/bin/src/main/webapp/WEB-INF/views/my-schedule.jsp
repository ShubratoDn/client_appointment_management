<!DOCTYPE html>
<html lang="en">
    <head>
        <%@include file="_partials/headerLink.jsp"%>
        <title>My Schedule - PACT</title>
    </head>
    <body class="sb-nav-fixed">

        <%@include file="_partials/navbar.jsp"%>

        <div id="layoutSidenav">

            <%@include file="_partials/sideNav.jsp"%>

            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">My Schedule</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">My Schedule</li>
                        </ol>
                        
                    </div>
                </main>
                <%@include file="_partials/footer.jsp"%>
            </div>
        </div>

<%--        footer links--%>
        <%@include file="_partials/footerLink.jsp"%>
    </body>
</html>
