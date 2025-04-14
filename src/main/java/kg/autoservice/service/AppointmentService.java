package kg.autoservice.service;


import kg.autoservice.dto.AppointmentDto;
import kg.autoservice.exception.ResourceNotFoundException;
import kg.autoservice.model.Appointment;
import kg.autoservice.model.Service;
import kg.autoservice.model.User;
import kg.autoservice.repository.AppointmentRepository;
import kg.autoservice.repository.ServiceRepository;
import kg.autoservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    /**
     * Получение всех записей
     */
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение записи по ID
     */
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
        return convertToDto(appointment);
    }

    /**
     * Получение записей для конкретного пользователя
     */
    public List<AppointmentDto> getAppointmentsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        return appointmentRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение записей по статусу
     */
    public List<AppointmentDto> getAppointmentsByStatus(Appointment.AppointmentStatus status) {
        return appointmentRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение записей пользователя по статусу
     */
    public List<AppointmentDto> getAppointmentsByUserAndStatus(Long userId, Appointment.AppointmentStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        return appointmentRepository.findByUserAndStatus(user, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение записей на определенный день
     */
    public List<AppointmentDto> getAppointmentsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        return appointmentRepository.findByDateTimeBetween(startOfDay, endOfDay).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Создание новой записи
     */
    @Transactional
    public AppointmentDto createAppointment(AppointmentDto appointmentDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Service service = serviceRepository.findById(appointmentDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + appointmentDto.getServiceId()));

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setDateTime(appointmentDto.getDateTime());
        appointment.setComment(appointmentDto.getComment());
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);
        appointment.setCarModel(appointmentDto.getCarModel());
        appointment.setCarYear(appointmentDto.getCarYear());
        appointment.setCarRegNumber(appointmentDto.getCarRegNumber());

        appointmentRepository.save(appointment);

        return convertToDto(appointment);
    }

    /**
     * Обновление статуса записи (для администраторов)
     */
    @Transactional
    public AppointmentDto updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        return convertToDto(appointment);
    }

    /**
     * Обновление данных записи
     */
    @Transactional
    public AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        // Обновляем только те поля, которые можно менять
        if (appointmentDto.getDateTime() != null) {
            appointment.setDateTime(appointmentDto.getDateTime());
        }

        if (appointmentDto.getComment() != null) {
            appointment.setComment(appointmentDto.getComment());
        }

        if (appointmentDto.getServiceId() != null && !appointmentDto.getServiceId().equals(appointment.getService().getId())) {
            Service service = serviceRepository.findById(appointmentDto.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + appointmentDto.getServiceId()));
            appointment.setService(service);
        }

        if (appointmentDto.getCarModel() != null) {
            appointment.setCarModel(appointmentDto.getCarModel());
        }

        if (appointmentDto.getCarYear() != null) {
            appointment.setCarYear(appointmentDto.getCarYear());
        }

        if (appointmentDto.getCarRegNumber() != null) {
            appointment.setCarRegNumber(appointmentDto.getCarRegNumber());
        }

        appointmentRepository.save(appointment);

        return convertToDto(appointment);
    }

    /**
     * Отмена записи пользователем
     */
    @Transactional
    public AppointmentDto cancelAppointment(Long id, String username) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Проверка, что запись принадлежит пользователю
        if (!appointment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only cancel your own appointments");
        }

        // Проверка, что запись еще не выполнена и не отменена
        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED ||
                appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot cancel appointment that is already kgpleted or cancelled");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        return convertToDto(appointment);
    }

    /**
     * Удаление записи (для администраторов)
     */
    @Transactional
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointmentRepository.delete(appointment);
    }

    /**
     * Конвертация сущности в DTO
     */
    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setUserId(appointment.getUser().getId());
        dto.setUserName(appointment.getUser().getFullName());
        dto.setServiceId(appointment.getService().getId());
        dto.setServiceName(appointment.getService().getName());
        dto.setDateTime(appointment.getDateTime());
        dto.setComment(appointment.getComment());
        dto.setStatus(appointment.getStatus());
        dto.setCarModel(appointment.getCarModel());
        dto.setCarYear(appointment.getCarYear());
        dto.setCarRegNumber(appointment.getCarRegNumber());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());

        return dto;
    }
}