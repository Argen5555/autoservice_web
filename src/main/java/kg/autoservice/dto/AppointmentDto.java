package kg.autoservice.dto;

import kg.autoservice.model.Appointment.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

    private Long id;

    private Long userId;

    private String userName;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    private String serviceName;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime dateTime;

    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;

    private AppointmentStatus status;

    @Size(max = 100, message = "Car model must not exceed 100 characters")
    private String carModel;

    private Integer carYear;

    @Size(max = 20, message = "Car registration number must not exceed 20 characters")
    private String carRegNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}