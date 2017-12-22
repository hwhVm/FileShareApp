package beini.com.myapplication.server;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import beini.com.myapplication.bean.SharedFileBean;
import fi.iki.elonen.NanoHTTPD;
/**
 * Create by beini  2017/12/22
 */

public class FileHTTPServer extends NanoHTTPD {
    private static final String REQUEST_ROOT = "/";
    public static List<SharedFileBean> fileLists = new ArrayList<>();

    public FileHTTPServer() {
        super(8080);
    }


    public Response serve(IHTTPSession session) {
        if (REQUEST_ROOT.equals(session.getUri()) || session.getUri().equals("")) {
            return responseRootPage(session);
        }
        return responseFile(session);
    }

    //对于请求根目录的，返回分享的文件列表
    public Response responseRootPage(IHTTPSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPER html><html><body>");
        builder.append("<ol>");
        builder.append("<li>分享文件数量：  " + fileLists.size() + "</li>");
        for (int i = 0, len = fileLists.size(); i < len; i++) {
            File file = new File(fileLists.get(i).getPath());
            if (file.exists()) {
                //文件及下载文件的链接，定义了一个文件类，这里使用getPath方法获得路径，使用getName方法获得文件名
                builder.append("<li> <a href=\"" + file.getPath() + "\">" + file.getName() + "</a></li>");
            }
        }
        builder.append("</ol>");
        builder.append("</body></html>\n");
        return NanoHTTPD.newFixedLengthResponse(String.valueOf(builder));
    }

    //对于请求文件的，返回下载的文件
    public Response responseFile(IHTTPSession session) {
        Log.e("com.beini", "----->responseFile");
        try {
            String uri = session.getUri();  //uri：用于标示文件资源的字符串，这里即是文件路径
            FileInputStream fis = new FileInputStream(uri);
            // 返回OK，同时传送文件，为了安全这里应该再加一个处理，即判断这个文件是否是我们所分享的文件，避免客户端访问了其他个人文件
            return NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "application/octet-stream", fis, fis.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response404(session, null);
    }

    //页面不存在，或者文件不存在时
    public Response response404(IHTTPSession session, String url) {
        Log.e("com.beini", "----->response404");
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html>body>");
        builder.append("S" +
                "<B>404 !</B>");
        builder.append("</body></html>\n");
        return NanoHTTPD.newFixedLengthResponse(builder.toString());
    }
}
