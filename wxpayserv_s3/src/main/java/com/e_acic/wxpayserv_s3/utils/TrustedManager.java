package com.e_acic.wxpayserv_s3.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/*
信任一切证书
* */
public class TrustedManager implements X509TrustManager {

    /*
    * 检查客户端证书
    * */
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }
    /*
    * 检查服务端证书
    * */
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }
    /**/
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        //return new X509Certificate[0];
        return null;
    }
}
