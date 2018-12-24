package cc.moecraft.livelocation.api.nodes.misc;

import cc.moecraft.livelocation.api.ApiNode;

import javax.servlet.http.HttpServletRequest;

/**
 * 此类由 Hykilpikonna 在 2018/10/07 创建!
 * Created by Hykilpikonna on 2018/10/07!
 * Github: https://github.com/hykilpikonna
 * Meow!
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
    public String process(HttpServletRequest request, String content)
    {
        return "Success! \nRequest content is: " + content;
    }
}
