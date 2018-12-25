package cc.moecraft.livelocation.api.nodes.data.get;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.model.UserInfo;
import cc.moecraft.livelocation.dataset.UserInfoDataset;

import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;

/**
 * 此类由 Hykilpikonna 在 2018/12/25 创建!
 * Created by Hykilpikonna on 2018/12/25!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class NodeGetUserInfo extends HLLApiNode
{
    public NodeGetUserInfo(HyLiveLocationServer server)
    {
        super(server);
    }

    @Override
    public String nodeName()
    {
        return "data.get.user-info";
    }

    @Override
    public String process(ApiAccess access)
    {
        if (!access.getHeaders().containsKey("username")) return "Error: Username not specified";

        // 获取用户
        UserInfo userInfo = new UserInfo().findById(access.getHeaders().get("username"));
        if (userInfo == null) return "Error: User does not exist";

        return GSON_WRITE.toJson(new UserInfoDataset(userInfo));
    }
}
