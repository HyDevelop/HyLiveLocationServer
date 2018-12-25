package cc.moecraft.livelocation;

import lombok.*;

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
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
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

    // 时间配置 (millis)
    private long inactiveTimeout = 5 * 60 * 1000;
}
