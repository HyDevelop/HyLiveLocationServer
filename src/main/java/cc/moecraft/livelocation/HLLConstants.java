package cc.moecraft.livelocation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 此类由 Hykilpikonna 在 2018/12/24 创建!
 * Created by Hykilpikonna on 2018/12/24!
 * Github: https://github.com/hykilpikonna
 * Meow!
 *
 * @author Hykilpikonna
 */
public class HLLConstants
{
    public static final Gson GSON_READ = new Gson();
    public static final Gson GSON_WRITE = new Gson();
    public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
}
