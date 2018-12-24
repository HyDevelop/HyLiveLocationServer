package cc.moecraft.livelocation.api.nodes.data.set;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.DataValidator;
import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.dataset.LocationDataset;

import javax.servlet.http.HttpServletRequest;

import static cc.moecraft.livelocation.HLLConstants.GSON_READ;
import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
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
        DataValidator.moveLastToLogs(dataset.getUsername());

        // Create new to replace last
        DataLatest latest = new DataLatest();
        latest.setUsername(dataset.getUsername());
        latest.setSubmitIp(request.getRemoteAddr());
        latest.setSubmitTime(System.currentTimeMillis());
        latest.setLocationDataset(server.encrypt(GSON_WRITE.toJson(dataset)));
        latest.save();

        return "Success";
    }
}
