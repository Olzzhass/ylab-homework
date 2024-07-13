package kaz.olzhas.ylab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Конфигурационный класс {@code Swagger2Configuration} для настройки Swagger 2 в Spring MVC приложении.
 *
 * <p>Этот класс активирует Swagger 2 через аннотацию {@code @EnableSwagger2} и предоставляет
 * бин {@link Docket}, который настраивает основные параметры Swagger, такие как сканирование
 * контроллеров и пути запросов.</p>
 *
 * <p>Кроме того, класс реализует интерфейс {@link WebMvcConfigurer} для настройки статических ресурсов
 * и представлений, необходимых для работы Swagger UI.</p>
 */
@EnableSwagger2
@Configuration
public class Swagger2Configuration implements WebMvcConfigurer {

    /**
     * Создает бин {@link Docket} для настройки Swagger.
     *
     * @return Docket для Swagger 2
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Настраивает обработчики ресурсов для Swagger UI.
     *
     * @param registry Реестр обработчиков ресурсов
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    /**
     * Добавляет контроллер представлений для перенаправления на Swagger UI.
     *
     * @param registry Реестр контроллеров представлений
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:" + "/swagger-ui/index.html");
    }
}
