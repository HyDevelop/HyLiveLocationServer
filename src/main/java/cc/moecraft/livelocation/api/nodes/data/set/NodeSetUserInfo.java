package cc.moecraft.livelocation.api.nodes.data.set;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.DataValidator;
import cc.moecraft.livelocation.database.model.UserInfo;
import cc.moecraft.livelocation.dataset.UserInfoDataset;

import static cc.moecraft.livelocation.HLLConstants.GSON_READ;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class NodeSetUserInfo extends HLLApiNode
{
    public NodeSetUserInfo(HyLiveLocationServer server)
    {
        super(server);
    }

    @Override
    public String nodeName()
    {
        return "data.set.user-info";
    }

    @Override
    public String process(ApiAccess access)
    {
        // Parse dataset
        String json = access.getHeaders().get("dataset");
        UserInfoDataset dataset = GSON_READ.fromJson(json, UserInfoDataset.class);

        UserInfo info = DataValidator.validateUser(dataset.getUsername());
        info.updateFromDataset(dataset);

        return "Success";
    }
}
