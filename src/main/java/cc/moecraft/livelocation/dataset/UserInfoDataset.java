package cc.moecraft.livelocation.dataset;

import cc.moecraft.livelocation.database.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
@Data
@AllArgsConstructor
public class UserInfoDataset
{
    private final String username;
    private final String avatarUrl;
    private Long lastActive = null;

    /**
     * 从数据库Model对象创建一个Dataset对象
     *
     * @param userInfo 用户信息
     */
    public UserInfoDataset(UserInfo userInfo)
    {
        this(userInfo.getUsername(), userInfo.getAvatarUrl(), userInfo.getLastActive());
    }
}
