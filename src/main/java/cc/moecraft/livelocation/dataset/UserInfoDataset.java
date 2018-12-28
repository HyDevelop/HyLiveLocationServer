package cc.moecraft.livelocation.dataset;

import cc.moecraft.livelocation.database.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * HyLiveLocation 用户信息数据组
 *
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @since 2018-12-24
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDataset
{
    /** 用户名 */
    private final String username;

    /** 头像URL */
    private final String avatarUrl;

    /** 最后活跃时间 (获取) */
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
