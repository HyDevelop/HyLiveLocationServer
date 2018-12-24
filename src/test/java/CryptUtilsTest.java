import cc.moecraft.livelocation.utils.encryption.CryptUtils;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class CryptUtilsTest
{
    public static void main(String[] args) throws Exception
    {
        String text = "Hello world \n\t Test";
        String password = "adasfasfasdfasfas11=2590=6;g'ert";

        String encrypted = CryptUtils.encrypt(text, password);
        System.out.println("原文: " + text);
        System.out.println("---------------------");
        System.out.println("加密后: " + encrypted);
        System.out.println("---------------------");
        System.out.println("解密后: " + CryptUtils.decrypt(encrypted, password));
    }
}
