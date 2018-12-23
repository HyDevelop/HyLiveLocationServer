import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class HLLClientTester
{
    public static void main(String[] args)
    {
        HttpRequest request = HttpUtil.createPost("localhost:19486/api");
        request.header("node", "misc.test").header("something", "someValue").body("Test request body");
        HttpResponse response = request.execute();
        System.out.println(response);
    }
}
