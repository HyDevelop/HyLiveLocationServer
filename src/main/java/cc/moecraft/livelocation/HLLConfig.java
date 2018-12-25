package cc.moecraft.livelocation;

import lombok.Builder;
import lombok.Data;

/**
 * 此类由 Hykilpikonna 在 2018/12/22 创建!
 * Created by Hykilpikonna on 2018/12/22!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@Data
@Builder
public class HLLConfig
{
    // 服务器配置
    private int port;
    private String password;

    // 日志配置
    private boolean debug = false;

    // 数据库配置
    private String dbUrl;
    private String dbUsr;
    private String dbPwd;
}
