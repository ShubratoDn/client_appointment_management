<!DOCTYPE html>
<html lang="en">
    <head>
        <%@include file="_partials/headerLink.jsp"%>
        <title>Book an Appointment - PACT</title>
    </head>
    <body class="sb-nav-fixed">

        <%@include file="_partials/navbar.jsp"%>

        <div id="layoutSidenav">

            <%@include file="_partials/sideNav.jsp"%>

            <div id="layoutSidenav_content">
                <main>
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">Book Appointment</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active">Book Appointment</li>
                        </ol>
                        <div class="row">
                           <div class="d-flex justify-content-between align-items-center">
                               <div class="mb-3 w-75">
                                   <label for="useroremail">Username or Email</label>
                                   <input id="useroremail" class="form-control" placeholder="Search User" />
                               </div>
                               <button type="button" class="btn btn-success" id="searchBtn">Search</button>
                           </div>

                            <div class="user-info-list" id="user-info-list">

                            </div>

                        </div>
                    </div>
                </main>
                <%@include file="_partials/footer.jsp"%>
            </div>
        </div>

<%--        footer links--%>
        <%@include file="_partials/footerLink.jsp"%>




        <script>
            // Function to fetch and render user data
            function fetchUsers(query) {
                $.ajax({
                    url: '<%= request.getContextPath() %>/users/search', // Endpoint
                    method: 'GET',
                    data: { searchQuery: query }, // Send the query as a request parameter
                    success: function (response) {
                        // Empty the user info list container
                        $('#user-info-list').empty();

                        if (response && response.length > 0) {
                            console.log(response)
                            // Iterate over the users and add them to the UI
                            response.forEach(user => {
                                var fullname = user.fullname
                                $('#user-info-list').append(`
                                <div class="user-info mb-2 border rounded p-3 d-flex align-items-baseline justify-content-between">
                                    <div>
                                        <h4>`+fullname+`</h4>
                                        <p class="m-0">Username: `+user.username+`</p>
                                        <p class="m-0">Email: `+user.email+`</p>
                                    </div>
                                    <a href="/appointment-request/`+user.userId+`"+user.userId+"" class="btn btn-info">Request Appointment</a>
                                </div>
                            `);
                            });
                        } else {
                            $('#user-info-list').append('<p class="text-muted">No users found.</p>');
                        }
                    },
                    error: function () {
                        alert('An error occurred while fetching user data.');
                    }
                });
            }

            $(document).ready(function () {
                // Event listener for the search input field
                $('#useroremail').on('keyup', function () {
                    const query = $(this).val().trim();
                    fetchUsers(query); // Fetch users as you type
                });

                // Event listener for the search button
                $('#searchBtn').on('click', function () {
                    const query = $('#useroremail').val().trim();
                    fetchUsers(query); // Fetch users on button click
                });
            });
        </script>
    </body>
</html>
