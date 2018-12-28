package cc.moecraft.livelocation.utils.encryption;

import java.security.Key;

import static cc.moecraft.livelocation.utils.encryption.CryptUtils.getKey;

/**
 * 加密工具
 *
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @since 2018-12-24
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
