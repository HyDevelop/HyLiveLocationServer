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
import java.util.stream.Collectors;

/**
 * 此类由 Hykilpikonna 在 2018/10/06 创建!
 * Created by Hykilpikonna on 2018/10/06!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
@SuppressWarnings("unused")
public class ApiHandler extends AbstractHandler
{
    @Getter
    private ApiNodeManager manager;
    private final HyLogger logger;

    public ApiHandler(HyLiveLocationServer server)
    {
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
                // Verify node.
                String nodeName = request.getHeader("node");
                if (nodeName == null) throw new RequestException("Node not specified.");

                // Find node.
                ApiNode node = manager.getNode(nodeName);
                if (node == null) throw new RequestException("Node " + nodeName + " is not a valid api node.");

                // Obtain Content.
                String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                // TODO: Remove debug output
                logger.debug("Request received: {} : {}", node, content);

                // Write response.
                ResponseUtils.writeResponse(response, node.process(request, content));
            }
            catch (RequestException e)
            {
                // Write error.
                ResponseUtils.writeResponse(response, "Error: " + e.getText());
            }
            catch (Throwable e)
            {
                // Write error.
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
}
