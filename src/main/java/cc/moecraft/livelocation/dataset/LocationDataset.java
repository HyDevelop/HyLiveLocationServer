package cc.moecraft.livelocation.dataset;

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
public class LocationDataset
{
    private final String username;
    private final Double latitude; // 纬度
    private final Double longitude; // 经度
}
