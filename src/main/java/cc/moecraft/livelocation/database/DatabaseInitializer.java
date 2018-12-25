package cc.moecraft.livelocation.database;

import cc.moecraft.livelocation.HLLConfig;
import cc.moecraft.livelocation.HyLiveLocationServer;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import lombok.Getter;

import static cc.moecraft.livelocation.database.model._MappingKit.mapping;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@Getter
public class DatabaseInitializer
{
    private C3p0Plugin c3p0Plugin;
    private ActiveRecordPlugin activeRecordPlugin;
    private final HyLiveLocationServer server;

    /**
     * Create a database initializer object.
     *
     * @param server Server
     */
    public DatabaseInitializer(HyLiveLocationServer server)
    {
        this.server = server;
    }

    /**
     * Initialize database.
     */
    public void initialize()
    {
        HLLConfig config = server.getConfig();
        c3p0Plugin = new C3p0Plugin(config.getDbUrl(), config.getDbUsr(), config.getDbPwd(), "com.mysql.cj.jdbc.Driver");
        c3p0Plugin.start();
        activeRecordPlugin = new ActiveRecordPlugin(c3p0Plugin);
        mapping(activeRecordPlugin);
        activeRecordPlugin.start();
    }
}
