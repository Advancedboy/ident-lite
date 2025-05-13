package com.identlite.api.repository;

import com.identlite.api.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(
        """
        SELECT DISTINCT u FROM User u
        JOIN u.bookings b
        WHERE b.startDate <= :endDate
          AND b.endDate >= :startDate
          AND (:hotelId IS NULL OR b.hotel.id = :hotelId)
        """)
    List<User> findUserWithBookingsInPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("hotelId") Long hotelId);
}