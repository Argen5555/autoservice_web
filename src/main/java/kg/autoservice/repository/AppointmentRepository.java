package kg.autoservice.repository;


import kg.autoservice.model.Appointment;
import kg.autoservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUser(User user);

    List<Appointment> findByStatus(Appointment.AppointmentStatus status);

    List<Appointment> findByUserAndStatus(User user, Appointment.AppointmentStatus status);

    List<Appointment> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(Appointment.AppointmentStatus status);
}