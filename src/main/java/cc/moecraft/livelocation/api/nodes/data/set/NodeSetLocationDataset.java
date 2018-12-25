package cc.moecraft.livelocation.api.nodes.data.set;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.DataValidator;
import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.UserInfo;
import cc.moecraft.livelocation.dataset.LocationDataset;

import static cc.moecraft.livelocation.HLLConstants.GSON_READ;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class NodeSetLocationDataset extends HLLApiNode
{
    public NodeSetLocationDataset(HyLiveLocationServer server)
    {
        super(server);
    }

    @Override
    public String nodeName()
    {
        return "data.set.user.location";
    }

    @Override
    public String process(ApiAccess access)
    {
        // Parse dataset
        String json = access.getHeaders().get("dataset");
        LocationDataset dataset = GSON_READ.fromJson(json, LocationDataset.class);

        // Move last to logs
        DataValidator.moveLastToLogs(dataset.getUsername());

        // Verify user, and update last active date
        UserInfo userInfo = DataValidator.validateUser(dataset.getUsername());
        userInfo.updateLastActive();
        userInfo.update();

        // Create new to replace last
        DataLatest latest = new DataLatest();
        latest.setUsername(dataset.getUsername());
        latest.setSubmitIp(access.getRequest().getRemoteAddr());
        latest.setSubmitTime(System.currentTimeMillis());
        latest.setLatitude(server.getEncryptor().encrypt(dataset.getLatitude()));
        latest.setLongitude(server.getEncryptor().encrypt(dataset.getLongitude()));
        latest.save();

        return "Success";
    }
}
