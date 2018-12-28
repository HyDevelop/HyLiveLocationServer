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
