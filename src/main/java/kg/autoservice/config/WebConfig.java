package kg.autoservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Настройка обработки статических ресурсов
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Маршрутизация для HTML страниц
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/about").setViewName("about");
        registry.addViewController("/services").setViewName("services");
        registry.addViewController("/prices").setViewName("prices");
        registry.addViewController("/reviews").setViewName("reviews");
        registry.addViewController("/contacts").setViewName("contacts");
        registry.addViewController("/login").setViewName("login");
    }

    // Убираем метод addCorsMappings - будем использовать только CorsConfig
}