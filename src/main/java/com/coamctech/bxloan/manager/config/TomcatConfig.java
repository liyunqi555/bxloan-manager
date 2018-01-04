package com.coamctech.bxloan.manager.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * Created by Administrator on 2018/1/2.
 */
@Configuration
public class TomcatConfig {
    @Value("${spring.tomcat.https.keystore}")
    private String key_store;
    @Value("${spring.tomcat.https.keystorePass}")
    private String keystorePass;
    @Value("${spring.tomcat.https.keystoreAlias}")
    private String keystoreAlias;
    @Value("${spring.tomcat.https.port}")
    private Integer port;
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
//            File keystore = new ClassPathResource("tomcat.jks").getFile();
//            File truststore = new ClassPathResource("tomcat.jks").getFile();
            File keystore = new FileSystemResource(key_store).getFile();
           // File truststore = new FileSystemResource(key_store).getFile();
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(port);
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(keystorePass);
            //protocol.setTruststoreFile(truststore.getAbsolutePath());
           // protocol.setTruststorePass(keystorePass);
//            protocol.setKeyAlias("tomcatjks");
//            protocol.setKeyAlias(keystoreAlias);
            return connector;
        }
        catch (Exception ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore"
                    + "] or truststore: [" + "keystore" + "]", ex);
        }
    }
}
