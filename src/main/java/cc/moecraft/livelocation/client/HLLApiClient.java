package cc.moecraft.livelocation.client;

import cc.moecraft.livelocation.dataset.LocationDataset;
import cc.moecraft.livelocation.dataset.UserInfoDataset;
import cc.moecraft.livelocation.utils.UrlUtils;
import cc.moecraft.livelocation.utils.encryption.Encryptor;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static cc.moecraft.livelocation.HLLConstants.GSON_READ;
import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("unused")
public class HLLApiClient
{
    private final URL url;
    private final Encryptor encryptor;
    private final String KEYWORD_NODE;

    /**
     * Create an Api client object.
     *
     * @param url URL (e.g. "http://livelocation.hydev.pw/api")
     */
    public HLLApiClient(String url, Encryptor encryptor)
    {
        try
        {
            this.url = new URL(UrlUtils.normalize(url));
            this.encryptor = encryptor;
            this.KEYWORD_NODE = "-enc-" + encrypt("node");
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送Api请求
     *
     * @param apiNode Api 节点
     * @param content 请求数据
     * @param kv Header 键值对
     * @return 返回信息
     */
    public String send(String apiNode, String content, Object ... kv)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // 设置 ApiNode
            connection.setRequestProperty(KEYWORD_NODE, encrypt(apiNode));

            // 设置其他键值对
            for (int i = 0; i < kv.length; i += 2)
            {
                String key = kv[i].toString();
                String val = kv[i + 1].toString();
                connection.setRequestProperty("-enc-" + encrypt(key), encrypt(val));
            }

            // 如果有内容
            if (content != null && !content.isEmpty())
            {
                // 加密内容
                content = "-enc-" + encrypt(content);

                // 发送内容
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" + content.getBytes().length);
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(content);
                outputStream.close();
            }

            // 获取回复
            return new BufferedReader(new InputStreamReader(connection.getInputStream()))
                    .lines().collect(joining(lineSeparator()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Error: Local Error: " + e.getMessage();
        }
    }

    private String encrypt(String text)
    {
        return encryptor.encrypt(text);
    }

    /**
     * 发送测试
     *
     * @param content 测试数据
     * @return 测试返回
     */
    public String sendTest(String content)
    {
        return send("misc.test", content);
    }

    /**
     * 更新用户信息
     *
     * @param dataset 用户信息Dataset
     * @return 执行结果
     */
    public String sendSetUserInfo(UserInfoDataset dataset)
    {
        return send("data.set.user.info", null, "dataset", GSON_WRITE.toJson(dataset));
    }

    /**
     * 上传位置信息
     *
     * @param dataset 位置信息Dataset
     * @return 执行结果
     */
    public String sendSetLocationDataset(LocationDataset dataset)
    {
        return send("data.set.user.location", null, "dataset", GSON_WRITE.toJson(dataset));
    }

    /**
     * 获取用户列表
     *
     * @param activeOnly 是否只记录活跃用户
     * @param inactiveOnly 是否只记录不活跃用户
     * @return 用户列表
     */
    public List<UserInfoDataset> getUserList(boolean activeOnly, boolean inactiveOnly)
    {
        return GSON_READ.fromJson(send("data.get.user.list", null, "active-only", activeOnly, "inactive-only", inactiveOnly),
                new TypeToken<List<UserInfoDataset>>(){}.getType());
    }

    /**
     * 获取全部用户列表
     *
     * @return 用户列表
     */
    public List<UserInfoDataset> getUserListAll()
    {
        return getUserList(false, false);
    }

    /**
     * 获取活跃用户列表
     *
     * @return 用户列表
     */
    public List<UserInfoDataset> getUserListActive()
    {
        return getUserList(true, false);
    }

    /**
     * 获取不活跃用户列表
     *
     * @return 用户列表
     */
    public List<UserInfoDataset> getUserListInactive()
    {
        return getUserList(false, true);
    }

    /**
     * 获取用户位置信息
     *
     * @param username 用户名
     * @return 用户位置
     */
    public LocationDataset getUserLocation(String username)
    {
        return GSON_READ.fromJson(send("data.get.user.location", null, "username", username), LocationDataset.class);
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public UserInfoDataset getUserInfo(String username)
    {
        return GSON_READ.fromJson(send("data.get.user.info", null, "username", username), UserInfoDataset.class);
    }

    /**
     * 获取用户位置历史
     *
     * @param username 用户名
     * @param start 起始时间
     * @param end 结束时间
     * @return 位置历史 (按时间排序)
     */
    public List<LocationDataset> getUserLocationHistory(String username, long start, long end)
    {
        return GSON_READ.fromJson(send("data.get.user.location.history", null,
                "username", username,
                "start", start,
                "end", end
        ), new TypeToken<List<LocationDataset>>(){}.getType());
    }
}
