package cc.moecraft.livelocation.api.nodes.data.get;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.dataset.LocationDataset;

import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;

/**
 * 此类由 Hykilpikonna 在 2018/12/25 创建!
 * Created by Hykilpikonna on 2018/12/25!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class NodeGetUserLocation extends HLLApiNode
{
    public NodeGetUserLocation(HyLiveLocationServer server)
    {
        super(server);
    }

    @Override
    public String nodeName()
    {
        return "data.get.user.location";
    }

    /**
     * 获取用户位置
     *  - username (str): 用户名
     *
     * @param access Api access.
     * @return 用户最后一次提交的位置
     */
    @Override
    public String process(ApiAccess access)
    {
        if (!access.getHeaders().containsKey("username")) return "Error: Username not specified";

        // 获取用户
        DataLatest dataLatest = new DataLatest().findById(access.getHeaders().get("username"));
        if (dataLatest == null) return "Error: User does not exist";

        return GSON_WRITE.toJson(new LocationDataset(server.getEncryptor(), dataLatest));
    }
}
