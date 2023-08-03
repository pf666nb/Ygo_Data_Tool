package org.example.Selector;

import org.example.Bean.DataBean;
import org.htmlunit.html.HtmlPage;
import org.jsoup.nodes.Element;

import java.util.List;

public interface TagSelector {

    void SelectTag(HtmlPage page);

    List<DataBean> getElements(HtmlPage page);
}
