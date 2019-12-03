
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static List<String> list = new ArrayList<>();
    private static String userName = "************"; //学号
    private static String key = "***********"; //身份证号
    private static Document document;
    public static void main(String[] args) {
        list = getLoginInfo();
        try {
            Connection connection = Jsoup.connect(ZFURL.ZFLOGIN5);
            connection.cookie("ASP.NET_SessionId", list.get(0))
                    .referrer(ZFURL.ZFLOGIN5)
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Content-Type", "application/x-www-form-urlencoded");
            HashMap<String, String> data = new HashMap<>();
            data.put("__VIEWSTATE", list.get(1));
            data.put("TextBox1", userName);
            data.put("TextBox2", key);
            data.put("RadioButtonList1", "%D1%A7%C9%FA");
            data.put("Button1", "");
            data.put("lbLanguage", "");
            connection.data(data);
            document = connection.post();
            Document courseInfo = getCourse(ZFURL.ZFBASE5 + document.select("a[onclick=GetMc('学生个人课表');]").first().attr("href"));
            Elements trs = courseInfo.select("tr");
            for(int i = 0;i < trs.size();i++){
                System.out.println(i);
                System.out.println(trs.get(i).select("td"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static List<String> getLoginInfo()  {
        List<String> result = new ArrayList<>();
        try {
            Connection.Response response;
            Connection connection = Jsoup.connect(ZFURL.ZFLOGIN5);
            response = connection.method(Connection.Method.GET).execute();
            result.add(response.cookie("ASP.NET_SessionId"));
            result.add(response.parse().select("input[name=__VIEWSTATE]").attr("value"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private static Document getCourse(String url){
        Connection connection = Jsoup.connect(url);
        Document result;
        try{
            connection.cookie("ASP.NET_SessionId", list.get(0))
                    .referrer("http://jwxt.jiangnan.edu.cn/jndx/xs_main.aspx?xh=" + userName);
            result = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
            result = new Document("");
        }
        return result;
    }
}
