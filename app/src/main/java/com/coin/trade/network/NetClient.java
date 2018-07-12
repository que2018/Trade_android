package com.coin.trade.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by richen on 2015/11/2.
 */
public class NetClient {
    private static final String TAG = NetClient.class.getSimpleName();
    private static NetClient instance = null;
    private static final int CONNECTION_TIMEOUT = 30000;

//    private CookieStore cookieStore;
//    private HttpContext localContext;


    HttpCookieManager cm;

//    private  Context context;

    public enum Method {GET, POST}

    ;

    private NetClient() {
//
//        cookieStore = new BasicCookieStore();
//        localContext = new BasicHttpContext();
//
//        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        cm = new HttpCookieManager();
    }

    public static NetClient getInstance() {
        if (instance == null) {
            instance = new NetClient();
        }

        return instance;
    }

//    public void setContext(Context context) {
//        this.context = context;
//    }

    public interface Response {
        public void responseJSON(JSONObject result);
    }

    public void request(Method method, String url, Map<String, String> params, Response response) {
        new HttpTask(method, url, params, response).execute((Void) null);
    }

    public class HttpTask extends AsyncTask<Void, Void, Boolean> {
        private Method mMethod;
        private String mUrl;
        private Map<String, String> mParams;
        private Response mResponse;

        private JSONObject mResult;

        HttpTask(Method method, String url, Map<String, String> params, Response response) {
            mMethod = method;
            mUrl = url;
            mParams = params;
            mResponse = response;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            mResult = null;
            if (mMethod == Method.GET) {
                mResult = httpRequest(mUrl);
            } else if (mMethod == Method.POST) {
                mResult = postRequest(mUrl, mParams, null, null);
            } else {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (mResponse != null)
                mResponse.responseJSON(mResult);
        }

        @Override
        protected void onCancelled() {
        }
    }

    /**
     * 是否有可用网络
     */
    public boolean hasActiveNet(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    public static boolean hasWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }

        return false;
    }

//    public class MyTLSSocketFactory extends SSLSocketFactory {
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//
//        public MyTLSSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException, IOException, CertificateException {
//            super(truststore);
//
//            TrustManager tm = new X509TrustManager() {
//                public void checkClientTrusted(X509Certificate[] chain, String authType) {
//                }
//
//                public void checkServerTrusted(X509Certificate[] chain, String authType) {
//                }
//
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//            };
//
////            AssetManager assetManager =context.getAssets();
////            InputStream is = assetManager.open("cloudflare_origin_ecc.pem");
//////            InputStream is = new FileInputStream("cloudflare_origin_ecc.pem");
////            CertificateFactory cf = CertificateFactory.getInstance("X.509");
////            Certificate ca = cf.generateCertificate(is);
////
////            truststore.setCertificateEntry("anchor", ca);
////
////            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
////            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
////            trustManagerFactory.init(truststore);
////            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
////
////            sslContext.init(null, trustManagers, null);
//            sslContext.init(null, new TrustManager[]{tm}, null);
//        }
//
//        @Override
//        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
//            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
//        }
//
//        @Override
//        public Socket createSocket() throws IOException {
//            return sslContext.getSocketFactory().createSocket();
//        }
//    }
//
//    private HttpClient getHttpClient() {
//        HttpClient httpClient = null;
//        try {
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            trustStore.load(null, null);
////            trustStore.load(null);
//
//            MyTLSSocketFactory sf = new MyTLSSocketFactory(trustStore);
//            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//            HttpParams httpparams = new BasicHttpParams();
//            HttpProtocolParams.setVersion(httpparams, HttpVersion.HTTP_1_1);
//            HttpProtocolParams.setContentCharset(httpparams, HTTP.UTF_8);
//
//            SchemeRegistry registry = new SchemeRegistry();
//            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//            registry.register(new Scheme("https", sf, 443));
//
//            ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpparams, registry);
//
//            httpClient = new DefaultHttpClient(ccm, httpparams);
//        } catch (Exception e) {
//            httpClient = new DefaultHttpClient();
//        }
//
//        return httpClient;
//
////        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
////
////        DefaultHttpClient client = new DefaultHttpClient();
////
////        SchemeRegistry registry = new SchemeRegistry();
////        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
////        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
////        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
////        registry.register(new Scheme("https", socketFactory, 443));
////        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
////        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
////
////// Set verifier
////        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
////
////        return httpClient;
//
//
//    }

