<%@ page import="com.appointment.management.pact.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.appointment.management.pact.entity.UserAppointment" %>
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
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeModal()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p id="availabilityMessage">Checking availability...</p>

                        <form action="/appointment-request/{userId}" method="post" id="appointmentRequestForm" style="display: none;">
                            <!-- Appointment Starting Time -->
                            <label>Appointment Starting Time</label>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">Starting Time:</span>
                                </div>
                                <input type="time" class="form-control" id="appointmentStartingTime" name="appointmentStartingTime" aria-describedby="basic-addon3" required>
                            </div>

                            <!-- Appointment Ending Time -->
                            <label>Appointment Ending Time</label>
                            <div class="input-group mb-3">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">Ending Time:</span>
                                </div>
                                <input type="time" class="form-control" id="appointmentEndingTime" name="appointmentEndingTime" aria-describedby="basic-addon3" required>
                            </div>

                            <!-- Description (Required) -->
                            <label>Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3" placeholder="Enter appointment description..." required></textarea>

                            <!-- Is All Day -->
                            <div class="form-check mt-3">
                                <input type="checkbox" class="form-check-input" id="isAllDay" name="isAllDay">
                                <label class="form-check-label" for="isAllDay">Is this an all-day event?</label>
                            </div>

                            <!-- Location (Optional) -->
                            <label class="mt-3">Location (Optional)</label>
                            <input type="text" class="form-control" id="location" name="location" placeholder="Enter location (optional)">

                            <!-- Submit Button -->
                            <button type="submit" class="btn btn-primary mt-3" id="submitAppointment">Submit Appointment</button>
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
            var selectedDate ;
            document.addEventListener('click', function (event) {
                // Check if the clicked element is a calendar cell with the desired class
                if (event.target.classList.contains('mbsc-calendar-cell-text')) {
                    // Get the aria-label value for the clicked date
                    const ariaLabel = event.target.getAttribute('aria-label');
                    if (ariaLabel) {
                        // Extract the date from the aria-label (assuming format like "Monday, December 9, 2024")
                        // selectedDate = ariaLabel.split(', ')[1];


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


            $('#appointmentStartingTime, #appointmentEndingTime').on('change', function () {
                const startTime = $('#appointmentStartingTime').val();
                const endTime = $('#appointmentEndingTime').val();

                if (startTime && endTime && startTime > endTime) {
                    alert('Starting time cannot be later than the ending time!');
                    $(this).val(''); // Clear the invalid input
                }
            });

            // Handle form submission via AJAX
            $('#appointmentRequestForm').submit(function (e) {
                e.preventDefault(); // Prevent the default form submission

                const appointmentData = {
                    bookingDate : selectedDate,
                    startTime: $('#appointmentStartingTime').val(),
                    endTime: $('#appointmentEndingTime').val(),
                    description: $('#description').val(),
                    isAllDay: $('#isAllDay').is(':checked'),
                    location: $('#location').val(),
                };

                // Validate required fields
                if (!appointmentData.description) {
                    alert('Description is required.');
                    return;
                }

                if (!appointmentData.startTime || !appointmentData.endTime) {
                    alert('Both starting and ending times are required.');
                    return;
                }

                // Send the AJAX request
                $.ajax({
                    url: "/appointment-request/"+userId,
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(appointmentData),
                    success: function (response) {
                        console.log(response)
                        alert('Appointment successfully submitted!');
                        // Optional: Close the modal or clear the form
                        // $('#appointmentRequestForm')[0].reset();
                    },
                    error: function (err) {
                        console.log(err);
                        $('#availabilityMessage').text(err.responseText);
                        alert(err.responseText);
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


            var appointments = [
                <%
                    List<UserAppointment> appointments = (List<UserAppointment>) request.getAttribute("allAppointment");
                    String titleColor = "#7bde83"; // Default color
                    for (int i = 0; i < appointments.size(); i++) {
                        UserAppointment ua = appointments.get(i);
                        String startTime = ua.getAppointment().getStartTime().toString(); // Format: YYYY-MM-DDTHH:mm
                        String endTime = ua.getAppointment().getEndTime().toString();   // Format: YYYY-MM-DDTHH:mm
                        String title = ua.getAppointment().getDescription();
                        out.print("{");
                        out.print("start: '" + startTime + "', ");
                        out.print("end: '" + endTime + "', ");
                        out.print("title: '" + title + "', ");
                        out.print("color: '" + titleColor + "'");
                        out.print("}");
                        if (i < appointments.size() - 1) {
                            out.print(", ");
                        }
                    }
                %>
            ];



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
                selectedDate = event.date;
                console.log('Date clicked:', event.date);
            },
            onEventCreated: function (args) {

            },
            view: {
                calendar: {
                    labels: true,
                },
            },
            data: appointments,
        });

    </script>


    </body>
</html>
