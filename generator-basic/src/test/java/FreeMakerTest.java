import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/25-20:09
 */
public class FreeMakerTest {

    @Test
    public void freeMakerTest() throws IOException, TemplateException {
        // 创建configuration对象
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        //  指定模板文件所在路径
        File targetFolder = new File("src/main/resources/templates");
        configuration.setDirectoryForTemplateLoading(targetFolder);
        configuration.setDefaultEncoding("utf-8");

        // 获取模板对象
        Template template = configuration.getTemplate("myweb.html.ftl");

        // 生成数据
        Map<String, Object> data = new HashMap<>();
        data.put("currentYear",2024);
        List<Map<String,Object>> menuItems = new ArrayList<>();
        Map<String, Object> menuItem1 = new HashMap<>();
        menuItem1.put("url","https://www.baidu.com");
        menuItem1.put("label","导航1");
        menuItems.add(menuItem1);
        Map<String, Object> menuItem2 = new HashMap<>();
        menuItem2.put("url","https://www.baidu.com");
        menuItem2.put("label","导航2");
        menuItems.add(menuItem2);
        data.put("menuItems",menuItems);

        Writer out = new FileWriter(new File(targetFolder,"myweb.html"));
        template.process(data,out);
        out.close();
    }
}
