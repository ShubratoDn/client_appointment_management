<%@ page import="com.appointment.management.pact.entity.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <%@include file="_partials/headerLink.jsp"%>
        <title>Requesting Appointment</title>
        <!-- Mobiscroll JS and CSS Includes -->
        <link rel="stylesheet" href="<%= request.getContextPath() %>/css/mobiscroll.javascript.min.css">
        <script src="<%= request.getContextPath() %>/js/mobiscroll.javascript.min.js"></script>
    </head>
    <body class="sb-nav-fixed">

        <%@include file="_partials/navbar.jsp"%>

        <div id="layoutSidenav">

            <%@include file="_partials/sideNav.jsp"%>

            <div id="layoutSidenav_content">
                <%
                    User requestingUser = (User) request.getAttribute("user");
                %>
                <main>
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">Requesting Appointment </h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">Requesting Appointment to <b><%=requestingUser.getFullname()%></b> </li>
                        </ol>

                        <div class="row">
                            <div class="mbsc-col-sm-12 mbsc-col-md-4">
                                <div class="mbsc-form-group">
                                    <h3 class="">Select a day for Booking an Appointment</h3>
                                    <div id="demo-multi-day"></div>
                                </div>
                            </div>
                        </div>

                    </div>
                </main>
                <%@include file="_partials/footer.jsp"%>
            </div>
        </div>

        <!-- Bootstrap Modal -->
        <div class="modal fade" id="availabilityModal" tabindex="-1" role="dialog" aria-labelledby="availabilityModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="availabilityModalLabel">Check Availability</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p id="availabilityMessage">Checking availability...</p>

                        <form action="/appointment-request/{userId}" method="post" id="appointmentRequestForm" style="display: none;">
                            <label>Appointment Starting Time</label>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">Starting Time : </span>
                                </div>
                                <input type="time" class="form-control" id="appointmentStartingTime" aria-describedby="basic-addon3">
                            </div>

                            <label>Appointment Ending Time</label>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">Ending Time : </span>
                                </div>
                                <input type="time" class="form-control" id="appointmentEndingTime" aria-describedby="basic-addon3">
                            </div>

                            <button type="submit" class="btn btn-primary" id="submitAppointmentRequest">Submit</button>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeModal()">Close</button>
                    </div>
                </div>
            </div>
        </div>


        <%--        footer links--%>
        <%@include file="_partials/footerLink.jsp"%>


        <script>
            // Get the current date
            var today = new Date();

            // Calculate the minimum date (2 days from today)
            var minDate = new Date();
            minDate.setDate(today.getDate());

            var maxDate = new Date();
            maxDate.setDate(today.getDate() + 60);

            var min = minDate.toISOString().slice(0, 16);
            var max = maxDate.toISOString().slice(0, 16);


            mobiscroll.setOptions({
                locale: mobiscroll.localeEn,
                theme: 'ios',
                themeVariant: 'light'
            });

            function closeModal(){
                $('#availabilityModal').modal('hide');
            }


            // Extract user ID from the current URL
            const currentUrl = window.location.href;
            const userId = currentUrl.split('/').pop(); // Assumes user ID is the last segment of the URL
            document.addEventListener('click', function (event) {
                // Check if the clicked element is a calendar cell with the desired class
                if (event.target.classList.contains('mbsc-calendar-cell-text')) {
                    // Get the aria-label value for the clicked date
                    const ariaLabel = event.target.getAttribute('aria-label');
                    if (ariaLabel) {
                        // Extract the date from the aria-label (assuming format like "Monday, December 9, 2024")
                        // const date = ariaLabel.split(', ')[1];


                        // Show the modal
                        $('#availabilityModal').modal('show');

                        // Display the loading message
                        $('#availabilityMessage').text('Checking availability...');

                        var encodedDate = encodeURIComponent(ariaLabel);
                        var requestURL = "/check-availability/userID/"+userId+"/"+encodedDate;

                        // Send the AJAX request
                        $.ajax({
                            url: requestURL,
                            method: 'GET',
                            success: function (data) {
                                console.log(data);

                                // Extract details
                                const availableDate = data.availableDate;
                                const startTime = data.workingHourStart;
                                const endTime = data.workingHourEnd;

                                // Create the message
                                const message = `
                                    Availability Details:
                                    - Date: `+ ariaLabel + `
                                    - Start Time: `+startTime + ` (24-hour format)
                                    - End Time: `+endTime+` (24-hour format)
                                `;

                                // Show the form after successful availability check
                                $('#appointmentRequestForm').show();

                                // Set the min and max properties for the input fields
                                $('#appointmentStartingTime').attr('min', startTime);
                                $('#appointmentEndingTime').attr('max', endTime);


                                // Update the modal with the formatted response
                                $('#availabilityMessage').html(message.replace(/\n/g, '<br>')); // Replace newlines with <br> for HTML display
                            },
                            error: function (err) {
                                $('#appointmentRequestForm').hide();
                                console.log(err);
                                // Handle errors
                                $('#availabilityMessage').text(err.responseText);
                            }
                        });

                    }
                }
            });

            // Handle form submission via AJAX
            $('#appointmentRequestForm').submit(function (e) {
                e.preventDefault(); // Prevent the default form submission

                const startingTime = $('#appointmentStartingTime').val();
                const endingTime = $('#appointmentEndingTime').val();

                if (!startingTime || !endingTime) {
                    alert("Both starting and ending times are required.");
                    return;
                }

                $.ajax({
                    url: '/appointment-request/' + userId, // Update with the actual userId dynamically
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        startingTime: startingTime,
                        endingTime: endingTime,
                    }),
                    success: function (response) {
                        alert('Appointment request submitted successfully!');
                        // Optionally hide the form after submission
                        $('#appointmentRequestForm').hide();
                    },
                    error: function (err) {
                        console.log(err);
                        alert('An error occurred while submitting the appointment request.');
                    }
                });
            });






            var availableDatesColor = [
            <%
                List<String> dates = (List<String>) request.getAttribute("availableDates");
                String color = "#7bde83";
                for (int i = 0; i < dates.size(); i++) {
                    out.print("{ date: '" + dates.get(i) + "T00:00', highlight: '" + color + "' }");
                    if (i < dates.size() - 1) {
                        out.print(", ");
                    }
                }
            %>
        ];


        var now = new Date();

        mobiscroll.eventcalendar('#demo-multi-day', {
            controls: ['calendar', 'time'],
            theme: 'ios',
            min: min,
            max: max,
            themeVariant: 'light',
            clickToCreate: false,
            dragToCreate: false,
            dragToMove: false,
            dragToResize: false,
            eventDelete: false,
            colors: availableDatesColor,
            // Add the onDayClick event handler
            onSelectedDateChange: function (event, inst) {
                console.log('Date clicked:', event.date);
            },
            onEventCreated: function (args) {
               // alert("alert")
            },
            view: {
                calendar: {
                    labels: true,
                },
            },
            data: [
                {
                    start: '2024-12-07T09:00',
                    end: '2024-12-10T18:00',
                    title: 'Business of Software Conference',
                    color: '#ff6d42',
                },
                {
                    start: '2024-12-07T13:00',
                    end: '2024-12-08T21:00',
                    title: 'Friends binge marathon',
                    color: '#7bde83',
                },
                {
                    start: '2024-12-14T13:00',
                    end: '2024-12-15T21:00',
                    title: 'Friends binge marathon',
                    color: '#7bde83',
                },
            ],
        });

    </script>


    </body>
</html>
