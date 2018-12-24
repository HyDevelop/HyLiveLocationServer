package cc.moecraft.livelocation.api;

import cc.moecraft.livelocation.HyLiveLocationServer;
import lombok.AllArgsConstructor;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
@AllArgsConstructor
public abstract class HLLApiNode implements ApiNode
{
    protected final HyLiveLocationServer server;
}
