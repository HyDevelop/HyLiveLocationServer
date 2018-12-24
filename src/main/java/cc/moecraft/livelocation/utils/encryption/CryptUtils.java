package cc.moecraft.livelocation.utils.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class CryptUtils
{
    /**
     * 加密一段字符串
     *
     * @param text 明文
     * @param password 密码
     * @return 密文
     */
    public static String encrypt(String text, String password)
    {
        try
        {
            return encryptHelper(text, password);
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
     * @param password 密码
     * @return 明文
     */
    public static String decrypt(String text, String password)
    {
        try
        {
            return decryptHelper(text, password);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String encryptHelper(String text, String password) throws Exception
    {
        Key secret = getKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return new BASE64Encoder().encode(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
    }

    private static String decryptHelper(String text, String password) throws Exception
    {
        Key secret = getKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(text)), StandardCharsets.UTF_8);
    }

    /**
     * 用密码生成Key
     *
     * @param keySeed 密码
     * @return Key
     */
    private static Key getKey(String keySeed)
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
