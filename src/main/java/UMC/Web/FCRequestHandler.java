package UMC.Web;

import UMC.Data.Utility;
import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class FCRequestHandler implements HttpRequestHandler, FunctionInitializer {
    @Override
    public void initialize(Context context) throws IOException {
        Utility.ROOTPATH = "/home/UMC/";
        String path = System.getenv("PATH");
        if (Utility.isEmpty(path) == false) {
            Utility.ROOTPATH = "/home/UMC/" + Utility.trim(path, '/') + "/";
        }
        String[] pkgs = Utility.isNull(System.getenv("PACKAGE"), "UMC").split(",");
        WebServlet.scanningClass(true, pkgs);
    }

    class FCContext extends HttpContext {

        public FCContext(HttpServletRequest req, HttpServletResponse resp, String UserHostAddress, URL url) {
            super(req, resp, UserHostAddress, url);
        }

        @Override
        public Map<String, String[]> getParameterMap() {

            return request.getParameterMap();
        }
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context) throws IOException, ServletException {

        Utility.ROOTPATH = "/home/UMC/";
        String path = System.getenv("PATH");
        if (Utility.isEmpty(path) == false) {
            Utility.ROOTPATH = "/home/UMC/" + Utility.trim(path, '/') + "/";
        }

        if (WebServlet.isScanning() == false) {

            String[] pkgs = Utility.isNull(System.getenv("PACKAGE"), "UMC").split(",");

            WebServlet.scanningClass(true, pkgs);
        }
        String FC_REQUEST_PATH = (String) request.getAttribute("FC_REQUEST_PATH");
        String requestURI = (String) request.getAttribute("FC_REQUEST_URI");
        int i = requestURI.indexOf('?');
        if (i > 0) {
            FC_REQUEST_PATH += requestURI.substring(i);
        }
        URL requestUrl = new URL(new URL(request.getRequestURL().toString()), FC_REQUEST_PATH);
        String requestClientIP = (String) request.getAttribute("FC_REQUEST_CLIENT_IP");

        new WebServlet().processRequest(new FCContext(request, response, requestClientIP, requestUrl));

    }
}
