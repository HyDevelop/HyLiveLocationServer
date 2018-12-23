package cc.moecraft.livelocation.api;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 此类由 Hykilpikonna 在 2018/10/06 创建!
 * Created by Hykilpikonna on 2018/10/06!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("WeakerAccess")
@Getter @ToString
public class ApiNodeManager
{
    private final ArrayList<ApiNode> registeredNodes = new ArrayList<>();

    /**
     * Register some nodes.
     *
     * @param node Nodes.
     */
    public void register(ApiNode ... node)
    {
        registeredNodes.addAll(Arrays.asList(node));
    }

    /**
     * Get node by name
     *
     * @param nodeName Node name.
     * @return Registered node. Return null if not found.
     */
    public ApiNode getNode(String nodeName)
    {
        nodeName = nodeName.toLowerCase();

        for (ApiNode node : registeredNodes)
            if (node.nodeName().equals(nodeName)) return node;

        return null;
    }
}
