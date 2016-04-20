package com.sharpdeep.assistant_android.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bear on 16-4-19.
 */
public class NetUtil {

    //判断是否有网络连接
    public static boolean isNetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }

    //判断是否连接WIFI
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null){
            return false;
        }else{
            return connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
        }
    }

    //wifi是否打开
    public static boolean isWifiEnable(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }


    public static boolean openWifi(Context context){
        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            ToastUtil.show((Activity) context,"正在打开wifi");
            return wifiManager.setWifiEnabled(true);
        }
        return true;
    }

    public static boolean connectSSID(Context context,String ssid){
        if (isWifiEnable(context) && isWifiConnected(context)){
            Map info = getWifiInfo(context);
            if (info != null && info.get("SSID").equals("\""+ssid+"\"")){
                return true;
            }
        }
        return false;
    }

    public static Map<String,String> getWifiInfo(Context context){
        if (!isWifiConnected(context)){
            return null;
        }
        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

        Map<String,String> wifiInfoMap = new HashMap<String,String>();
        wifiInfoMap.put("SSID",wifiManager.getConnectionInfo().getSSID());
        wifiInfoMap.put("BSSID",wifiManager.getConnectionInfo().getBSSID());
        wifiInfoMap.put("mac",wifiManager.getConnectionInfo().getMacAddress());
        wifiInfoMap.put("rssi", String.valueOf(wifiManager.getConnectionInfo().getRssi()));
        wifiInfoMap.put("linkspeed", String.valueOf(wifiManager.getConnectionInfo().getLinkSpeed()));
        wifiInfoMap.put("ip", FormatString(dhcpInfo.ipAddress));
        wifiInfoMap.put("mask",FormatString(dhcpInfo.netmask));
        wifiInfoMap.put("netgate",FormatString(dhcpInfo.gateway));
        wifiInfoMap.put("dns",FormatString(dhcpInfo.dns1));

        return wifiInfoMap;
    }

    public static List<HashMap<String,String>> getAllWifiInfo(Context context){
        if (!isWifiEnable(context)){
            return null;
        }

        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
        wifiManager.startScan();
        ArrayList<ScanResult> scanResultList = (ArrayList<ScanResult>) wifiManager.getScanResults();

        sortByLevel(scanResultList);

        ArrayList<HashMap<String,String>> allWifiInfoList = new ArrayList<>();

        for(ScanResult scanResult : scanResultList){
            HashMap<String,String> wifiInfoMap = new HashMap<>();
            wifiInfoMap.put("SSID",scanResult.SSID);
            wifiInfoMap.put("BSSID",scanResult.BSSID);
            wifiInfoMap.put("level", String.valueOf(scanResult.level));
            wifiInfoMap.put("tostring",scanResult.toString());
            allWifiInfoList.add(wifiInfoMap);
        }

        return allWifiInfoList;
    }

    public static String FormatString(int value){
        String strValue="";
        byte[] ary = intToByteArray(value);
        for(int i=ary.length-1;i>=0;i--){
            strValue += (ary[i] & 0xFF);
            if(i>0){
                strValue+=".";
            }
        }
        return strValue;
    }
    private static byte[] intToByteArray(int value){
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++){
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }

    //将搜索到的wifi根据信号强度从强到弱进行排序
    private static  void sortByLevel(ArrayList<ScanResult> list) {
        for(int i=0;i<list.size()-1;i++)
            for(int j=i+1;j<list.size();j++)
            {
                if(list.get(i).level<list.get(j).level)    //level属性即为强度
                {
                    ScanResult temp = null;
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
    }
}
