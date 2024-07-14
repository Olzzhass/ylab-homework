package kaz.olzhas.ylab.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Класс {@code MainConfig} представляет конфигурацию основного контекста Spring MVC приложения.
 *
 * <p>Он инициализирует контекст приложения, сканирует компоненты в пакете {@code kaz.olzhas.ylab},
 * включает поддержку аспектов через {@code @EnableAspectJAutoProxy}, и настраивает диспетчер
 * сервлетов Spring MVC для обработки входящих запросов.</p>
 *
 * <p>Класс реализует интерфейс {@code WebApplicationInitializer}, что позволяет ему
 * определять конфигурацию приложения и его контекст еще до загрузки контекста Spring.</p>
 */
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan("kaz.olzhas.ylab")
public class MainConfig implements WebApplicationInitializer {

    /**
     * Метод {@code onStartup} инициализирует контекст приложения и регистрирует диспетчер сервлетов.
     *
     * @param servletContext Контекст сервлета для регистрации компонентов и настройки приложения
     * @throws ServletException Исключение, возникающее при ошибке конфигурации сервлета
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MainConfig.class);

        System.out.println("STARTED");

        servletContext.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
