package kaz.olzhas.ylab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kaz.olzhas.ylab.liquibase.LiquibaseDemo;
import kaz.olzhas.ylab.mapper.BookingMapper;
import kaz.olzhas.ylab.mapper.UserMapper;
import kaz.olzhas.ylab.mapper.WorkspaceMapper;
import kaz.olzhas.ylab.service.AdminService;
import kaz.olzhas.ylab.service.UserService;
import kaz.olzhas.ylab.service.WorkspaceService;
import kaz.olzhas.ylab.state.Authentication;
import kaz.olzhas.ylab.util.ConnectionManager;
import kaz.olzhas.ylab.util.PropertiesUtil;
import org.mapstruct.factory.Mappers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Application context for managing beans and dependencies.
 */

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    private Properties properties;
    private ConnectionManager connectionManager;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Authentication authentication = new Authentication();
        servletContext.setAttribute("authentication", authentication);
        servletContext.setAttribute("objectMapper", objectMapper);
        servletContext.setAttribute("userMapper", Mappers.getMapper(UserMapper.class));
        servletContext.setAttribute("workspaceMapper", Mappers.getMapper(WorkspaceMapper.class));
        servletContext.setAttribute("bookingMapper", Mappers.getMapper(BookingMapper.class));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void loadProperties(ServletContext servletContext){
        if(properties == null){
            properties = new Properties();
            try {
                properties.load(servletContext.getResourceAsStream("/WEB-INF/classes/application.properties"));
                servletContext.setAttribute("servletProperties", properties);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Property file not found!");
            }catch (IOException e){
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }

    private void databaseConfiguration(ServletContext servletContext){
        String url = PropertiesUtil.get("db.url");
        String username = PropertiesUtil.get("db.username");
        String password = PropertiesUtil.get("db.password");
        String driver = PropertiesUtil.get("db.driver");

        connectionManager = new ConnectionManager(url, username, password, driver);
        servletContext.setAttribute("connectionManager", connectionManager);

        String changeLogFile = PropertiesUtil.get("liquibase.change-log");
        String schemaName = PropertiesUtil.get("liquibase.liquibase-schema");

        if(Boolean.parseBoolean(PropertiesUtil.get("liquibase.enabled"))){
            LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
            liquibaseDemo.runMigrations();
            servletContext.setAttribute("liquibaseDemo", liquibaseDemo);
            System.out.println("Migration successful");
        }
    }

    private void serviceContextInit(ServletContext servletContext){

        UserService userService = new UserService(connectionManager);
        WorkspaceService workspaceService = new WorkspaceService(userService, connectionManager);
        AdminService adminService = new AdminService(workspaceService, userService, connectionManager);


        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("workspaceService", workspaceService);
        servletContext.setAttribute("adminService", adminService);
    }
}
