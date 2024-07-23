package kaz.olzhas.ylab.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Фабрика YamlPropertySourceFactory используется для загрузки YAML-файлов в качестве источников свойств в Spring.
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    /**
     * Создает источник свойств на основе указанного YAML-файла.
     *
     * @param name            имя источника свойств
     * @param encodedResource ресурс, содержащий YAML-файл
     * @return объект PropertySource, представляющий загруженные свойства из YAML-файла
     * @throws IOException если возникает ошибка ввода-вывода при загрузке YAML-файла
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource)
            throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        Properties props = factory.getObject();
        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), props);
    }
}
