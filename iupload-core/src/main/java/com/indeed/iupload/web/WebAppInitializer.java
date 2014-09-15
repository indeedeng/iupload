package com.indeed.iupload.web;

import com.google.common.base.Strings;
import com.indeed.iupload.web.config.AppConfiguration;
import com.indeed.iupload.web.config.WebConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.Log4jConfigListener;


@SuppressWarnings({"UnusedDeclaration"})
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {
    private static final Logger log = Logger.getLogger(WebAppInitializer.class);

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        initWebapp(servletContext);

        super.onStartup(servletContext);
    }

    protected void initStaticsHandling(ServletContext servletContext) {
        //no-op. using Spring DefaultServletHandlerConfigurer instead
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfiguration.class, AppConfiguration.class};
    }

    protected void initWebapp(ServletContext servletContext) {
        initLog4j(servletContext);

        initStaticsHandling(servletContext);

        initJSPMapping(servletContext);
    }

    protected void initLog4j(ServletContext servletContext) {
        final String log4jConfigLocationParam = "log4jConfigLocation";
        final String log4jConfigLocation = servletContext.getInitParameter(log4jConfigLocationParam);
        if(Strings.isNullOrEmpty(log4jConfigLocation)) {
            servletContext.setInitParameter(log4jConfigLocationParam, getDefaultLog4jConfigLocation());
        }
        servletContext.setInitParameter("log4jExposeWebAppRoot", "false");

        servletContext.addListener(Log4jConfigListener.class);
    }

    protected void initJSPMapping(ServletContext servletContext) {
        // Map jsp files to jsp servlet
    }

    protected boolean isDeveloperStationEnvironment() {
        final String env = System.getenv("SPRING_PROFILES_DEFAULT");
        return "developer".equals(env);
    }

    protected String getDefaultLog4jConfigLocation() {
        return "/WEB-INF/config/log4j.xml";
    }

    protected String getPropertiesInitializerClass() {
        return PropertiesInitializer.class.getName();
    }

    // Spring AbstractAnnotationConfigDispatcherServletInitializer overrides below

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        String initClass = getPropertiesInitializerClass();
        if(initClass != null) {
            registration.setInitParameter("contextInitializerClasses", initClass);
        }

        super.customizeRegistration(registration);
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[0]; // root ApplicationContext not used by default. everything is in the servlet context
    }
}
