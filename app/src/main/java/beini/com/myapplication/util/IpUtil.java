package beini.com.myapplication.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Create by beini  2017/12/22
 */
public class IpUtil {

    public static String getDeviceIp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (!wifiManager.isWifiEnabled()) {//判断wifi是否开启
            wifiManager.setWifiEnabled(true);
            Toast.makeText(context, "wifi已经打开，请连接网络", Toast.LENGTH_SHORT).show();
        }
        int ip = wifiInfo.getIpAddress();
        return (ip & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }
}
