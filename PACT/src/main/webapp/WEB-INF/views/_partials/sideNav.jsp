<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
            <div class="nav">
                <div class="sb-sidenav-menu-heading">Core</div>
                <a class="nav-link" href="/">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Dashboard
                </a>

                <div class="sb-sidenav-menu-heading">AVAILABILITY</div>
                <a class="nav-link" href="/set-availability">
                    <div class="sb-nav-link-icon">
                        <i class="fas fa-book"></i>
                    </div>
                    Setup Availability
                </a>


                <div class="sb-sidenav-menu-heading">APPOINTMENT</div>
                <a class="nav-link" href="/book-appointment">
                    <div class="sb-nav-link-icon">
                    	<i class="fas fa-book"></i>
                    </div>
                    Book Appointment
                </a>

                </a>

                   <a class="nav-link" href="/appointment-requests">
                    <div class="sb-nav-link-icon">
                    	<i class="fas fa-calendar"></i>
                    </div>
                    Appointment Requests
                </a>

                </a>

                <a class="nav-link" href="/requested-appointment">
                    <div class="sb-nav-link-icon">
                        <i class="fas fa-address-book"></i>
                    </div>
                    Requested Appointment
                </a>

            </div>
        </div>
        <div class="sb-sidenav-footer">
            <div class="small">Logged in as:</div>
            <%= SecurityContextHolder.getContext().getAuthentication().getName() %>
        </div>

    </nav>
</div>