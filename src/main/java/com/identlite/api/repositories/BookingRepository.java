package com.identlite.api.repositories;


import com.identlite.api.models.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(
            value = "SELECT b FROM Booking b "
            + "JOIN b.user u "
            + "WHERE u.username = :username"
            + "AND b.status = :status",
            nativeQuery = true)
    List<Booking> findBookingsByUserAndStatus(@Param("username") String username,
                                              @Param("status") String status);

    @Query(
            value = "SELECT * FROM bookings b "
                    + "JOIN users u ON b.user_id = u.id "
                    + "WHERE u.username = :username "
                    + "AND b.status = :status",
            nativeQuery = true
    )

    List<Booking> findBookingsByUserAndStatusNative(@Param("username") String username,
                                                    @Param("status") String status);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByRoomId(Long roomId);

    Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);
}
