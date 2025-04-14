package kg.autoservice.controller;


import kg.autoservice.dto.AppointmentDto;
import kg.autoservice.model.Appointment;
import kg.autoservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Получение всех записей (только для админов)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    /**
     * Получение записи по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    /**
     * Получение записей текущего пользователя
     */
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentDto>> getMyAppointments(Authentication authentication) {
        // В реальном приложении вы бы получили ID пользователя из аутентификации
        // Здесь для примера просто ищем пользователя по имени
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByUser(1L); // Заглушка
        return ResponseEntity.ok(appointments);
    }

    /**
     * Получение записей по статусу (только для админов)
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByStatus(
            @PathVariable Appointment.AppointmentStatus status) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Получение записей на определенную дату (только для админов)
     */
    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(appointments);
    }

    /**
     * Создание новой записи
     */
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(
            @Valid @RequestBody AppointmentDto appointmentDto,
            Authentication authentication) {
        AppointmentDto createdAppointment = appointmentService.createAppointment(
                appointmentDto, authentication.getName());
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    /**
     * Обновление статуса записи (только для админов)
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam Appointment.AppointmentStatus status) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    /**
     * Обновление данных записи
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentDto appointmentDto) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(id, appointmentDto);
        return ResponseEntity.ok(updatedAppointment);
    }

    /**
     * Отмена записи пользователем
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDto> cancelAppointment(
            @PathVariable Long id,
            Authentication authentication) {
        AppointmentDto cancelledAppointment = appointmentService.cancelAppointment(
                id, authentication.getName());
        return ResponseEntity.ok(cancelledAppointment);
    }

    /**
     * Удаление записи (только для админов)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}