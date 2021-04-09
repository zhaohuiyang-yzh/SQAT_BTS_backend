package bts.demo.bts.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HTTPDataUtil {
    @Value("${bts.ip}")
    private  String URL_String;
    @Value("${bts.username}")
    private  String username;
    @Value("${bts.password}")
    private  String password;

    public  String getAuthorization() throws Exception {
        String param = "username="+username+"&password="+password;
        JSONObject jsonObject = sendPost("/sys/login/restful",  param,"");
        return jsonObject.get("token").toString();
    }
    public  JSONObject sendGet(String address,String param,String token) throws Exception{
        String url_appender = param.equals("")? address : address+"?"+param;
        URL url = new URL(URL_String + url_appender);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        configRequestHeader(connection,"GET",token);
        connection.connect();
        return new JSONObject(getResponse(connection));
    }
    public  JSONObject sendPost(String address,String param,String token) throws Exception{
        URL url = new URL(URL_String + address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        configRequestHeader(connection,"POST",token);

        //输入POST的参数
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        out.write(param);
        out.flush();
        out.close();

        String strResponse = getResponse(connection);
        return new JSONObject(strResponse);
    }
    private static String getResponse(HttpURLConnection connection) throws IOException {
        //获取响应内容的字符串
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String strLine;
        StringBuilder strResponse = new StringBuilder();
        while((strLine =reader.readLine()) != null)
        {
            strResponse.append(strLine).append("\n");
        }
        return strResponse.toString();
    }

    private static void configRequestHeader(HttpURLConnection connection,String method,String token) throws Exception{
        connection.addRequestProperty("Connection", "keep-alive");
        connection.addRequestProperty("Accept", "*/*");
        if(token != null && !token.isEmpty())
            connection.addRequestProperty("login-token", token);//设置获取的token
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64)");
        connection.setRequestMethod(method);
    }
}
