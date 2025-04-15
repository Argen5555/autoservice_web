package kg.autoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Разрешаем все источники (для разработки)
        config.addAllowedOrigin("*");

        // Разрешаем все заголовки и методы
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // Важно: установите это в false для разрешения * в allowedOrigins
        config.setAllowCredentials(false);

        // Применяем ко всем путям
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}