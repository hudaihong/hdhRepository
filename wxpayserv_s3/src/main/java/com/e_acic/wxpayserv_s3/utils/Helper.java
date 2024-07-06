package com.e_acic.wxpayserv_s3.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;


/*
工具模块，加解密
* */
public class Helper {
    private Helper() {
    }

    //密钥 (需要前端和后端保持一致)`
    private  final String KEY = "abcdefgabcdefg12";
    private static Helper instance = null;
    //    线程安全的单例模式
    public static Helper getInstance() {
        // 这个条件, 判定是否要加锁. 如果对象已经有了, 就不必加锁了, 此时本身就是线程安全的.
        if (instance == null) {
            synchronized (Helper.class) {
                if (instance == null) {
                    instance = new Helper();
                }
            }
        }
        return instance;
    }



    /**
     * 加密
     */
    public  String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }

        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }


    /**
     * 解密
     */
    public  String Decrypt(String sSrc, String sKey)  {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /*
    发送https请求，可能需要验证证书
    * */
    public  String httpsRequest(String requestUrl, String requestMethod, String outputStr)  {
        // 初始化一个json对象
        String result = "";

        // 创建SSLContext对象，并使用我们指定的信任管理器初始化

        try {
            TrustManager[] tmManagers = new TrustManager[0];
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tmManagers, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory sslSocket = sslContext.getSocketFactory();
            URI _uri = new URI(requestUrl);
            URL _url = _uri.toURL();
            HttpsURLConnection _conn = (HttpsURLConnection) _url.openConnection();
            _conn.setSSLSocketFactory(sslSocket);
            _conn.setInstanceFollowRedirects(true);
            //设置超时时间为10秒
            _conn.setConnectTimeout(10000);
            _conn.setReadTimeout(10000);

            _conn.setDoOutput(true);
            _conn.setDoInput(true);
            _conn.setUseCaches(false);
            // 设置请求方式 GET/POST
            _conn.setRequestMethod(requestMethod);
            // 不考虑大小写。如果两个字符串的长度相等，并且两个字符串中的相应字符都相等（忽略大小写），则认为这两个字符串是相等的。
            if ("GET".equalsIgnoreCase(requestMethod)) {
                _conn.connect();
            }
            // 当有数据需要提交时,往服务器端写内容 也就是发起http请求需要带的参数
            if (outputStr != null) {
                OutputStream outputStream = _conn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            int code = _conn.getResponseCode();
            if (code == 200) {
                // 获得输入流 读取服务器端返回的内容
                InputStream inputStream = _conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                // 释放资源
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                _conn.disconnect();
                // 将字符串转换为json对象
                result = stringBuffer.toString();
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    /*
    * 发送http请求
    * */
    private String httpSend(String requestUrl, String requestMethod, String outputStr)  {
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;

        URI _uri = null;
        try {
            _uri = new URI(requestUrl);
            URL _url = _uri.toURL();
            HttpURLConnection _conn = (HttpURLConnection) _url.openConnection();
            // 如果通过post传递参数，可能需要设置为true,get参数都在url后面，设置为false
            _conn.setDoOutput(false);
            // 需要读取返回数据设置为true
            _conn.setDoInput(true);
            // 设置请求方式
            _conn.setRequestMethod("GET");
            // 设置是否使用缓存
            _conn.setUseCaches(true);
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            _conn.setInstanceFollowRedirects(true);
            // 设置超时时间
            _conn.setConnectTimeout(3000);
            if ("GET".equalsIgnoreCase(requestMethod)) {
                _conn.connect();
            }
            // 当有数据需要提交时,往服务器端写内容 也就是发起http请求需要带的参数
            if (outputStr != null) {
                OutputStream outputStream = _conn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 4. 得到响应状态码的返回值 responseCode
            int code = _conn.getResponseCode();
            // 5. 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            String msg = "";
            if (code == 200) { // 正常响应
                InputStream inputStream = _conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String str = null;
                StringBuffer stringBuffer = new StringBuffer();
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                // 释放资源
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                _conn.disconnect();
                // 将字符串转换为json对象
                result = stringBuffer.toString();
            }

            // 显示响应结果
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}