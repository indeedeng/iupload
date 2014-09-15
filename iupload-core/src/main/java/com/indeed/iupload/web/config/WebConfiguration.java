package com.indeed.iupload.web.config;

//import com.indeed.squall.common.web.configurations.StaticMapperConfiguration;
//import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = "com.indeed.iupload")
//@Import(StaticMapperConfiguration.class)
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    Environment env;

    public static
    @Bean
    PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public
    @Bean
    CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
