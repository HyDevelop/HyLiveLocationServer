package cc.moecraft.livelocation.api.nodes.data.get;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.model.DataLog;
import cc.moecraft.livelocation.dataset.LocationDataset;

import java.util.ArrayList;

import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;
import static cc.moecraft.logger.utils.FormatUtils.resolve;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;

/**
 * 此类由 Hykilpikonna 在 2018/12/25 创建!
 * Created by Hykilpikonna on 2018/12/25!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class NodeGetUserLocationHistory extends HLLApiNode
{
    public NodeGetUserLocationHistory(HyLiveLocationServer server)
    {
        super(server);
    }

    @Override
    public String nodeName()
    {
        return "data.get.user.location.history";
    }

    /**
     * 获取用户位置历史
     *  - username (str): 用户名
     *  - start (long):   起始日期 (ms)
     *  - end (long):     结束日期 (ms)
     *
     * @param access Api access.
     * @return 历史位置列表JSON
     */
    @Override
    public String process(ApiAccess access)
    {
        // 获取用户名
        if (!access.getHeaders().containsKey("username")) return "Error: Username not specified";
        String username = access.getHeaders().get("username");

        // 默认从两天前开始
        long starting = parseLong(access.getHeaders().getOrDefault("start", currentTimeMillis() - 2 * 24 * 60 * 60 * 1000 + ""));

        // 默认从现在结束
        long ending = parseLong(access.getHeaders().getOrDefault("end", currentTimeMillis() + ""));

        // 创建SQL语句
        String sql = resolve("SELECT * FROM data_log WHERE username=\"{}\" AND submit_time>={} AND submit_time<={}",
                username, starting, ending);

        // 在数据库中找到
        final ArrayList<LocationDataset> locationDatasets = new ArrayList<>();
        new DataLog().find(sql).forEach(dataLog -> locationDatasets.add(new LocationDataset(server.getEncryptor(), dataLog)));

        // 排序
        locationDatasets.sort(LocationDataset::compareTo);

        return GSON_WRITE.toJson(locationDatasets);
    }
}
