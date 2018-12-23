import cc.moecraft.livelocation.HyLiveLocationLauncher;
import cc.moecraft.utils.FileUtils;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import static cc.moecraft.livelocation.HyLiveLocationLauncher.getServer;

/**
 * 此类由 Hykilpikonna 在 2018/10/07 创建!
 * Created by Hykilpikonna on 2018/10/07!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class ModelGenerator
{
    public static void main(String[] args) throws Exception
    {
        // The package that base model will be generated in
        String baseModelPkg = "cc.moecraft.livelocation.database.model.base";
        String baseModelDir = PathKit.getWebRootPath() + "/src/main/java/cc/moecraft/livelocation/database/model/base/";

        // The package that model will be generated in.
        String modelPkg = "cc.moecraft.livelocation.database.model";
        String modelDir = baseModelDir + "../";

        HyLiveLocationLauncher.main(new String[]{"do-nothing", "-file=./my-secret-config.json"});

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(getServer().getConfig().getDbUrl());
        dataSource.setUser(getServer().getConfig().getDbUsr());
        dataSource.setPassword(getServer().getConfig().getDbPwd());

        Generator generator = new Generator(dataSource, baseModelPkg, baseModelDir, modelPkg, modelDir);
        generator.generate();

        // Replace Model with JbootModel
        replaceText(new File(modelDir),
                "import com.jfinal.plugin.activerecord.Model;", "import io.jboot.JbootModel;",
                " extends Model<M>", " extends JbootModel<M>");
    }

    private static void replaceText(File path, String... text)
    {
        if (path.isDirectory()) // Directory
        {
            File[] files = path.listFiles();
            if (files != null) for (File file : files) replaceText(file, text);
        }
        else
        {
            try // File
            {
                String content = FileUtils.readFileAsString(path);

                for (int i = 0; i < text.length; i += 2)
                {
                    if (i + 1 > text.length) break;

                    String from = text[i];
                    String to = text[i + 1];

                    content = content.replace(from, to);
                }

                // 导出到文件
                try (Writer writer = new FileWriter(path))
                {
                    writer.write(content);
                    writer.flush();
                }
            }
            catch (java.io.IOException e) { throw new RuntimeException(e); }
        }
    }
}
