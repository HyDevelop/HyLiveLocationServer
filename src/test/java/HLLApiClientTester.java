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
        client.ping();

        System.out.println("\n-------------------=[ 获取延迟 ]=--------------------");
        System.out.println(client.ping() + " ms");

        System.out.println("\n-------------------=[ 上传位置 ]=--------------------");
        System.out.println(client.sendSetLocationDataset(new LocationDataset("testUser", 47d, -12d))); // 返回 Success

        System.out.println("\n-----------------=[ 获取用户列表 ]=------------------");
        System.out.println(client.getUserListAll()); // 返回 ArrayList<UserInfoDataset>

        System.out.println("\n---------------=[ 获取活跃用户列表 ]=----------------");
        System.out.println(client.getUserListActive());

        System.out.println("\n--------------=[ 获取不活跃用户列表 ]=---------------");
        System.out.println(client.getUserListInactive());

        System.out.println("\n-----------------=[ 获取用户位置 ]=------------------");
        System.out.println(client.getUserLocation("testUser")); // 返回 LocationDataset

        System.out.println("\n-----------------=[ 获取历史位置 ]=------------------");
        System.out.println(client.getUserLocationHistory("testUser")); // 返回 ArrayList<LocationDataset>

        System.out.println("\n-----------------=[ 获取配置文件 ]=------------------");
        System.out.println(client.getServerConfig()); // 返回 HLLConfig
    }
}
