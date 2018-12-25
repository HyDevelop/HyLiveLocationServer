import cc.moecraft.livelocation.utils.encryption.Base64C;
import cc.moecraft.livelocation.utils.encryption.Encryptor;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class Base64CTest
{
    public static void main(String[] args)
    {
        System.out.println(Base64C.encodeBase64C("Hello world !"));
        System.out.println(Base64C.decodeBase64CStr("SGVsbG8gd29ybGQgIQ"));
        System.out.println(Base64C.decodeBase64CStr("SGVsbG8gd29ybGQgIQ=="));
        System.out.println(new Encryptor("default-pw").decrypt("E4CtWYzemXCPRULSUUDdcHrreGttNCR20b3++pAMoXXBvd/G4h1SScZX9AsXj7P/GHOh6UhFc2Kj2uTxLkkdhQ=="));
    }
}
