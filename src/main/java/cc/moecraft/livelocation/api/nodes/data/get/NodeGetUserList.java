package cc.moecraft.livelocation.api.nodes.data.get;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.model.UserInfo;
import cc.moecraft.livelocation.dataset.UserInfoDataset;

import java.util.ArrayList;

import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;
import static java.lang.Boolean.parseBoolean;

/**
 * 此类由 Hykilpikonna 在 2018/12/25 创建!
 * Created by Hykilpikonna on 2018/12/25!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class NodeGetUserList extends HLLApiNode
{
    public NodeGetUserList(HyLiveLocationServer server)
    {
        super(server);
    }

    @Override
    public String nodeName()
    {
        return "data.get.user-list";
    }

    @Override
    public String process(ApiAccess access)
    {
        boolean activeOnly = parseBoolean(access.getHeaders().getOrDefault("active-only", "false"));

        // 创建SQL语句
        String sql = "SELECT * FROM user_info";
        if (activeOnly) sql += " WHERE last_active>=" + (System.currentTimeMillis() - server.getConfig().getInactiveTimeout());

        // 在数据库中找到
        final ArrayList<UserInfoDataset> userInfoDatasets = new ArrayList<>();
        new UserInfo().find(sql).forEach(userInfo -> userInfoDatasets.add(new UserInfoDataset(userInfo)));

        return GSON_WRITE.toJson(userInfoDatasets);
    }
}
