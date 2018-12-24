package cc.moecraft.livelocation.database;

import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.DataLog;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class DataValidator
{
    /**
     * Move last DataLatest to DataLog.
     * Ignores if last doesn't exist.
     *
     * @param username Username
     */
    public static void moveLastToLogs(String username)
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
