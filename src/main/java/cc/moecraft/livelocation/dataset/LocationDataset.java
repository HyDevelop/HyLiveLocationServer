package cc.moecraft.livelocation.dataset;

import cc.moecraft.livelocation.database.model.DataLatest;
import cc.moecraft.livelocation.database.model.DataLog;
import cc.moecraft.livelocation.utils.encryption.Encryptor;
import lombok.Data;

import static java.lang.Double.parseDouble;

/**
 * HyLiveLocation 位置数据组
 *
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @since 2018-12-24
 */
@Data
public class LocationDataset implements Comparable<LocationDataset>
{
    /** 用户名 */
    private final String username;

    /** 纬度 */
    private final Double latitude;

    /** 经度 */
    private final Double longitude;

    /** 提交IP (获取) */
    private String submitIp = null;

    /** 提交时间 (获取) */
    private Long submitTime = null;

    /**
     * 从数据库Model对象创建一个Dataset对象
    public LocationDataset(String username, Double latitude, Double longitude, String submitIp, Long submitTime)
    {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.submitIp = submitIp;
        this.submitTime = submitTime;
    }

    public LocationDataset(String username, Double latitude, Double longitude)
    {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    @Override
    public int compareTo(LocationDataset o)
    {
        return submitTime.equals(o.submitTime) ? 0 : submitTime > o.submitTime ? -1 : 1;
    }
}
