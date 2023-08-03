package org.example.Selector;

import org.htmlunit.html.HtmlPage;
import org.jsoup.nodes.Element;

import java.util.List;

public interface TagSelector {

    void SelectTag(HtmlPage page);

    void getElements(HtmlPage page);
}
