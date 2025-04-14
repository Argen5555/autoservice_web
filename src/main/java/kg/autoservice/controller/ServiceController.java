package kg.autoservice.controller;

import kg.autoservice.dto.ServiceDto;
import kg.autoservice.service.ServiceCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceCatalogService serviceCatalogService;

    /**
     * Получение списка всех услуг
     */
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        List<ServiceDto> services = serviceCatalogService.getAllServices();
        return ResponseEntity.ok(services);
    }

    /**
     * Получение услуги по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        ServiceDto service = serviceCatalogService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    /**
     * Получение услуг по категории
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ServiceDto>> getServicesByCategory(@PathVariable String category) {
        List<ServiceDto> services = serviceCatalogService.getServicesByCategory(category);
        return ResponseEntity.ok(services);
    }

    /**
     * Получение рекомендуемых услуг
     */
    @GetMapping("/featured")
    public ResponseEntity<List<ServiceDto>> getFeaturedServices() {
        List<ServiceDto> services = serviceCatalogService.getFeaturedServices();
        return ResponseEntity.ok(services);
    }

    /**
     * Поиск услуг по ключевому слову
     */
    @GetMapping("/search")
    public ResponseEntity<List<ServiceDto>> searchServices(@RequestParam String keyword) {
        List<ServiceDto> services = serviceCatalogService.searchServices(keyword);
        return ResponseEntity.ok(services);
    }

    /**
     * Создание новой услуги (только для админов)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDto> createService(@Valid @RequestBody ServiceDto serviceDto) {
        ServiceDto createdService = serviceCatalogService.createService(serviceDto);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    /**
     * Обновление услуги (только для админов)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long id,
                                                    @Valid @RequestBody ServiceDto serviceDto) {
        ServiceDto updatedService = serviceCatalogService.updateService(id, serviceDto);
        return ResponseEntity.ok(updatedService);
    }

    /**
     * Удаление услуги (только для админов)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceCatalogService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}