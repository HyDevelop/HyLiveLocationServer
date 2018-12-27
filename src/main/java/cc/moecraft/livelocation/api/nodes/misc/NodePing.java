package cc.moecraft.livelocation.api.nodes.misc;

import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.ApiNode;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 此类由 Hykilpikonna 在 2018/12/26 创建!
 * Created by Hykilpikonna on 2018/12/26!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class NodePing implements ApiNode
{
    @Override
    public String nodeName()
    {
        return "misc.ping";
    }

    /**
     * 获取当前服务器时间
     *  - timezone (str): 时区
     *
     * @param access Api access.
     * @return 当前时间
     */
    @Override
    public String process(ApiAccess access)
    {
        if (!access.getHeaders().containsKey("timezone")) return System.currentTimeMillis() + "";
        return Calendar.getInstance(TimeZone.getTimeZone(access.getHeaders().get("timezone"))).getTimeInMillis() + "";
    }
}
