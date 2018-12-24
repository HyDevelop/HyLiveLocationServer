import cc.moecraft.livelocation.client.HLLApiClient;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class HLLApiClientTester
{
    public static void main(String[] args)
    {
        HLLApiClient client = new HLLApiClient("localhost:19486/api");
        System.out.println(client.send("misc.test", "Test request body", "something", "someValue"));

        /*HttpRequest request = HttpUtil.createPost("localhost:19486/api");
        request.header("node", "misc.test").header("something", "someValue").body("Test request body");
        HttpResponse response = request.execute();
        System.out.println(response);

        // Incorrect usage
        System.out.println(HttpUtil.get("localhost:19486/some-other-things"));*/
    }
}
