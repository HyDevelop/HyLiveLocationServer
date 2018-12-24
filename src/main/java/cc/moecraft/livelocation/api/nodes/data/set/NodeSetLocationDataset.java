package cc.moecraft.livelocation.api.nodes.data.set;

import cc.moecraft.livelocation.api.ApiNode;

import javax.servlet.http.HttpServletRequest;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class NodeSetLocationDataset implements ApiNode
{
    @Override
    public String nodeName()
    {
        return "data.set.location-dataset";
    }

    @Override
    public String process(HttpServletRequest request, String content)
    {
        return "";
    }
}
