package cc.moecraft.livelocation;

import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.DataLog;
import cc.moecraft.livelocation.utils.encryption.Encryptor;
import cc.moecraft.utils.FileUtils;
import cc.moecraft.utils.cli.Args;
import cc.moecraft.utils.cli.ArgsUtils;
import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

import static cc.moecraft.livelocation.HLLConstants.GSON_PRETTY;
import static cc.moecraft.livelocation.HLLConstants.GSON_READ;
import static cc.moecraft.utils.cli.ResourceUtils.readResource;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

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
    @Getter
    private static HyLiveLocationServer server;

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
        long activeTimeout = parseLong(options.getOrDefault("active-timeout", "" + new HLLServerConfig().getInactiveTimeout()));
        long locationLogTime = parseLong(options.getOrDefault("location-log-time", "" + new HLLServerConfig().getLocationLogTime()));

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

                GSON_PRETTY.toJson(new HLLServerConfig(port, password, debug, dbUrl, dbUsr, dbPwd, activeTimeout, locationLogTime), writer);
                return "Export Success!";
            }
        }

        // 如果不是Export的话从文件读取
        if (hasFile)
        {
            File file = new File(options.get("file"));
            HLLServerConfig config = GSON_READ.fromJson(FileUtils.readFileAsString(file), HLLServerConfig.class);
            password = config.getPassword();
            port = config.getPort();
            debug = config.isDebug();
            dbUrl = config.getDbUrl();
            dbUsr = config.getDbUsr();
            dbPwd = config.getDbPwd();
            activeTimeout = config.getInactiveTimeout();
            locationLogTime = config.getLocationLogTime();
        }

        // 检查是否定义了密码
        if (password == null) return "Password is undefined";
        if (dbUrl == null) return "Database URL is undefined";
        if (dbUsr == null) return "Database user is undefined";
        if (dbPwd == null) return "Database password is undefined";

        // 初始化服务器
        server = new HyLiveLocationServer(new HLLServerConfig(port, password, debug, dbUrl, dbUsr, dbPwd, activeTimeout, locationLogTime));

        // Start操作, 启动服务器
        if (operation.equals("start"))
        {
            server.start();
        }

        // Migrate操作,
        if (operation.equals("migrate"))
        {
            if (!options.containsKey("old-password")) return "Old password is undefined for migration";
            String oldPassword = options.get("old-password");

            Encryptor o = new Encryptor(oldPassword);
            Encryptor n = new Encryptor(password);

            // 初始化数据库
            server.getDatabaseInitializer().initialize();

            int count = 1;

            for (DataLatest dataLatest : new DataLatest().findAll())
            {
                dataLatest.setLongitude(n.encrypt(o.decrypt(dataLatest.getLongitude())));
                dataLatest.setLatitude(n.encrypt(o.decrypt(dataLatest.getLatitude())));
                dataLatest.update();
                System.out.println("Updated " + (count ++) + " data_latest records.");
            }

            count = 1;

            for (DataLog dataLog : new DataLog().findAll())
            {
                dataLog.setLongitude(n.encrypt(o.decrypt(dataLog.getLongitude())));
                dataLog.setLatitude(n.encrypt(o.decrypt(dataLog.getLatitude())));
                dataLog.update();
                System.out.println("Updated " + (count ++) + " data_log records.");
            }
            return "Migration complete";
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
