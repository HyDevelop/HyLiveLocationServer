import cc.moecraft.livelocation.client.HLLApiClient;
import cc.moecraft.livelocation.dataset.LocationDataset;
import cc.moecraft.livelocation.utils.encryption.Encryptor;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class HLLApiClientTester
{
    public static void main(String[] args)
    {
        HLLApiClient client = new HLLApiClient("localhost:19486/api", new Encryptor("default-pw"));
        System.out.println("-------------------=[ Send Test Request ]=--------------------");
        System.out.println(client.send("misc.test", "Test request body", "something", "someValue"));

        System.out.println("---------------=[ Update TestUser Location ]=-----------------");
        System.out.println(client.sendSetLocationDataset(new LocationDataset("testUser", 47d, -12d)));
        // System.out.println(client.sendSetLocationDataset(new LocationDataset("inactiveUser", 45d, -12d)));


    }
}
