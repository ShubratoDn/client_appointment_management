package com.appointment.management.pact.repository;

import com.appointment.management.pact.entity.User;
import com.appointment.management.pact.entity.UserAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserAppointmentRepository extends JpaRepository<UserAppointment, Integer> {
    // Fetch upcoming appointments
    @Query("SELECT ua FROM UserAppointment ua WHERE (ua.author.userId = :authorId " +
            "OR ua.requested_user.userId = :requestedUserId) " +
            "AND ua.appointment.endTime > :now AND ua.status='APPROVED' ORDER BY ua.appointment.startTime asc")
    List<UserAppointment> findUpcomingAppointments(@Param("authorId") Integer authorId,
                                                   @Param("requestedUserId") Integer requestedUserId,
                                                   @Param("now") LocalDateTime now);


    @Query("SELECT ua FROM UserAppointment ua WHERE ua.author.userId = :authorId " +
            "AND ua.appointment.endTime > :now AND ua.status = :status ORDER BY ua.appointment.startTime asc")
    List<UserAppointment> findAuthorsUpcomingAppointmentsWithStatus(@Param("authorId") Integer authorId,
                                                   @Param("now") LocalDateTime now,
                                                    @Param("status") String status);

    @Query("SELECT ua FROM UserAppointment ua WHERE ua.requested_user.userId = :authorId " +
            "AND ua.appointment.endTime > :now AND ua.status = :status ORDER BY ua.appointment.startTime asc")
    List<UserAppointment> findRequestedUsersUpcomingAppointmentsWithStatus(@Param("authorId") Integer authorId,
                                                                    @Param("now") LocalDateTime now,
                                                                    @Param("status") String status);


//    @Query("SELECT COUNT(ua) > 0 FROM UserAppointment ua " +
//            "WHERE ua.author.userId = :userId " +
//            "AND ((ua.appointment.startTime <= :endTime AND ua.appointment.endTime >= :startTime) " +
//            "OR (ua.appointment.startTime >= :startTime AND ua.appointment.startTime <= :endTime))")
//    boolean isUserAvailable(@Param("userId") Integer userId,
//                            @Param("startTime") LocalDateTime startTime,
//                            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT ua FROM UserAppointment ua " +
            "WHERE ua.author.userId = :userId " +
            "AND ((ua.appointment.startTime < :endTime AND ua.appointment.endTime > :startTime) " +
            "OR (ua.appointment.startTime >= :startTime AND ua.appointment.startTime < :endTime))" +
            "AND ua.status='APPROVED'")
    List<UserAppointment> findOverlappingAppointments(@Param("userId") Integer userId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

//    List<UserAppointment> findAllByAuthor(User user);

    @Query("SELECT ua FROM UserAppointment ua " +
            "WHERE (ua.author.userId = :userId " +
            "OR ua.requested_user.userId = :userId )" +
            "AND ua.status='APPROVED' " )
    List<UserAppointment> findAllByAuthor(@Param("userId") Integer userId);



    @Query("SELECT ua FROM UserAppointment ua " +
            "WHERE (ua.author.userId = :authorId OR ua.requested_user.userId = :authorId) " +
            "AND ua.appointment.startTime >= :startOfDay " +
            "AND ua.appointment.endTime <= :endOfDay " +
            "AND ua.status='APPROVED'")
    List<UserAppointment> findBookingsByAuthorAndDay(
            @Param("authorId") Integer authorId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}
