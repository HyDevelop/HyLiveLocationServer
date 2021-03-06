package cc.moecraft.livelocation.client;

import cc.moecraft.livelocation.HLLServerConfig;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static cc.moecraft.livelocation.HLLConstants.GSON_READ;
import static cc.moecraft.livelocation.HLLConstants.GSON_WRITE;
import static java.lang.Long.*;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

/**
 * HyLiveLocation API客户端
 *
 * @author Hykilpikonna (https://github.com/hykilpikonna)
 * @since 2018-12-24
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class HLLApiClient
{
    private final URL url;
    private final Encryptor encryptor;
    private final String KEYWORD_NODE;

    /**
     * 创建一个API客户端对象
     *
     * @param url 服务器URL (e.g. "http://livelocation.hydev.pw/api")
     * @param encryptor 加密工具
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
                if (kv[i] == null || kv[i + 1] == null) continue;

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
            String response = encryptor.decrypt(new BufferedReader(new InputStreamReader(connection.getInputStream()))
                    .lines().collect(joining(lineSeparator())));

            // 判断异常
            if (response.toLowerCase().startsWith("error")) throw new ApiException(response);

            return response;
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
     * API访问异常
     */
    public static class ApiException extends RuntimeException
    {
        public ApiException(String message)
        {
            super(message);
        }
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
    public String sendUserInfo(UserInfoDataset dataset)
    {
        return send("data.set.user.info", null, "dataset", GSON_WRITE.toJson(dataset));
    }

    /**
     * 更新用户信息
     *
     * @param username 用户名
     * @param avatarUrl 头像URL
     * @return 执行结果
     */
    public String sendUserInfo(String username, String avatarUrl)
    {
        return sendUserInfo(new UserInfoDataset(username, avatarUrl));
    }

    /**
     * 上传位置信息
     *
     * @param dataset 位置信息Dataset
     * @return 执行结果
     */
    public String sendLocationDataset(LocationDataset dataset)
    {
        return send("data.set.user.location", null, "dataset", GSON_WRITE.toJson(dataset));
    }

    /**
     * 上传位置信息
     *
     * @param username 用户名
     * @param lat 纬度
     * @param lon 经度
     * @return 执行结果
     */
    public String sendLocationDataset(String username, Double lat, Double lon)
    {
        return sendLocationDataset(new LocationDataset(username, lat, lon));
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
        return GSON_READ.fromJson(send("data.get.user.list", null,
                "active-only", activeOnly,
                "inactive-only", inactiveOnly
        ), new TypeToken<List<UserInfoDataset>>(){}.getType());
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
    public List<LocationDataset> getUserLocationHistory(String username, Long start, Long end)
    {
        return GSON_READ.fromJson(send("data.get.user.location.history", null,
                "username", username,
                "start", start,
                "end", end
        ), new TypeToken<List<LocationDataset>>(){}.getType());
    }

    /**
     * 获取用户位置历史
     *
     * @param username 用户名
     * @param start 起始时间
     * @param end 结束时间
     * @return 位置历史 (按时间排序)
     */
    public List<LocationDataset> getUserLocationHistory(String username, Date start, Date end)
    {
        return getUserLocationHistory(username, start.getTime(), end.getTime());
    }

    /**
     * 获取用户两天内的位置历史
     *
     * @param username 用户名
     * @return 位置历史 (按时间排序)
     */
    public List<LocationDataset> getUserLocationHistory(String username)
    {
        return getUserLocationHistory(username, (Long) null, null);
    }

    /**
     * 获取服务器配置
     *
     * @return 服务器配置
     */
    public HLLServerConfig getServerConfig()
    {
        return GSON_READ.fromJson(send("data.get.server.config", null), HLLServerConfig.class);
    }

    /**
     * 获取服务器时间
     *
     * @param timezone 时区
     * @return 时间 (ms)
     */
    public long getServerTime(String timezone)
    {
        return parseLong(send("misc.ping", "timezone", timezone));
    }

    /**
     * 获取服务器时区的时间
     *
     * @return 时间 (ms)
     */
    public long getServerTime()
    {
        return getServerTime(null);
    }

    /**
     * 获取连接延迟
     *
     * @return 延迟 (ms)
     */
    public long ping()
    {
        // 这个地方很奇怪...
        // 无论怎样写都是负数
        // Fixed: 要先获取服务器时间然后再获取本地时间
        long serverTime = getServerTime("UTC");
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() - serverTime;
    }
}
