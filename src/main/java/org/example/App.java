package org.example;

import org.example.Selector.Imp.*;
import org.htmlunit.BrowserVersion;
import org.htmlunit.NicelyResynchronizingAjaxController;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import java.io.IOException;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws IOException {
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

        webClient.waitForBackgroundJavaScript(5000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
//        DeckTag deckTag = new DeckTag();
//        deckTag.SelectTag(page);
//        deckTag.getElements(page);

//        MonsterTag monsterTag = new MonsterTag();
//        monsterTag.SelectTag(page);
//        monsterTag.getElements(page);

//        SpellTag spellTag = new SpellTag();
//        spellTag.SelectTag(page);
//        spellTag.getElements(page);

        ExTag trapTag = new ExTag();
        trapTag.SelectTag(page);
        trapTag.getElements(page);




//        String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
//        System.out.println(pageXml);
//        //TODO 下面的代码就是对字符串的操作了,常规的爬虫操作,用到了比较好用的Jsoup库
//
//        Document document = Jsoup.parse(pageXml);//获取html文档
//        List<Element> infoListEle = document.getElementsByClass("tab-img");
//
//        List<DomElement> moster = page.getByXPath("//*[@id=\"app\"]/div[2]/div/div/div[1]/div[2]/ul/li[2]/a");
//        List<DomElement> tarp = page.getByXPath("//*[@id=\"app\"]/div[2]/div/div/div[1]/div[2]/ul/li[4]/a");
//
//        tarp.get(0).click();
//        final DomNodeList<DomElement> imgs = page.getElementsByTagName("img");
//
//        List<Element> infoListEles = document.getElementById("trap").getElementsByTag("tr");//获取元素节点等
//        infoListEles.forEach(element -> {
//
//            System.out.println(element.getElementsByTag("a").text());
//
//        });
    }
}
