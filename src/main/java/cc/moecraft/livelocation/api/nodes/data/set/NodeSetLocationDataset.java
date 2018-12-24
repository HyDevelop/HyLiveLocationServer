package cc.moecraft.livelocation.api.nodes.data.set;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.api.HLLApiNode;
import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.DataLog;
import cc.moecraft.livelocation.dataset.LocationDataset;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;

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

        String json = server.decrypt(request.getHeader("dataset"));
        LocationDataset dataset = new Gson().fromJson(json, LocationDataset.class);

        DataLatest last = new DataLatest().findById(dataset.getUsername());
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



        return "Success";
    }
}
