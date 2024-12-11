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
            "AND ua.appointment.startTime > :now ORDER BY ua.appointment.startTime asc")
    List<UserAppointment> findUpcomingAppointments(@Param("authorId") Integer authorId,
                                                   @Param("requestedUserId") Integer requestedUserId,
                                                   @Param("now") LocalDateTime now);


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
            "OR (ua.appointment.startTime >= :startTime AND ua.appointment.startTime < :endTime))")
    List<UserAppointment> findOverlappingAppointments(@Param("userId") Integer userId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

//    List<UserAppointment> findAllByAuthor(User user);

    @Query("SELECT ua FROM UserAppointment ua " +
            "WHERE ua.author.userId = :userId")
    List<UserAppointment> findAllByAuthor(@Param("userId") Integer userId);



    @Query("SELECT ua FROM UserAppointment ua " +
            "WHERE ua.author.userId = :authorId " +
            "AND ua.appointment.startTime >= :startOfDay " +
            "AND ua.appointment.endTime <= :endOfDay")
    List<UserAppointment> findBookingsByAuthorAndDay(
            @Param("authorId") Integer authorId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}
