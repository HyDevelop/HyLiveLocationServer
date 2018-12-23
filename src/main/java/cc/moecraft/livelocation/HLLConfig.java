package cc.moecraft.livelocation;

import lombok.Builder;
import lombok.Data;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
@Data
@Builder
public class HLLConfig
{
    private int port;
    private String password;

    private boolean debug;

    private String dbUrl;
    private String dbUsr;
    private String dbPwd;
}
