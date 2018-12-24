package cc.moecraft.livelocation.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class UrlUtils
{
    /**
     * 标准化URL字符串
     * Credit to Hutool
     *
     * @param url URL字符串
     * @return 标准化后的URL字符串
     */
    public static String normalize(String url)
    {
        if (StrUtil.isBlank(url))
        {
            return url;
        }
        final int sepIndex = url.indexOf("://");
        String pre;
        String body;
        if (sepIndex > 0)
        {
            pre = StrUtil.subPre(url, sepIndex + 3);
            body = StrUtil.subSuf(url, sepIndex + 3);
        }
        else
        {
            pre = "http://";
            body = url;
        }

        int paramsSepIndex = url.indexOf("?");
        String params = null;
        if (paramsSepIndex > 0)
        {
            params = StrUtil.subSuf(body, paramsSepIndex);
            body = StrUtil.subPre(body, paramsSepIndex);
        }

        //去除开头的\或者/
        body = body.replaceAll("^[\\/]+", StrUtil.EMPTY);
        //替换多个\或/为单个/
        body = body.replace("\\", "/").replaceAll("//+", "/");
        return pre + body + StrUtil.nullToEmpty(params);
    }
}
