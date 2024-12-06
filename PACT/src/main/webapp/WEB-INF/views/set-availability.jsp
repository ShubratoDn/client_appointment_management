<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="_partials/headerLink.jsp" %>
    <title>Book an Appointment - PACT</title>
    <!-- Mobiscroll JS and CSS Includes -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/mobiscroll.javascript.min.css">
    <script src="<%= request.getContextPath() %>/js/mobiscroll.javascript.min.js"></script>
</head>
<body class="sb-nav-fixed">

<%@include file="_partials/navbar.jsp" %>

<div id="layoutSidenav">

    <%@include file="_partials/sideNav.jsp" %>

    <div id="layoutSidenav_content">
        <main>
            <div class="container-fluid px-4">
                <h1 class="mt-4">Set Availability</h1>
                <div class="row">

                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Working hour starts</span>
                        </div>
                        <input type="time" value="08:30" name="workingHourStarts" id="workingHourStarts" placeholder="Working hour starts" class="form-control">
                    </div>

                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">Working hour Ends</span>
                        </div>
                        <input type="time" value="17:30" name="workingHourEnds" id="workingHourEnds" placeholder="Working hour starts" class="form-control">
                    </div>


                    <div class="mbsc-col-sm-12 mbsc-col-md-4">
                        <div class="mbsc-form-group">
                            <h3 class="">Select Your Available Days</h3>
                            <div id="demo-multi-day"></div>
                        </div>
                    </div>

                    <!-- Submit Button -->
                    <div class="mbsc-col-sm-12">
                        <button id="submitBtn" class="btn btn-primary">Submit</button>
                    </div>
                </div>
            </div>
        </main>
        <%@include file="_partials/footer.jsp" %>
    </div>
</div>

<%-- Footer links --%>
<%@include file="_partials/footerLink.jsp" %>

<script>
    // Inject available dates from the server-side model
    var availableDates = [
        <%
            List<String> dates = (List<String>) request.getAttribute("availableDates");
            for (int i = 0; i < dates.size(); i++) {
                out.print("'" + dates.get(i) + "'");
                if (i < dates.size() - 1) {
                    out.print(", ");
                }
            }
        %>
    ];
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

    var selectedDates = [];

    // Initialize the Mobiscroll calendar with multiple date selection
    mobiscroll.datepicker('#demo-multi-day', {
        controls: ['calendar'],
        display: 'inline',
        selectMultiple: true,
        select: 'date',
        min: min,
        max: max,
        onChange: function(event, inst) {
            console.log(event.value);
            selectedDates = event.value;
        },
        onInit: function (event, inst) {
            inst.setVal(availableDates, true);
        },
    });

    document.getElementById('submitBtn').addEventListener('click', function () {
        // Collect the working hours
        const workingHourStarts = document.getElementById('workingHourStarts').value;
        const workingHourEnds = document.getElementById('workingHourEnds').value;

        if (!workingHourStarts || !workingHourEnds) {
            alert('Please enter your working hours.');
            return;
        }

        if (selectedDates.length === 0) {
            alert('Please select at least one available date.');
            return;
        }

        // Data to be sent to the backend
        const requestData = {
            workingHourStarts: workingHourStarts,
            workingHourEnds: workingHourEnds,
            availableDates: selectedDates
        };

        // jQuery AJAX POST request
        $.ajax({
            url: '/set-availability',        // The URL to send the request to
            method: 'POST',                  // HTTP method
            contentType: 'application/json', // Set content type header
            data: JSON.stringify(requestData), // Send the data as a JSON string
            success: function(data) {
                alert('Availability set successfully!');
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                alert('Error setting availability.');
            }
        });

    });


</script>




<script>
    // Wait for the page to load completely
    window.addEventListener('load', function() {
        // Get all the text nodes in the document
        const textNodes = document.createTreeWalker(
            document.body,
            NodeFilter.SHOW_TEXT,
            null,
            false
        );

        let currentNode;
        while (currentNode = textNodes.nextNode()) {
            // Check if the node's text contains 'TRIAL' (case-sensitive)
            if (currentNode.nodeValue.includes('TRIAL')) {
                // Remove the text by setting the content to an empty string
                currentNode.nodeValue = currentNode.nodeValue.replace('TRIAL', '');
            }
        }
    });

</script>

</body>
</html>
