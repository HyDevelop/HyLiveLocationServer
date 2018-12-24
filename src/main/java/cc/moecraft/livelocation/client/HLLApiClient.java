package cc.moecraft.livelocation.client;

import cc.moecraft.livelocation.utils.UrlUtils;
import cc.moecraft.livelocation.utils.encryption.Encryptor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static cc.moecraft.livelocation.utils.UrlUtils.urlDecode;
import static cc.moecraft.livelocation.utils.UrlUtils.urlEncode;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

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
    private static final String KEYWORD_ENC = urlEncode("{enc}");
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
            this.KEYWORD_NODE = KEYWORD_ENC + encrypt("node");
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
            connection.setRequestProperty(KEYWORD_NODE, encrypt(apiNode));

            // 设置其他键值对
            for (int i = 0; i < kv.length; i += 2)
            {
                String key = kv[i].toString();
                String val = kv[i + 1].toString();
                connection.setRequestProperty(KEYWORD_ENC + encrypt(key), encrypt(val));
            }

            // 如果有内容
            if (content != null && !content.isEmpty())
            {
                // 加密内容
                content = KEYWORD_ENC + encrypt(content);

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
            return urlDecode(new BufferedReader(new InputStreamReader(connection.getInputStream()))
                    .lines().collect(joining(lineSeparator())));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Error: Local Error: " + e.getMessage();
        }
    }

    private String encrypt(String text)
    {
        return urlEncode(encryptor.encrypt(text));
    }
}
