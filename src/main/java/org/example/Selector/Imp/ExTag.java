package org.example.Selector.Imp;

import org.example.Bean.DataBean;
import org.example.CardEnum.CardType;
import org.example.Selector.TagSelector;
import org.example.Utils.GetBeanUtil;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * gecco
 * 额外卡组对应的筛选器
 *
 * @author : wpf
 * @date : 2023-08-03 15:00
 **/
public class ExTag implements TagSelector {
    public final  String TAG_XPATH = "//*[@id=\"app\"]/div[2]/div/div/div[1]/div[2]/ul/li[6]/a";
    public final  String TAG = "ex";

    public final  String TD = "td";

    public final  String TR = "tr";
    @Override
    public void SelectTag(HtmlPage page) {
        List<DomElement> byXPath = page.getByXPath(TAG_XPATH);
        try {
            byXPath.get(0).click();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DataBean> getElements(HtmlPage page) {
        Document document = Jsoup.parse(page.asXml());
        List<Element> elementList = document.getElementById(TAG).getElementsByTag(TR);//获取元素节点等
        for (int i = 1; i < elementList.size(); i++) {
            doTrParser(elementList.get(i));
        }
        return null;
    }

    private void doTrParser(Element element){
        Elements elementsByTag = element.getElementsByTag(TD);
         DataBean build = GetBeanUtil.build(elementsByTag, CardType.EX);
        System.out.println(build);


    }
}
