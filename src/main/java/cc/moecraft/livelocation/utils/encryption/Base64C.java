package cc.moecraft.livelocation.utils.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

import static cc.moecraft.utils.StringUtils.repeat;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Base64C
{
    public static String encodeBase64C(byte[] bytes)
    {
        return new BASE64Encoder().encode(bytes)
                .replace("=", "")
                .replace("+", "-")
                .replace("/", "_")
                .replaceAll("[\\s*\t\n\r]", "");
    }

    public static String encodeBase64C(String text)
    {
        return encodeBase64C(text.getBytes());
    }

    public static byte[] decodeBase64C(String text)
    {
        if (text.length() % 4 != 0) text += repeat("=", 4 - text.length() % 4);
        try
        {
            return new BASE64Decoder().decodeBuffer(text
                    .replace("-", "+")
                    .replace("_", "/"));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String decodeBase64CStr(String text)
    {
        return new String(decodeBase64C(text));
    }
}
