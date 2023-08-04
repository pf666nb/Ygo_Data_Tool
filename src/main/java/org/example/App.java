package org.example;

import org.example.Bean.DataBean;
import org.example.Config.WebClientConfig;
import org.example.Selector.Imp.*;
import org.example.Utils.GenerateYgoPieChartUtil;
import org.htmlunit.BrowserVersion;
import org.htmlunit.NicelyResynchronizingAjaxController;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.List;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws Exception {
        WebClient webClient = WebClientConfig.GetClient();
        HtmlPage page = null;
        try {
            page = webClient.getPage("https://mycard.moe/ygopro/arena/#/cards");
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
        List<DataBean> elements = trapTag.getElements(page);
        GenerateYgoPieChartUtil.generateYgoPie(elements);


    }
}
