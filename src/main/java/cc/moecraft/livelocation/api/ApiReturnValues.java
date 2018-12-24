package cc.moecraft.livelocation.api;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public enum ApiReturnValues
{
    Success,
    Unknown,
    Failed;

    @Override
    public String toString()
    {
        return name();
    }
}
