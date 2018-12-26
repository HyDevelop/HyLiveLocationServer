package cc.moecraft.livelocation.api.nodes.data.get;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.HLLApiNode;

import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;

/**
 * 此类由 Hykilpikonna 在 2018/12/25 创建!
 * Created by Hykilpikonna on 2018/12/25!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class NodeGetServerConfig extends HLLApiNode
{
    public NodeGetServerConfig(HyLiveLocationServer server)
    {
        super(server);
    }

    @Override
    public String nodeName()
    {
        return "data.get.server.config";
    }

    @Override
    public String process(ApiAccess access)
    {
        return GSON_WRITE.toJson(server.getConfig().toBuilder().password("HIDDEN").dbPwd("HIDDEN").dbUrl("HIDDEN").dbUsr("HIDDEN").build());
    }
}
