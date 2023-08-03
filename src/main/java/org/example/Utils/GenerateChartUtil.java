package org.example.Utils;

/**
 * @author tqf
 * @Description 图表生成工具类
 * @Version 1.0
 * @since 2022-06-07 09:43
 */

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class GenerateChartUtil {

    /**
     * 生成柱状图(返回JFreeChart)
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @param yAxisMinValue   y轴最小值（可以为空）
     * @param yAxisMaxValue   y轴最大值（可以为空）
     * @param legendColorList 图例背景颜色（可以为空）
     * @param barLabelVisible 是否显示柱体标签（可以为空）
     * @param barLabelFormat  柱体标签格式（可以为空）
     * @return
     */
    public static JFreeChart createBarChart(String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle, Double yAxisMinValue
            , Double yAxisMaxValue, List<Color> legendColorList, Boolean barLabelVisible, String barLabelFormat) throws Exception {
        //设置主题，防止中文乱码
        theme = theme == null ? JFreeChartUtil.createChartTheme("") : theme;
        ChartFactory.setChartTheme(theme);
        //创建柱状图
        JFreeChart chart = ChartFactory.createBarChart(chartTitle, xAxisTitle, yAxisTitle
                , JFreeChartUtil.createDefaultCategoryDataset(legendNameList, xAxisNameList, dataList));
        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        // 对柱子进行渲染
        JFreeChartUtil.setBarRenderer(chart.getCategoryPlot(), true);CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis categoryAxis = plot.getDomainAxis();
        // 最大换行数
        categoryAxis.setMaximumCategoryLabelLines(10);
        //y轴
        ValueAxis valueAxis = chart.getCategoryPlot().getRangeAxis();
        if (yAxisMinValue != null) {
            valueAxis.setLowerBound(yAxisMinValue);
        }
        if (yAxisMaxValue != null) {
            valueAxis.setUpperBound(yAxisMaxValue);
        }
        CategoryItemRenderer customBarRenderer = plot.getRenderer();
        //显示每个柱的数值
        if (barLabelVisible != null) {
            customBarRenderer.setDefaultItemLabelsVisible(barLabelVisible);
            //柱体数值格式
            if (StrUtil.isNotEmpty(barLabelFormat)) {
                customBarRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(barLabelFormat, NumberFormat.getInstance()));
            }
        }
        //设置系列柱体背景颜色
        if (CollectionUtil.isNotEmpty(legendColorList)) {
            for (int i = 0; i < legendNameList.size() && i < legendColorList.size(); i++) {
                Color color = legendColorList.get(i);
                if (color == null) {
                    continue;
                }
                customBarRenderer.setSeriesPaint(i, color);
            }
        }
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 标注位于上侧
        chart.getLegend().setPosition(RectangleEdge.TOP);
        return chart;
    }

    /**
     * 生成柱状图(返回outputStream)
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @param outputStream   输出流
     * @param width          宽度
     * @param height         高度
     * @param yAxisMinValue   y轴最小值（可以为空）
     * @param yAxisMaxValue   y轴最大值（可以为空）
     * @param legendColorList 图例背景颜色（可以为空）
     * @param barLabelVisible 是否显示柱体标签（可以为空）
     * @param barLabelFormat  柱体标签格式（可以为空）
     * @return
     */
    public static void createBarChart(OutputStream outputStream, String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle, int width, int height
            , Double yAxisMinValue, Double yAxisMaxValue, List<Color> legendColorList, Boolean barLabelVisible, String barLabelFormat) throws Exception {
        JFreeChart chart = createBarChart(chartTitle, legendNameList, xAxisNameList, dataList, theme, yAxisTitle, xAxisTitle
                , yAxisMinValue, yAxisMaxValue, legendColorList, barLabelVisible, barLabelFormat);
        try {
            ChartUtils.writeChartAsJPEG(outputStream, 1.0f, chart, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成柱状图(返回byte[])
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @param width          宽度
     * @param height         高度
     * @param yAxisMinValue   y轴最小值（可以为空）
     * @param yAxisMaxValue   y轴最大值（可以为空）
     * @param legendColorList 图例背景颜色（可以为空）
     * @param barLabelVisible 是否显示柱体标签（可以为空）
     * @param barLabelFormat  柱体标签格式（可以为空）
     * @return
     */
    public static byte[] createBarChart(String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle, int width, int height
            , Double yAxisMinValue , Double yAxisMaxValue, List<Color> legendColorList, Boolean barLabelVisible, String barLabelFormat) throws Exception {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        createBarChart(bas, chartTitle, legendNameList, xAxisNameList, dataList, theme, yAxisTitle, xAxisTitle, width, height
                , yAxisMinValue, yAxisMaxValue, legendColorList, barLabelVisible, barLabelFormat);
        byte[] byteArray = bas.toByteArray();
        return byteArray;
    }

    /**
     * 生成柱堆叠状图(返回JFreeChart)
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @return
     */
    public static JFreeChart createStackedBarChart(String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle) throws Exception {
        //设置主题，防止中文乱码
        theme = theme == null ? JFreeChartUtil.createChartTheme("") : theme;
        ChartFactory.setChartTheme(theme);
        //创建堆叠柱状图
        JFreeChart chart = ChartFactory.createStackedBarChart(chartTitle, xAxisTitle, yAxisTitle
                , JFreeChartUtil.createDefaultCategoryDataset(legendNameList, xAxisNameList, dataList));
        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        // 对柱子进行渲染
        JFreeChartUtil.setBarRenderer(chart.getCategoryPlot(), true);
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 标注位于上侧
        chart.getLegend().setPosition(RectangleEdge.TOP);
        return chart;
    }

    /**
     * 生成堆叠柱状图(返回outputStream)
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @param outputStream   输出流
     * @param width          宽度
     * @param height         高度
     * @return
     */
    public static void createStackedBarChart(OutputStream outputStream, String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle, int width, int height
    ) throws Exception {
        JFreeChart chart = createStackedBarChart(chartTitle, legendNameList, xAxisNameList, dataList, theme, yAxisTitle, xAxisTitle);
        try {
            ChartUtils.writeChartAsJPEG(outputStream, 1.0f, chart, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成堆叠柱状图(返回byte[])
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @param width          宽度
     * @param height         高度
     * @return
     */
    public static byte[] createStackedBarChart(String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle, int width, int height) throws Exception {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        createStackedBarChart(bas, chartTitle, legendNameList, xAxisNameList, dataList, theme, yAxisTitle, xAxisTitle, width, height);
        byte[] byteArray = bas.toByteArray();
        return byteArray;
    }

    /**
     * 生成折线图(返回JFreeChart)
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @return
     */
    public static JFreeChart createLineChart(String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle) throws Exception {
        //设置主题，防止中文乱码
        theme = theme == null ? JFreeChartUtil.createChartTheme("") : theme;
        ChartFactory.setChartTheme(theme);
        //创建折线图
        JFreeChart chart = ChartFactory.createLineChart(chartTitle, xAxisTitle, yAxisTitle
                , JFreeChartUtil.createDefaultCategoryDataset(legendNameList, xAxisNameList, dataList));
        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        // 对折现进行渲染
        JFreeChartUtil.setLineRender(chart.getCategoryPlot(), true, true);
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 标注位于上侧
        chart.getLegend().setPosition(RectangleEdge.TOP);
        return chart;
    }

    /**
     * 生成折线图(返回outputStream)
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @param outputStream   输出流
     * @param width          宽度
     * @param height         高度
     * @return
     */
    public static void createLineChart(OutputStream outputStream, String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle, int width, int height
    ) throws Exception {
        JFreeChart chart = createLineChart(chartTitle, legendNameList, xAxisNameList, dataList, theme, yAxisTitle, xAxisTitle);
        try {
            ChartUtils.writeChartAsJPEG(outputStream, 1.0f, chart, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成折线图(返回byte[])
     *
     * @param chartTitle     图表标题
     * @param legendNameList 图例名称列表
     * @param xAxisNameList  x轴名称列表
     * @param dataList       数据列表
     * @param theme          主题(null代表默认主题)
     * @param yAxisTitle     y轴标题
     * @param xAxisTitle     x轴标题
     * @param width          宽度
     * @param height         高度
     * @return
     */
    public static byte[] createLineChart(String chartTitle, List<String> legendNameList, List<String> xAxisNameList
            , List<List<Object>> dataList, StandardChartTheme theme, String yAxisTitle, String xAxisTitle, int width, int height) throws Exception {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        createLineChart(bas, chartTitle, legendNameList, xAxisNameList, dataList, theme, yAxisTitle, xAxisTitle, width, height);
        byte[] byteArray = bas.toByteArray();
        return byteArray;
    }

    /**
     * 生成散点图(返回JFreeChart)
     *
     * @param chartTitle 图表标题
     * @param dataset    数据集
     * @param theme      主题(null代表默认主题)
     * @param yAxisTitle y轴标题
     * @param xAxisTitle x轴标题
     * @return
     */
    public static JFreeChart createScatterPlot(String chartTitle
            , XYDataset dataset, StandardChartTheme theme, String yAxisTitle, String xAxisTitle) throws Exception {
        //设置主题，防止中文乱码
        theme = theme == null ? JFreeChartUtil.createChartTheme("") : theme;
        ChartFactory.setChartTheme(theme);
        //创建散点图
        JFreeChart chart = ChartFactory.createScatterPlot(chartTitle, xAxisTitle, yAxisTitle
                , dataset);
        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        //散点图渲染
        JFreeChartUtil.setScatterRender(chart.getXYPlot());
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 标注位于上侧
        chart.getLegend().setPosition(RectangleEdge.TOP);
        return chart;
    }

    /**
     * 生成散点图(返回outputStream)
     *
     * @param chartTitle   图表标题
     * @param dataset      数据集
     * @param theme        主题(null代表默认主题)
     * @param yAxisTitle   y轴标题
     * @param xAxisTitle   x轴标题
     * @param outputStream 输出流
     * @param width        宽度
     * @param height       高度
     * @return
     */
    public static void createScatterPlot(OutputStream outputStream, String chartTitle, XYDataset dataset, StandardChartTheme theme
            , String yAxisTitle, String xAxisTitle, int width, int height
    ) throws Exception {
        JFreeChart chart = createScatterPlot(chartTitle, dataset, theme, yAxisTitle, xAxisTitle);
        try {
            ChartUtils.writeChartAsJPEG(outputStream, 1.0f, chart, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成散点图(返回byte[])
     *
     * @param chartTitle 图表标题
     * @param dataset    数据集
     * @param theme      主题(null代表默认主题)
     * @param yAxisTitle y轴标题
     * @param xAxisTitle x轴标题
     * @param width      宽度
     * @param height     高度
     * @return
     */
    public static byte[] createScatterPlot(String chartTitle, XYDataset dataset, StandardChartTheme theme, String yAxisTitle
            , String xAxisTitle, int width, int height) throws Exception {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        createScatterPlot(bas, chartTitle, dataset, theme, yAxisTitle, xAxisTitle, width, height);
        byte[] byteArray = bas.toByteArray();
        return byteArray;
    }
}
