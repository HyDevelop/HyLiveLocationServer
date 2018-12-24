package cc.moecraft.livelocation.api;

/**
 * 此类由 Hykilpikonna 在 2018/10/06 创建!
 * Created by Hykilpikonna on 2018/10/06!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("UnnecessaryInterfaceModifier")
public interface ApiNode
{
    /**
     * Name of the api node.
     * MUST be all lowercase.
     *
     * @return Name of the api node.
     */
    public String nodeName();

    /**
     * Process an api request.
     *
     * @param access Api access.
     * @return Result.
     */
    public String process(ApiAccess access);
}
