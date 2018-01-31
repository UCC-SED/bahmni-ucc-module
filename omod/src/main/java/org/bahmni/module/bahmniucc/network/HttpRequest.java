package org.bahmni.module.bahmniucc.network;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by ucc-ian on 02/Jan/2018.
 */
public class HttpRequest {

    private Logger logger = Logger.getLogger(getClass());

    public String httpGetRequest(String url, String token, String contentType) {
        try {
            HttpClientBuilder b = HttpClientBuilder.create();

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            b.setSslcontext(sslContext);

            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory)
                    .build();

            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            b.setConnectionManager(connMgr);

            HttpClient httpclient = b.build();

            HttpGet get = new HttpGet(url);

            get.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
            get.setHeader(HttpHeaders.AUTHORIZATION, token);


            HttpResponse response = httpclient.execute(get);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


            logger.error(" URL " + url);
            logger.error(" token " + token);
            logger.error(" CODE " + response.getStatusLine().getStatusCode());
            logger.error(" REASON " + response.getStatusLine().getReasonPhrase());

            String responseBody = EntityUtils.toString(response.getEntity());


            JSONObject responseObject = new JSONObject();

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseObject.put("httpStatusCode", response.getStatusLine().getStatusCode())
                        .put("httpReasonPhrase", response.getStatusLine().getReasonPhrase())
                        .put("httpResponseBody", JSONObject.NULL);
                return responseBody;
            } else {
                responseObject.put("httpStatusCode", response.getStatusLine().getStatusCode())
                        .put("httpReasonPhrase", response.getStatusLine().getReasonPhrase())
                        .put("httpResponseBody", responseBody);
                //	return responseObject.toString();
                return responseBody;
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String httpPostRequest(String url, String content,  String token, String contentType) {
        try {
            HttpClientBuilder b = HttpClientBuilder.create();

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            b.setSslcontext(sslContext);

            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory)
                    .build();

            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            b.setConnectionManager(connMgr);

            HttpClient httpclient = b.build();

            HttpPost post = new HttpPost(url);

            HttpEntity entity = new ByteArrayEntity(content.getBytes("UTF-8"));

            post.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
            post.addHeader("Jump-Accepted", "true");
            post.setEntity(entity);

            HttpResponse response = httpclient.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            logger.error(" CODE " + response.getStatusLine().getStatusCode());
            logger.error(" REASON " + response.getStatusLine().getReasonPhrase());

            String responseBody = EntityUtils.toString(response.getEntity());
            logger.error(" content " + content);
            logger.error(" contentType " + contentType);

            JSONObject responseObject = new JSONObject();

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseObject.put("httpStatusCode", response.getStatusLine().getStatusCode())
                        .put("httpReasonPhrase", response.getStatusLine().getReasonPhrase())
                        .put("httpResponseBody", JSONObject.NULL);
                return responseBody;
            } else {
                responseObject.put("httpStatusCode", response.getStatusLine().getStatusCode())
                        .put("httpReasonPhrase", response.getStatusLine().getReasonPhrase())
                        .put("httpResponseBody", responseBody);
                //	return responseObject.toString();
                return responseBody;
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }
}
