package cc.moecraft.livelocation.database;

import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.DataLog;
import cc.moecraft.livelocation.database.model.UserInfo;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
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
            dataLog.setLatitude(last.getLatitude());
            dataLog.setLongitude(last.getLongitude());
            dataLog.save();
            last.delete();
        }
    }

    /**
     * Validate user login
     *  - Creates a user info entry if it doesn't exist.
     */
    public static UserInfo validateUser(String username)
    {
        UserInfo userInfo = new UserInfo().findById(username);
        if (userInfo == null)
        {
            userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.updateLastActive();
            userInfo.save();
        }
        return userInfo;
    }
}
