package cc.moecraft.livelocation.utils.encryption;

import lombok.AllArgsConstructor;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("unused")
@AllArgsConstructor
public class Encryptor
{
    private final String password;

    /**
     * 用设好的密码加密
     *
     * @param text 明文
     * @return 密文
     */
    public String encrypt(String text)
    {
        return CryptUtils.encrypt(text, password);
    }

    /**
     * 用设好的密码解密
     *
     * @param text 明文
     * @return 密文
     */
    public String decrypt(String text)
    {
        return CryptUtils.decrypt(text, password);
    }
}
