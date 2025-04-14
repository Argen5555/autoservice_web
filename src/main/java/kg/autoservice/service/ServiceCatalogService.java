package kg.autoservice.service;


import kg.autoservice.dto.ServiceDto;
import kg.autoservice.exception.ResourceNotFoundException;
import kg.autoservice.model.Service;
import kg.autoservice.repository.ReviewRepository;
import kg.autoservice.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Получение всех услуг
     */
    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение услуги по ID
     */
    public ServiceDto getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        return convertToDto(service);
    }

    /**
     * Получение услуг по категории
     */
    public List<ServiceDto> getServicesByCategory(String category) {
        return serviceRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение рекомендуемых услуг
     */
    public List<ServiceDto> getFeaturedServices() {
        return serviceRepository.findByFeaturedTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Поиск услуг по ключевому слову
     */
    public List<ServiceDto> searchServices(String keyword) {
        return serviceRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Создание новой услуги
     */
    @Transactional
    public ServiceDto createService(ServiceDto serviceDto) {
        Service service = new Service();
        updateServiceFromDto(service, serviceDto);

        serviceRepository.save(service);
        return convertToDto(service);
    }

    /**
     * Обновление услуги
     */
    @Transactional
    public ServiceDto updateService(Long id, ServiceDto serviceDto) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        updateServiceFromDto(service, serviceDto);

        serviceRepository.save(service);
        return convertToDto(service);
    }

    /**
     * Удаление услуги
     */
    @Transactional
    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        serviceRepository.delete(service);
    }

    /**
     * Обновление сущности из DTO
     */
    private void updateServiceFromDto(Service service, ServiceDto dto) {
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setDuration(dto.getDuration());
        service.setCategory(dto.getCategory());
        service.setFeatured(dto.getFeatured() != null ? dto.getFeatured() : false);
        service.setImageUrl(dto.getImageUrl());
    }

    /**
     * Конвертация сущности в DTO
     */
    private ServiceDto convertToDto(Service service) {
        ServiceDto dto = new ServiceDto();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setDuration(service.getDuration());
        dto.setCategory(service.getCategory());
        dto.setFeatured(service.getFeatured());
        dto.setImageUrl(service.getImageUrl());

        // Получение среднего рейтинга
        Double avgRating = reviewRepository.getAverageRatingForService(service.getId());
        dto.setAverageRating(avgRating);

        return dto;
    }
}
