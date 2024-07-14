package kaz.olzhas.ylab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Конфигурационный класс {@code WebConfiguration} для настройки HTTP сообщений в Spring MVC приложении.
 *
 * <p>Этот класс реализует интерфейс {@link WebMvcConfigurer} и переопределяет метод
 * {@code configureMessageConverters}, чтобы настроить преобразователи сообщений HTTP. Он использует
 * библиотеку Jackson для сериализации и десериализации JSON и включает отступы для улучшения читаемости
 * вывода JSON.</p>
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * Настраивает преобразователи сообщений HTTP для JSON.
     *
     * @param converters Список преобразователей сообщений HTTP
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true); // Отступы для читаемости JSON

        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
}
