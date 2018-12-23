package cc.moecraft.livelocation.utils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 此类由 Hykilpikonna 在 2018/10/06 创建!
 * Created by Hykilpikonna on 2018/10/06!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class ResponseUtils
{
    /**
     * Write content to a http response.
     *
     * @param response Response.
     * @param content Content.
     */
    public static void writeResponse(HttpServletResponse response, String content)
    {
        try
        {
            // Declare response encoding and types
            response.setContentType("text; charset=utf-8");

            // Declare response status code
            response.setStatus(HttpServletResponse.SC_OK);

            // Write back response
            response.getWriter().println(content);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class CustomResponseWrapper extends HttpServletResponseWrapper
    {
        private CharArrayWriter output;

        public CustomResponseWrapper(HttpServletResponse response)
        {
            super(response);
            output = new CharArrayWriter();
        }

        public String getResponseContent()
        {
            return output.toString();
        }

        public PrintWriter getWriter()
        {
            return new PrintWriter(output);
        }
    }
}
