package cc.moecraft.livelocation.client;

import cc.moecraft.livelocation.utils.UrlUtils;
import cc.moecraft.livelocation.utils.encryption.Encryptor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class HLLApiClient
{
    private final URL url;
    private final Encryptor encryptor;

    /**
     * Create an Api client object.
     *
     * @param url URL (e.g. "http://livelocation.hydev.pw/api")
     */
    public HLLApiClient(String url, Encryptor encryptor)
    {
        try
        {
            this.encryptor = encryptor;
            this.url = new URL(UrlUtils.normalize(url));
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String send(String apiNode, String content, Object ... kv)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // 设置 ApiNode
            connection.setRequestProperty("node", apiNode);

            // 设置其他键值对
            for (int i = 0; i < kv.length; i += 2)
            {
                String key = kv[i].toString();
                String val = kv[i + 1].toString();
                connection.setRequestProperty(key, val);
            }

            // 如果有内容, 设置内容
            if (content != null && !content.isEmpty())
            {
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", "" + content.getBytes().length);
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(content);
                outputStream.close();
            }

            // 获取回复
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                response.append(line).append("\n");
            }
            reader.close();

            // 去掉最后多余的换行
            return response.toString().substring(0, response.length() - 1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Error: Local Error: " + e.getLocalizedMessage();
        }
    }
}
