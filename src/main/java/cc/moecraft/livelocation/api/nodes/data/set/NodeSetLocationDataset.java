package cc.moecraft.livelocation.api.nodes.data.set;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.DataLog;
import cc.moecraft.livelocation.dataset.LocationDataset;

import javax.servlet.http.HttpServletRequest;

import static cc.moecraft.livelocation.HLLConstants.GSON_READ;
import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;

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
        return "data.set.location-dataset";
    }

    @Override
    public String process(HttpServletRequest request, String content)
    {
        if (!content.equals("Where am I?")) return "Who are you?";

        // Parse dataset
        String json = server.decrypt(request.getHeader("dataset"));
        LocationDataset dataset = GSON_READ.fromJson(json, LocationDataset.class);

        // Move last to logs
        moveLastToLogs(dataset.getUsername());

        // Create new to replace last
        DataLatest latest = new DataLatest();
        latest.setUsername(dataset.getUsername());
        latest.setSubmitIp(request.getRemoteAddr());
        latest.setSubmitTime(System.currentTimeMillis());
        latest.setLocationDataset(server.encrypt(GSON_WRITE.toJson(dataset)));
        latest.save();

        return "Success";
    }

    /**
     * Move last DataLatest to DataLog.
     * Ignores if last doesn't exist.
     *
     * @param username Username
     */
    private void moveLastToLogs(String username)
    {
        DataLatest last = new DataLatest().findById(username);
        if (last != null)
        {
            DataLog dataLog = new DataLog();
            dataLog.setUsername(last.getUsername());
            dataLog.setSubmitIp(last.getSubmitIp());
            dataLog.setSubmitTime(last.getSubmitTime());
            dataLog.setLocationDataset(last.getLocationDataset());
            dataLog.save();
            last.delete();
        }
    }
}
