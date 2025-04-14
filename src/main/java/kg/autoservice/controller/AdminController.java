package kg.autoservice.controller;


import kg.autoservice.dto.AppointmentDto;
import kg.autoservice.dto.ReviewDto;
import kg.autoservice.dto.ServiceDto;
import kg.autoservice.dto.UserDto;
import kg.autoservice.model.Appointment;
import kg.autoservice.service.AppointmentService;
import kg.autoservice.service.ReviewService;
import kg.autoservice.service.ServiceCatalogService;
import kg.autoservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ServiceCatalogService serviceCatalogService;
    private final AppointmentService appointmentService;
    private final ReviewService reviewService;

    /**
     * Получение статистики для дашборда
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Общее количество пользователей
        List<UserDto> users = userService.getAllUsers();
        stats.put("totalUsers", users.size());

        // Общее количество услуг
        List<ServiceDto> services = serviceCatalogService.getAllServices();
        stats.put("totalServices", services.size());

        // Количество записей по статусам
        List<AppointmentDto> pendingAppointments = appointmentService.getAppointmentsByStatus(Appointment.AppointmentStatus.PENDING);
        List<AppointmentDto> confirmedAppointments = appointmentService.getAppointmentsByStatus(Appointment.AppointmentStatus.CONFIRMED);
        List<AppointmentDto> kgpletedAppointments = appointmentService.getAppointmentsByStatus(Appointment.AppointmentStatus.COMPLETED);

        stats.put("pendingAppointments", pendingAppointments.size());
        stats.put("confirmedAppointments", confirmedAppointments.size());
        stats.put("kgpletedAppointments", kgpletedAppointments.size());

        // Количество отзывов, требующих модерации
        List<ReviewDto> pendingReviews = reviewService.getPendingReviews();
        stats.put("pendingReviews", pendingReviews.size());

        return ResponseEntity.ok(stats);
    }

    /**
     * Получение последних записей
     */
    @GetMapping("/latest-appointments")
    public ResponseEntity<List<AppointmentDto>> getLatestAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        // В реальном приложении здесь была бы выборка только последних записей с сортировкой
        return ResponseEntity.ok(appointments);
    }

    /**
     * Получение последних отзывов
     */
    @GetMapping("/latest-reviews")
    public ResponseEntity<List<ReviewDto>> getLatestReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        // В реальном приложении здесь была бы выборка только последних отзывов с сортировкой
        return ResponseEntity.ok(reviews);
    }
}