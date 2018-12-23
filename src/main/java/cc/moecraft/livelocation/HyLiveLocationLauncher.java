package cc.moecraft.livelocation;

import cc.moecraft.utils.FileUtils;
import cc.moecraft.utils.cli.Args;
import cc.moecraft.utils.cli.ArgsUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

import static cc.moecraft.utils.cli.ResourceUtils.readResource;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class HyLiveLocationLauncher
{
    public static void main(String[] argsString) throws Exception
    {
        Args args = ArgsUtils.parse(argsString);
        ArrayList<String> operations = args.getOperations();
        Map<String, String> options = args.getOptions();

        String message = run(operations, options);
        if (message != null && !message.isEmpty()) System.out.println(message);
    }

    private static String run(ArrayList<String> operations, Map<String, String> options) throws Exception
    {
        // 获取操作
        if (operations.size() < 1) return help("");
        String operation = operations.get(0).toLowerCase();
        if (operation.startsWith("help")) return help(operation.equals("help") ? "" : operation.replaceFirst("help-", ""));

        // 获取配置
        String password = options.getOrDefault("password", null);
        int port = parseInt(options.getOrDefault("port", "19486"));
        boolean debug = parseBoolean(options.getOrDefault("debug", "false"));
        String dbUrl = options.getOrDefault("db-url", null);
        String dbUsr = options.getOrDefault("db-usr", null);
        String dbPwd = options.getOrDefault("db-pwd", null);

        // 从文件读取配置
        boolean hasFile = options.containsKey("file");

        // Export操作
        if (operation.equals("export"))
        {
            if (!hasFile) return "How can I export anything if you don't tell me where to export it to?";

            // 导出到文件
            try (Writer writer = new FileWriter(new File(options.get("file"))))
            {
                if (password == null) password = "default-pw";
                if (dbUrl == null) dbUrl = "jdbc:mysql://localhost/my_database?serverTimezone=UTC ";
                if (dbUsr == null) dbUsr = "root";
                if (dbPwd == null) dbPwd = "default-pw";

                new Gson().toJson(new HLLConfig(port, password, debug, dbUrl, dbUsr, dbPwd), writer);
                return "Export Success!";
            }
        }

        // 如果不是Export的话从文件读取
        if (hasFile)
        {
            File file = new File(options.get("file"));
            HLLConfig config = new Gson().fromJson(FileUtils.readFileAsString(file), HLLConfig.class);
            password = config.getPassword();
            port = config.getPort();
            debug = config.isDebug();
            dbUrl = config.getDbUrl();
            dbUsr = config.getDbUsr();
            dbPwd = config.getDbPwd();
        }

        // 检查是否定义了密码
        if (password == null) return "Password is undefined";
        if (dbUrl == null) return "Database URL is undefined";
        if (dbUsr == null) return "Database user is undefined";
        if (dbPwd == null) return "Database password is undefined";

        // 初始化服务器
        HyLiveLocationServer server = new HyLiveLocationServer(new HLLConfig(port, password, debug, dbUrl, dbUsr, dbPwd));

        // 启动服务器
        if (operation.equals("start"))
        {
            server.start();
        }

        return "All done.";
    }

    private static String help(String lang)
    {
        String file = "help";
        switch (lang)
        {
            case "cn": file += "-cn"; break;
        }
        return readResource(HyLiveLocationLauncher.class.getClassLoader(), file);
    }
}
