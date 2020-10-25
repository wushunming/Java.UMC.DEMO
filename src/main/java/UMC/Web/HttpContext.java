package UMC.Web;


import UMC.Data.Utility;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpContext implements UMC.Net.NetContext {
    private URL _url;
    private String _userHostAddress;

    public HttpContext(HttpServletRequest req, HttpServletResponse resp, String UserHostAddress, URL url) {
        _url = url;
        _userHostAddress = UserHostAddress;
        this.request = req;
        this.response = resp;
    }

    public String getUserHostAddress() {
        return _userHostAddress;
    }

    public URL getUrl() {
        return _url;
    }

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public int getStatusCode() {
        return response.getStatus();

    }


    public void setStatusCode(int code) {
        response.setStatus(code);

    }

    public String getContentType() {
        return request.getContentType();

    }

    public void setContentType(String contentType) {
        response.setContentType(contentType);

    }


    private String _userAgent;

    public String getUserAgent() {
        if (Utility.isEmpty(_userAgent)) {

            _userAgent = Utility.isNull(request.getHeader("User-Agent"), "UMC");

        }
        return _userAgent;
    }


    public void addHeader(String name, String value) {
        response.setHeader(name, value);

    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public void addCookie(String name, String value) {
        response.addCookie(new Cookie(name, value));
    }

    public String getCookie(String name) {
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies
                ) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c.getValue();
            }
        }
        return null;
    }


    public InputStream getInputStream() throws IOException {

        return request.getInputStream();

    }

    public Enumeration<String> getHeaderNames() {

        return request.getHeaderNames();

    }


    public PrintWriter getOutput() throws IOException {
        return response.getWriter();
    }

    public OutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    private URL _Referrer;

    public URL getUrlReferrer() {
        if (_Referrer == null) {

            String referer = request.getHeader("referer");
            if (Utility.isEmpty(referer) == false) {
                try {
                    _Referrer = new URL(referer);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    _Referrer = this.getUrl();
                }
            } else {

                _Referrer = this.getUrl();
            }
        }
        return _Referrer;
    }


    public String getHttpMethod() {
        return request.getMethod();

    }

    public void redirect(String url) {

        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Map<String, String[]> map;

    public Map<String, String[]> getParameterMap() {
        if (map == null) {
            map = new HashMap<>();


            Set<Map.Entry<String, String[]>> set = request.getParameterMap().entrySet();
            for (Map.Entry<String, String[]> entry : set) {

                String key = entry.getKey();
                try {
                    key = new String(key.getBytes("ISO-8859-1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ;
                String[] values = entry.getValue();
                for (int i = 0; i < values.length; i++) {
                    try {
                        values[i] = new String(values[i].getBytes("ISO-8859-1"), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    ;
                }
                map.put(key, values);
            }

        }

        return map;
    }
}