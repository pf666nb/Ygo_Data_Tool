package org.example.Utils;

/**
 * @author tqf
 * @Description 饼图生成工具类
 * @Version 1.0
 * @since 2022-06-07 09:50
 */
import cn.hutool.core.collection.CollectionUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.util.Rotation;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class GeneratePieChartUtil {
    /**
     * 生成饼图(返回JFreeChart)
     *
     * @param chartTitle         图表标题
     * @param legendNameList     图例名称列表
     * @param dataList           数据列表
     * @param theme              主题(null代表默认主题)
     * @param legendColorList    图例背景颜色列表（为空，使用默认背景颜色）
     * @param explodePercentList 偏离百分比数据
     * @return
     */
    public static JFreeChart createPieChart(String chartTitle, List<String> legendNameList, List<Object> dataList, StandardChartTheme theme
            , List<Color> legendColorList, List<Double> explodePercentList) throws Exception {
        //设置主题，防止中文乱码
        theme = theme == null ? JFreeChartUtil.createChartTheme("") : theme;
        ChartFactory.setChartTheme(theme);
        //创建饼图
        JFreeChart chart = ChartFactory.createPieChart(chartTitle, JFreeChartUtil.createDefaultPieDataset(legendNameList, dataList));
        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        PiePlot piePlot = (PiePlot) chart.getPlot();
        //边框线为白色
        piePlot.setOutlinePaint(Color.white);
        //连接线类型为直线
        piePlot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
        // 对饼图进行渲染
        JFreeChartUtil.setPieRender(chart.getPlot());
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 标注位于右侧
        chart.getLegend().setPosition(RectangleEdge.RIGHT);

        //设置图例背景颜色（饼图）
        if (CollectionUtil.isNotEmpty(legendColorList)) {
            for (int i = 0; i < legendNameList.size() && i < legendColorList.size(); i++) {
                Color color = legendColorList.get(i);
                if (color == null) {
                    continue;
                }
                piePlot.setSectionPaint(legendNameList.get(i), color);
            }
        }
        //设置偏离百分比
        if (CollectionUtil.isNotEmpty(explodePercentList)) {
            for (int i = 0; i < legendNameList.size() && i < explodePercentList.size(); i++) {
                piePlot.setExplodePercent(legendNameList.get(i), explodePercentList.get(i));
            }
        }
        return chart;
    }

    /**
     * 生成饼图（返回byte[])
     *
     * @param chartTitle         图表标题
     * @param legendNameList     图例名称列表
     * @param dataList           数据列表
     * @param width              宽度
     * @param height             高度
     * @param theme              主题(null代表默认主题)
     * @param legendColorList    图例背景颜色列表（为空，使用默认背景颜色）
     * @param explodePercentList 偏离百分比数据
     * @return
     */
    public static byte[] createPieChart(String chartTitle, List<String> legendNameList, List<Object> dataList, int width, int height
            , StandardChartTheme theme, List<Color> legendColorList, List<Double> explodePercentList) throws Exception {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        createPieChart(bas, chartTitle, legendNameList, dataList, width, height, theme, legendColorList, explodePercentList);
        byte[] byteArray = bas.toByteArray();
        return byteArray;
    }

    /**
     * 生成饼图(返回outputStream)
     *
     * @param outputStream       输出流
     * @param chartTitle         图表标题
     * @param legendNameList     图例名称列表
     * @param dataList           数据列表
     * @param width              宽度
     * @param height             高度
     * @param theme              主题(null代表默认主题)
     * @param legendColorList    图例背景颜色列表（为空，使用默认背景颜色）
     * @param explodePercentList 偏离百分比数据
     * @return
     */
    public static void createPieChart(OutputStream outputStream, String chartTitle, List<String> legendNameList, List<Object> dataList
            , int width, int height, StandardChartTheme theme, List<Color> legendColorList, List<Double> explodePercentList) throws Exception {
        JFreeChart chart = createPieChart(chartTitle, legendNameList, dataList, theme, legendColorList, explodePercentList);
        try {
            ChartUtils.writeChartAsJPEG(outputStream, 1.0f, chart, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成3D饼图(返回JFreeChart)
     *
     * @param chartTitle      图表标题
     * @param legendNameList  图例名称列表
     * @param dataList        数据列表
     * @param theme           主题(null代表默认主题)
     * @param alpha           0.5F为半透明，1为不透明，0为全透明
     * @param legendColorList 图例背景颜色列表（为空，使用默认背景颜色）
     * @return
     */
    public static JFreeChart create3DPieChart(String chartTitle, List<String> legendNameList, List<Object> dataList, StandardChartTheme theme
            , float alpha, List<Color> legendColorList) throws Exception {
        //设置主题，防止中文乱码
        theme = theme == null ? JFreeChartUtil.createChartTheme("") : theme;
        ChartFactory.setChartTheme(theme);
        //创建饼图
        JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, JFreeChartUtil.createDefaultPieDataset(legendNameList, dataList)
                , true, true, true);
        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 标注位于右侧
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        PiePlot3D pieplot3d = (PiePlot3D) chart.getPlot();
        //设置方向为”顺时针方向“
        pieplot3d.setDirection(Rotation.CLOCKWISE);
        //设置透明度，0.5F为半透明，1为不透明，0为全透明
        pieplot3d.setForegroundAlpha(alpha);
        //边框线为白色
        pieplot3d.setOutlinePaint(Color.white);
        //连接线类型为直线
        pieplot3d.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
        //设置图例背景颜色（饼图）
        if (CollectionUtil.isNotEmpty(legendColorList)) {
            for (int i = 0; i < legendNameList.size() && i < legendColorList.size(); i++) {
                pieplot3d.setSectionPaint(legendNameList.get(i), legendColorList.get(i));
            }
        }
        // 对饼图进行渲染
        JFreeChartUtil.setPieRender(chart.getPlot());
        return chart;
    }

    /**
     * 生成3D饼图（返回byte[])
     *
     * @param chartTitle      图表标题
     * @param legendNameList  图例名称列表
     * @param dataList        数据列表
     * @param width           宽度
     * @param height          高度
     * @param theme           主题(null代表默认主题)
     * @param alpha           0.5F为半透明，1为不透明，0为全透明
     * @param legendColorList 图例背景颜色列表（为空，使用默认背景颜色）
     * @return
     */
    public static byte[] create3DPieChart(String chartTitle, List<String> legendNameList, List<Object> dataList, int width, int height
            , StandardChartTheme theme, float alpha, List<Color> legendColorList) throws Exception {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        create3DPieChart(bas, chartTitle, legendNameList, dataList, width, height, theme, alpha, legendColorList);
        byte[] byteArray = bas.toByteArray();
        return byteArray;
    }

    /**
     * 生成3D饼图(返回outputStream)
     *
     * @param outputStream    输出流
     * @param chartTitle      图表标题
     * @param legendNameList  图例名称列表
     * @param dataList        数据列表
     * @param width           宽度
     * @param height          高度
     * @param theme           主题(null代表默认主题)
     * @param alpha           0.5F为半透明，1为不透明，0为全透明
     * @param legendColorList 图例背景颜色列表（为空，使用默认背景颜色）
     * @return
     */
    public static void create3DPieChart(OutputStream outputStream, String chartTitle, List<String> legendNameList, List<Object> dataList
            , int width, int height, StandardChartTheme theme, float alpha, List<Color> legendColorList) throws Exception {
        JFreeChart chart = create3DPieChart(chartTitle, legendNameList, dataList, theme, alpha, legendColorList);
        try {
            ChartUtils.writeChartAsJPEG(outputStream, 1.0f, chart, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
