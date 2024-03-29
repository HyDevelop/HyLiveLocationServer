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
 * Meow!
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
        return "data.get.user.list";
    }

    /**
     * 获取用户列表
     *  - active-only (boolean):   是否只获取活跃用户
     *  - inactive-only (boolean): 是否只获取不活跃用户
     *
     * @param access Api access.
     * @return 用户列表
     */
    @Override
    public String process(ApiAccess access)
    {
        boolean activeOnly = parseBoolean(access.getHeaders().getOrDefault("active-only", "false"));
        boolean inactiveOnly = parseBoolean(access.getHeaders().getOrDefault("inactive-only", "false"));

        // 如果同时只获取活跃和不活跃的话, 就是什么都没有啦w
        if (activeOnly && inactiveOnly) return GSON_WRITE.toJson(new ArrayList<>());

        // 创建SQL语句
        String sql = "SELECT * FROM user_info";
        if (activeOnly || inactiveOnly) sql += " WHERE";
        if (activeOnly) sql += " last_active>=" + (System.currentTimeMillis() - server.getConfig().getInactiveTimeout());
        if (inactiveOnly) sql += " last_active<" + (System.currentTimeMillis() - server.getConfig().getInactiveTimeout());

        // 在数据库中找到
        final ArrayList<UserInfoDataset> userInfoDatasets = new ArrayList<>();
        new UserInfo().find(sql).forEach(userInfo -> userInfoDatasets.add(new UserInfoDataset(userInfo)));

        return GSON_WRITE.toJson(userInfoDatasets);
    }
}
