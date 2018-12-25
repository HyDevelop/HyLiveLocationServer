package cc.moecraft.livelocation.dataset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
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
}
