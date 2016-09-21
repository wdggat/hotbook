package com.iread.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientVM {
    private Logger logger = Logger.getLogger(HttpClientVM.class);
    public static HttpClientVM clientVM;

    private final static String USER_AGENT = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; "
            + "Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729;"
            + " Media Center PC 6.0; InfoPath.3; CIBA)";
    private DefaultHttpClient client;
    private HttpContext currentContext;

    // Use HttpClient as singleton for efficiency
    public static HttpClientVM getClientVM() {
        if (clientVM == null) {
            clientVM = new HttpClientVM();
        }
        // Clear context
        clientVM.setCurrentContext(null);
        return clientVM;
    }

    public static void changeIPTo(InetAddress ip) {
        getClientVM();
        clientVM.changeIP(ip);
    }

    public HttpClientVM() {
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        HttpProtocolParams.setUseExpectContinue(params, false);
        HttpConnectionParams.setStaleCheckingEnabled(params, true);
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80,
                PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443,
                SSLSocketFactory.getSocketFactory()));
        ThreadSafeClientConnManager manager =
                new ThreadSafeClientConnManager(schemeRegistry);
        manager.setMaxTotal(200);
        manager.setDefaultMaxPerRoute(20);

        client = new DefaultHttpClient(manager, params);
    }

    private void changeIP(InetAddress ip) {
        logger.info("IP used by HttpClient is set to " + ip.getHostAddress());
        client.getParams().setParameter(ConnRoutePNames.LOCAL_ADDRESS,
                ip);
    }

    // Set user specified context
    public void setCurrentContext(HttpContext context) {
        this.currentContext = context;
    }

    // Make it easy to execute a request
    public String get(String url) {
        return get(url, "utf-8", null);
    }

    public String get(String url, String encoding) {
        return get(url, encoding, null);
    }

    public String get(String url, String encoding,
                      Map<String, String> headMap) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", USER_AGENT);
        if (headMap != null) {
            for (Entry<String, String> entry : headMap.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }

        try {
            HttpResponse response = client.execute(httpGet, currentContext);
            return EntityUtils.toString(response.getEntity(), encoding);
        } catch (IOException ioe) {
            logger.error("Failed to access " + url + " by ", ioe);
            return null;
        }
    }

    public String post(String url, Map<String, String> keyValue) {
        return post(url, keyValue, "utf-8");
    }

    public String post(String url, Map<String, String> keyValue,
                       String encoding) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : keyValue.keySet()) {
            params.add(new BasicNameValuePair(key, keyValue.get(key)));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, encoding));
            HttpResponse response = client.execute(httpPost, currentContext);
            return EntityUtils.toString(response.getEntity(), encoding);
        } catch (IOException ioe) {
            logger.error("Failed to access " + url + " by ", ioe);
            return null;
        }
    }

    private void shutdown() {
        if (client != null) {
            client.getConnectionManager().shutdown();
        }
    }

    public String post(String url, Map<String, String> keyValue,
                       String encoding, boolean isRedirect) throws Exception {
        if (url.startsWith("http")) {

        }
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if (keyValue != null && keyValue.size() > 0) {
            for (String key : keyValue.keySet()) {
                formparams.add(new BasicNameValuePair(key, keyValue.get(key)));
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                encoding);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", USER_AGENT);
        httpPost.setEntity(entity);
        HttpResponse response = client.execute(httpPost, currentContext);
        if (isRedirect && response.getStatusLine().getStatusCode() == 302) {
            return response.getHeaders("location")[0].getValue();
        }
        return EntityUtils.toString(response.getEntity());
    }
}
