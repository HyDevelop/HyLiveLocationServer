package cc.moecraft.livelocation;

import cc.moecraft.logger.HyLogger;
import cc.moecraft.logger.LoggerInstanceManager;
import cc.moecraft.logger.environments.ColorSupportLevel;
import cc.moecraft.logger.environments.ConsoleColoredEnv;
import cc.moecraft.logger.environments.FileEnv;
import lombok.Getter;
import org.eclipse.jetty.server.Server;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@Getter
public class HyLiveLocationServer
{
    private final HLLConfig config;

    private final LoggerInstanceManager lim = new LoggerInstanceManager(
            new ConsoleColoredEnv(ColorSupportLevel.DEFAULT),
            new FileEnv("./logs/", "HyLiveLocationServer.log"));

    private final HyLogger logger;

    /**
     * 创建一个HyLiveLocationServer对象
     *
     * @param config 配置
     */
    public HyLiveLocationServer(HLLConfig config)
    {
        this.config = config;
        this.logger = lim.getLoggerInstance("Main", config.isDebug());
    }
}
