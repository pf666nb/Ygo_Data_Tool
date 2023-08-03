package org.example;

import org.example.Bean.DataBean;
import org.example.Selector.Imp.DeckTag;
import org.example.Selector.Imp.MonsterTag;
import org.example.Utils.GeneratePieChartUtil;
import org.example.Utils.JFreeChartUtil;
import org.htmlunit.BrowserVersion;
import org.htmlunit.NicelyResynchronizingAjaxController;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ygo_Data_Tool
 *
 * @author : wpf
 * @date : 2023-08-03 17:06
 **/
public class JFreeChartTest {
    public static String imagePath = "D:\\";

    public static void main(String[] args) throws Exception {
        // 生成饼图
        testPie();

    }

    /**
     * 生成饼图
     */
    public static void testPie() throws Exception {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

        HtmlPage page = null;
        try {
            page = webClient.getPage("https://mycard.moe/ygopro/arena/#/cards");//尝试加载上面图片例子给出的网页
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            webClient.close();
        }

        webClient.waitForBackgroundJavaScript(5000);
        MonsterTag deckTag = new MonsterTag();
        deckTag.SelectTag(page);
        List<DataBean> elements = deckTag.getElements(page);
        List<String> use = elements.stream().map(DataBean::getAttribute2).collect(Collectors.toList());
        List<String> name = elements.stream().map(DataBean::getName).collect(Collectors.toList());
        Double other = 100.0;
        for (int i = 0; i < use.size(); i++) {
            other -= Double.parseDouble(use.get(i).substring(0,use.get(i).length()-1));
        }
        List<Object> collect = use.stream().map(s -> s.substring(0, s.length() - 1)).collect(Collectors.toList());

         List<Object> objects = collect.subList(0, 8);
         List<String> strings = name.subList(0, 8);
        objects.add(other);
        strings.add("其他");
        List<Color> legendColorList = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GRAY, Color.green, Color.cyan, Color.ORANGE));
        //偏离百分比数据
        List<Double> explodePercentList = new ArrayList<>(Arrays.asList(0.1, 0.1, 0.1, 0.1, 0.1));
        JFreeChart chart = GeneratePieChartUtil.createPieChart("YGO今日饼图", strings, objects
                , JFreeChartUtil.createChartTheme("宋体"), legendColorList, explodePercentList);
        //在D盘目录下生成图片
        File p = new File(imagePath);
        if (!p.exists()) {
            p.mkdirs();
        }
        String imageName = System.currentTimeMillis() + "_饼图" + ".jpeg";
        File file = new File(p.getPath() + "/" + imageName);
        try {
            if(file.exists()) {
                file.delete();
            }
            ChartUtils.saveChartAsJPEG(file, chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
