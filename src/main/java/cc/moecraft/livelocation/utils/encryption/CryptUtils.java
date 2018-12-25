package cc.moecraft.livelocation.utils.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static cc.moecraft.livelocation.utils.encryption.Base64C.decodeBase64C;
import static cc.moecraft.livelocation.utils.encryption.Base64C.encodeBase64C;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("WeakerAccess")
public class CryptUtils
{
    /**
     * 加密一段字符串
     *
     * @param text 明文
     * @param secret 密钥
     * @return 密文
     */
    public static String encrypt(String text, Key secret)
    {
        try
        {
            return encryptHelper(text, secret);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密一段字符串
     *
     * @param text 密文
     * @param secret 密钥
     * @return 明文
     */
    public static String decrypt(String text, Key secret)
    {
        try
        {
            return decryptHelper(text, secret);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密一段明文字符串
     *
     * @param text 明文
     * @param secret 密钥
     * @return 密文
     * @throws Exception 加密相关的异常
     */
    private static String encryptHelper(String text, Key secret) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return encodeBase64C(cipher.doFinal(text.getBytes(UTF_8)));
    }

    /**
     * 解密一段密文字符串
     *
     * @param text 密文
     * @param secret 密钥
     * @return 明文
     * @throws Exception 加密相关的异常
     */
    private static String decryptHelper(String text, Key secret) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(decodeBase64C(text)), UTF_8);
    }

    /**
     * 用密码生成Key
     *
     * @param keySeed 密码
     * @return Key
     */
    public static Key getKey(String keySeed)
    {
        if (keySeed == null) keySeed = System.getenv("AES_SYS_KEY");
        if (keySeed == null) keySeed = System.getProperty("AES_SYS_KEY");
        if (keySeed == null || keySeed.trim().length() == 0) throw new RuntimeException("KeySeed is null");

        try
        {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
}
