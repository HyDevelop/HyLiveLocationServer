import cc.moecraft.livelocation.HyLiveLocationLauncher;

/**
 * 此类由 Hykilpikonna 在 2018/12/25 创建!
 * Created by Hykilpikonna on 2018/12/25!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class DatabaseTest
{
    public static void main(String[] args) throws Exception
    {
        HyLiveLocationLauncher.main(new String[]{"do-nothing", "-file=./my-secret-config.json"});
        HyLiveLocationLauncher.getServer().getDatabaseInitializer().initialize();

        System.out.println("");
    }
}
