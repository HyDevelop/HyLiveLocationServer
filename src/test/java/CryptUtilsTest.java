import cc.moecraft.livelocation.utils.encryption.Encryptor;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class CryptUtilsTest
{
    public static void main(String[] args) throws Exception
    {
        String text = "Hello world \n\t Test";
        String password = "adasfasfasdfasfas11=2590=6;g'ert";

        Encryptor encryptor = new Encryptor(password);

        String encrypted = encryptor.encrypt(text);
        System.out.println("原文: " + text);
        System.out.println("---------------------");
        System.out.println("加密后: " + encrypted);
        System.out.println("---------------------");
        System.out.println("解密后: " + encryptor.decrypt(encrypted));
    }
}