    public InputStream getStream(String strUrl) {
        try {
            URL realUrl = new URL(strUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            cm.setCookies(conn);
            // 建立实际的连接
            conn.connect();
            cm.storeCookies(conn);

            return conn.getInputStream();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        return null;
    }
//    public InputStream getStream(String url) {
//        HttpClient httpClient = getHttpClient();
//
//        HttpGet get = new HttpGet(url);
////        HttpPost httpPost = new HttpPost(url);
//        try {
////            HttpResponse response = httpClient.execute(httpPost);
//            HttpResponse response = httpClient.execute(get, localContext);
//            int status = response.getStatusLine().getStatusCode();
//
//            if (200 == status) {
//                return response.getEntity().getContent();
//            }
//        } catch (Exception e) {
//            Log.i(TAG, e.getMessage());
//        }
//
//        return null;
//    }

    //    BitmapFactory.Options options = new BitmapFactory.Options();
//    options.inJustDecodeBounds = false;
//    options.inSampleSize = 5; // width，hight设为原来的十分一
    public Bitmap getBitmap(String url, BitmapFactory.Options options) {
        Bitmap bitmap;
        InputStream inputStream = getStream(url);
        if (options != null) {
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } else {
            bitmap = BitmapFactory.decodeStream(inputStream);
        }

        return bitmap;
    }

    public Bitmap getBitmap(String url) {
        return getBitmap(url, null);
    }

    public JSONObject httpRequest(String strUrl) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(strUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            cm.setCookies(conn);
            // 建立实际的连接
            conn.connect();
            cm.storeCookies(conn);

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader( new InputStreamReader(conn.getInputStream()) );
            String line;
            while ((line = in.readLine()) != null) {
                if (!"".equals(result)) {
                    result += "/n" + line;
                }
                else {
                    result += line;
                }
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JSONObject jsonData =  null;
        try {
            jsonData = new JSONObject(result);
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        return jsonData;
    }
//    public JSONObject httpRequest(String url) {
//        HttpClient httpClient = getHttpClient();
//
//        JSONObject jsonData =  null;
//                                                                                                                                       ;
//        HttpGet httpGet = new HttpGet(url);
//        try {
//            HttpResponse httpResponse = httpClient.execute(httpGet, localContext);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            String result = EntityUtils.toString(httpEntity, HTTP.UTF_8);
//            jsonData = new JSONObject(result);
//        }
//        catch (IOException e) {}
//        catch (JSONException e) {}
//
//        return jsonData;
//    }

    /**
     * Get 请求
     *
     * @param url
     */
//    public String[] getRequest(String url, String cookie) {
//        HttpParams httpparams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpparams, CONNECTION_TIMEOUT);
//        HttpClient client = new DefaultHttpClient(httpparams);
//        HttpGet get = new HttpGet(url);
//
//        if (cookie != null) {
//            get.addHeader("Cookie", cookie);
//        }
//
//        try {
//            HttpResponse response = client.execute(get, localContext);
//            int statusCode = response.getStatusLine().getStatusCode();
//            HttpEntity entity = response.getEntity();
//            String result = EntityUtils.toString(entity, "utf-8");
//            String message = "";
//
//            if (statusCode == 200) {
//                message = "ok";
//            } else if (statusCode == 500) {
//                message = "服务器内部错误";
//            } else {
//                if (result != null) {
//                    JSONObject obj = new JSONObject(result);
//                    if (obj != null && obj.has("error")) {
//                        message = obj.getString("error");
//                    } else {
//                        message = "发生未知错误";
//                    }
//                } else {
//                    message = "发生未知错误";
//                }
//            }
//
//            return new String[] { statusCode + "", result, message };
//        } catch (Exception e) {
//            Log.i(TAG, e.getMessage());
//        }
//
//        return null;
//    }

    /**
     * Post 请求
     */
    public JSONObject postRequest(String strUrl, Map<String, String> params, Map<String, String> headers, String cookie) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            StringBuilder sb = new StringBuilder();
            if(params!=null && !params.isEmpty()){
                for(Map.Entry<String, String> entry : params.entrySet()){
                    sb.append(entry.getKey()).append('=');
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    sb.append('&');
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            byte[] entity = sb.toString().getBytes();
            HttpURLConnection conn = (HttpURLConnection) new URL(strUrl).openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            cm.setCookies(conn);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
            OutputStream outStream = conn.getOutputStream();
            outStream.write(entity);

            cm.storeCookies(conn);

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader( new InputStreamReader(conn.getInputStream()) );
            String line;
            while ((line = in.readLine()) != null) {
                if (!"".equals(result)) {
                    result += "/n" + line;
                }
                else {
                    result += line;
                }
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        JSONObject jsonData =  null;
        try {
            jsonData = new JSONObject(result);
         } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        return jsonData;
    }
//    public JSONObject postRequest(String url, Map<String, String> params, Map<String, String> headers, String cookie) {
//        HttpClient httpClient = getHttpClient();
//
////        HttpParams httpparams = new BasicHttpParams();
////        HttpConnectionParams.setConnectionTimeout(httpparams, CONNECTION_TIMEOUT);
////        HttpClient client = new DefaultHttpClient(httpparams);
//        HttpPost post = new HttpPost(url);
//
//
//
//        // 添加 headers
//        if (headers != null) {
//            for (Map.Entry<String, String> item : headers.entrySet()) {
//                String key = item.getKey();
//                String value = item.getValue();
//                post.addHeader(key, value);
//            }
//        }
//
//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        // 添加 form 参数
//        if (params != null) {
//            for (Map.Entry<String, String> item : params.entrySet()) {
//                String key = item.getKey();
//                String value = item.getValue();
//                BasicNameValuePair basic = new BasicNameValuePair(key, value);
//                nvps.add(basic);
//            }
//        }
//
//        // 如果有 cookie 则添加
//        if (cookie != null) {
//            post.addHeader("Cookie", cookie);
//        }
//
//        JSONObject jsonData =  null;
//        try {
//            HttpEntity entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
//            post.setEntity(entity);
//            HttpResponse response = httpClient.execute(post, localContext);
//            int statusCode = response.getStatusLine().getStatusCode();
//            String result = EntityUtils.toString(response.getEntity(), "utf-8");
//
//            jsonData = new JSONObject(result);
//        } catch (Exception e) {
//            Log.i(TAG, e.getMessage());
//        }
//
//        return jsonData;
//    }
}
