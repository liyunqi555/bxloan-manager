package com.coamctech.bxloan.manager.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import javax.net.ssl.*;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by tiansj on 15/8/19.
 */
public class HttpRestClient {

    private static Client client;

    static {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }
                    }
            }, null);
            ClientConfig config = new DefaultClientConfig();
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }, ctx));
            client = Client.create(config);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static ClientResponse get(String url) {
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u);
            ClientResponse response = resource.get(ClientResponse.class);
            return response;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse get(HashMap header, String url) {
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u);
            WebResource.Builder builder = wrapHeader(resource, header);
            ClientResponse response = builder.get(ClientResponse.class);
            return response;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse post(String url, Object params) {
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u);
            ClientResponse result = resource.post(ClientResponse.class, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse post(String url, MultivaluedMap params) {
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u);
            ClientResponse result = resource.queryParams(params).post(ClientResponse.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse post(HashMap header, String url, MultivaluedMap params) {
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u);
            resource = resource.queryParams(params);
            WebResource.Builder builder = wrapHeader(resource, header);
            ClientResponse result = builder.post(ClientResponse.class);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse post(HashMap header, String url, Object requestEntity) {
        if (header == null || header.keySet().size() == 0) {
            return post(url, requestEntity);
        }
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u);
            WebResource.Builder builder = wrapHeader(resource, header);
            ClientResponse result = builder.post(ClientResponse.class, requestEntity);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse delete(String url) {
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u.toString());
            ClientResponse response = resource.delete(ClientResponse.class);
            return response;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse delete(HashMap header, String url) {
        if (header == null || header.keySet().size() == 0) {
            return delete(url);
        }
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u.toString());
            WebResource.Builder builder = wrapHeader(resource, header);
            ClientResponse response = builder.delete(ClientResponse.class);
            return response;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ClientResponse put(String url, Object requestEntity) {
        try {
            URI u = new URI(url);
            WebResource resource = client.resource(u.toString());
            ClientResponse response = resource.put(ClientResponse.class, requestEntity);
            return response;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientResponse put(HashMap header, String url, Object requestEntity) {
        if (header == null || header.keySet().size() == 0) {
            return put(url, requestEntity);
        }
        WebResource resource = client.resource(url);
        WebResource.Builder builder = wrapHeader(resource, header);
        ClientResponse response = builder.put(ClientResponse.class, requestEntity);
        return response;
    }

    private static WebResource.Builder wrapHeader(WebResource resource, HashMap header) {
        WebResource.Builder builder = resource.header("Content-Type", "application/json");
        Set<String> set = header.keySet();
        for (String key : set) {
            builder = builder.header(key, header.get(key).toString());
        }
        return builder;
    }

}
