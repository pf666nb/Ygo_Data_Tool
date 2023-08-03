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


    }


}
