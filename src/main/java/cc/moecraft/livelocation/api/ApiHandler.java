package cc.moecraft.livelocation.api;

import cc.moecraft.livelocation.HyLiveLocationServer;
import cc.moecraft.livelocation.utils.ResponseUtils;
import cc.moecraft.logger.HyLogger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 此类由 Hykilpikonna 在 2018/10/06 创建!
 * Created by Hykilpikonna on 2018/10/06!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("unused")
public class ApiHandler extends AbstractHandler
{
    @Getter
    private ApiNodeManager manager;
    private final HyLogger logger;
    private final HyLiveLocationServer server;

    public ApiHandler(HyLiveLocationServer server)
    {
        this.server = server;
        manager = new ApiNodeManager();

        logger = server.getLim().getLoggerInstance("ApiHandler", server.getConfig().isDebug());
        logger.log("Api Handler 已加载!");
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    {
        baseRequest.setHandled(true);
        if (request.getMethod().equalsIgnoreCase("post") && target.equalsIgnoreCase("/api"))
        {
            try
            {
                // Decrypt Headers.
                Map<String, String> headers = decryptHeaders(request);

                // Verify node.
                String nodeName = headers.get("node");
                if (nodeName == null) throw new RequestException("Node not specified.");

                // Find node.
                ApiNode node = manager.getNode(nodeName);
                if (node == null) throw new RequestException("Node " + nodeName + " is not a valid api node.");

                // Obtain Content.
                String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                // Decrypt Content.
                if (content.startsWith("{enc}")) content = server.getEncryptor().decrypt(content.substring(5));

                // Debug output
                logger.debug("Request received: {} : {}", node, content);

                // Write response.
                ResponseUtils.writeResponse(response, node.process(new ApiAccess(request, headers, content)));
            }
            catch (RequestException e)
            {
                // Write error.
                ResponseUtils.writeResponse(response, "Error: " + e.getText());
            }
            catch (Throwable e)
            {
                // Write error.
                e.printStackTrace();
                ResponseUtils.writeResponse(response, "Unpredicted Error: " + e.getMessage());
            }
        }
        else
        {
            ResponseUtils.writeResponse(response, "What is the meaning of life");
        }
    }

    @Override
    public void destroy()
    {
        logger.log("Api Handler 已卸载!");
    }

    @Getter
    @AllArgsConstructor
    private class RequestException extends Exception
    {
        private String text;
    }

    /**
     * 解密自定义HTTP请求头
     *
     * @param request 请求
     * @return 解密后的请求头Map
     */
    private Map<String, String> decryptHeaders(HttpServletRequest request)
    {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> keys = request.getHeaderNames();
        while (keys.hasMoreElements())
        {
            String encryptedKey = keys.nextElement();
            String key = decode(encryptedKey);
            String val = decode(request.getHeader(encryptedKey));

            if (key.startsWith("{enc}"))
            {
                String decryptedKey = server.getEncryptor().decrypt(key.substring(5));
                String decryptedVal = server.getEncryptor().decrypt(val);
                headers.put(decryptedKey, decryptedVal);
            }
            else headers.put(key, val);
        }
        return headers;
    }

    private static String decode(String text)
    {
        if (text == null || text.isEmpty()) return "";
        try
        {
            return URLDecoder.decode(text, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }
}
