package com.identlite.api.repository;

import com.identlite.api.dto.UserDto;
import com.identlite.api.model.Booking;
import com.identlite.api.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"bookings", "bookings.hotel"})
    Optional<User> findById(Long id);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findBookingsByUserId(@Param("userId") Long userId);
}
