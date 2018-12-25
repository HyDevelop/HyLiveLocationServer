package cc.moecraft.livelocation.utils.encryption;

import java.security.Key;

import static cc.moecraft.livelocation.utils.encryption.CryptUtils.getKey;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("unused")
public class Encryptor
{
    private final Key secret;

    public Encryptor(String password)
    {
        secret = getKey(password);
    }

    /**
     * 用设好的密码加密
     *
     * @param text 明文
     * @return 密文
     */
    public String encrypt(String text)
    {
        return CryptUtils.encrypt(text, secret);
    }

    /**
     * 用设好的密码加密
     *
     * @param text 明文
     * @return 密文
     */
    public String encrypt(Object text)
    {
        return encrypt(text.toString());
    }

    /**
     * 用设好的密码解密
     *
     * @param text 明文
     * @return 密文
     */
    public String decrypt(String text)
    {
        return CryptUtils.decrypt(text, secret);
    }

    /**
     * 用设好的密码解密
     *
     * @param text 明文
     * @return 密文
     */
    public String decrypt(Object text)
    {
        return encrypt(text.toString());
    }
}
