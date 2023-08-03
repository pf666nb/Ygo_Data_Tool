package org.example.Selector.Imp;

import org.apache.http.util.Asserts;
import org.example.Selector.TagSelector;
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
 *
 * @author : wpf
 * @date : 2023-08-03 12:02
 **/
public class DeckTag implements TagSelector {

    public final  String TAG_XPATH = "//*[@id=\"app\"]/div[2]/div/div/div[1]/div[2]/ul/li[1]/a";
    public final  String TAG = "deck";

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
    public void getElements(HtmlPage page) {
        Document document = Jsoup.parse(page.asXml());
        List<Element> elementList = document.getElementById(TAG).getElementsByTag(TR);//获取元素节点等
        for (int i = 1; i < elementList.size(); i++) {
            doTrParser(elementList.get(i));
        }

    }

    private void doTrParser(Element element){
        Elements elementsByTag = element.getElementsByTag(TD);
        System.out.println("排名:"+elementsByTag.get(0).text());
        System.out.println("卡组:"+elementsByTag.get(1).getElementsByTag("a").text());
        System.out.println("使用数:"+elementsByTag.get(2).text());
        System.out.println("使用率:"+elementsByTag.get(3).text());
        System.out.println("热门标签:"+elementsByTag.get(4).text());


    }

}
