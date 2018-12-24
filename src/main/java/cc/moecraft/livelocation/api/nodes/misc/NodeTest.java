package cc.moecraft.livelocation.api.nodes.misc;

import cc.moecraft.livelocation.api.ApiAccess;
import cc.moecraft.livelocation.api.ApiNode;

/**
 * 此类由 Hykilpikonna 在 2018/10/07 创建!
 * Created by Hykilpikonna on 2018/10/07!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class NodeTest implements ApiNode
{
    @Override
    public String nodeName()
    {
        return "misc.test";
    }

    @Override
    public String process(ApiAccess access)
    {
        return "Success! \nRequest content is: " + access.getContent();
    }
}
