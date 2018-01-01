package com.coamctech.bxloan.manager;


import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/10/20.
 */

@SpringBootApplication
public class Application extends SpringBootServletInitializer {


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector());
        return tomcat;
    }
    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            File keystore = new ClassPathResource("tomcat.jks").getFile();
            File truststore = new ClassPathResource("tomcat.jks").getFile();
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(443);
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass("lirong");
            protocol.setTruststoreFile(truststore.getAbsolutePath());
            protocol.setTruststorePass("lirong");
            protocol.setKeyAlias("tomcatjks");
            return connector;
        }
        catch (IOException ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore"
                    + "] or truststore: [" + "keystore" + "]", ex);
        }
    }

}