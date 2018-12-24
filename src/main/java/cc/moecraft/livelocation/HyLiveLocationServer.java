package cc.moecraft.livelocation;

import cc.moecraft.livelocation.api.ApiHandler;
import cc.moecraft.livelocation.api.nodes.misc.NodeTest;
import cc.moecraft.livelocation.utils.CryptUtils;
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
 * Meow!
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

    /**
     * 启动HTTP服务器
     *
     * @throws Exception 出错了
     */
    public void start() throws Exception
    {
        // 创建Api监听器对象
        ApiHandler handler = new ApiHandler(this);
        handler.getManager().register(
                new NodeTest()
        );

        // 创建Jetty服务器对象
        Server server = new Server(config.getPort());
        server.setHandler(handler);

        // 启动Jetty服务器
        server.start();
        server.join();
    }

    public String encrypt(String text)
    {
        return CryptUtils.encrypt(text, getConfig().getPassword());
    }

    public String decrypt(String text)
    {
        return CryptUtils.decrypt(text, getConfig().getPassword());
    }
}
