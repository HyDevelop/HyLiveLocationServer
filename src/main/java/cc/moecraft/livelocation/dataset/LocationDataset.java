package cc.moecraft.livelocation.dataset;

import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.DataLog;
import cc.moecraft.livelocation.utils.encryption.Encryptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static java.lang.Double.parseDouble;

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
@RequiredArgsConstructor
public class LocationDataset
{
    private final String username;
    private final Double latitude; // 纬度
    private final Double longitude; // 经度
    private String submitIp = null;
    private Long submitTime = null;

    /**
     * 从数据库Model对象创建一个Dataset对象
     *
     * @param encryptor 解密工具
     * @param dataLatest 位置信息
     */
    public LocationDataset(Encryptor encryptor, DataLatest dataLatest)
    {
        this(dataLatest.getUsername(),
                parseDouble(encryptor.decrypt(dataLatest.getLatitude())),
                parseDouble(encryptor.decrypt(dataLatest.getLongitude())),
                dataLatest.getSubmitIp(), dataLatest.getSubmitTime());
    }

    /**
     * 从数据库Model对象创建一个Dataset对象
     *
     * @param encryptor 解密工具
     * @param dataLog 位置信息
     */
    public LocationDataset(Encryptor encryptor, DataLog dataLog)
    {
        this(dataLog.getUsername(),
                parseDouble(encryptor.decrypt(dataLog.getLatitude())),
                parseDouble(encryptor.decrypt(dataLog.getLongitude())),
                dataLog.getSubmitIp(), dataLog.getSubmitTime());
    }
}
