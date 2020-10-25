package UMC.Web;

import UMC.Data.Utility;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class HttpServlet extends javax.servlet.http.HttpServlet  {

    @Override
    public void init() throws ServletException {
        super.init();
        if (!WebServlet.isScanning()) WebServlet.scanningClass(false);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.ProcessRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.ProcessRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.ProcessRequest(req, resp);
    }

    void ProcessRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf8");
        StringBuffer stringBuffer = req.getRequestURL();

        String query = req.getQueryString();
        if (Utility.isEmpty(query) == false) {
            stringBuffer.append("?");
            stringBuffer.append(query);
        }
        URL Url = new URL(stringBuffer.toString());//.toString());

        String ip = req.getRemoteAddr();

        String xRIP = req.getHeader("X-Real-IP");
        if (Utility.isEmpty(xRIP) == false) {
            ip = xRIP;
        }
        String chost = req.getHeader("CA-Host");

        if (Utility.isEmpty(chost) == false) {
            Url = new URL(String.format("https://%s%s%s", chost, Url.getPath(), Url.getQuery()));
        }

        new WebServlet().processRequest(new HttpContext(req, resp, ip, Url));

    }


}
