package org.example.Utils;

import org.example.Bean.DataBean;
import org.example.CardEnum.CardType;
import org.jsoup.select.Elements;

/**
 * Ygo_Data_Tool
 * 根据Element来构建bean
 *
 * @author : wpf
 * @date : 2023-08-03 16:18
 **/
public class GetBeanUtil {
    public static DataBean build(Elements elements, CardType cardType){
        DataBean dataBean = new DataBean();
        dataBean.setOrder(elements.get(0).text());
        dataBean.setName(elements.get(1).getElementsByTag("a").text());
        dataBean.setType(cardType.getType());
        dataBean.setAttribute1(elements.get(2).text());
        dataBean.setAttribute2(elements.get(3).text());
        dataBean.setAttribute3(elements.get(4).text());
        //如果大于5说明不是deck页面的，deck页面只有5个栏位
        if (elements.size()>5){
            dataBean.setAttribute4(elements.get(4).text());
            dataBean.setAttribute5(elements.get(5).text());
        }
        return  dataBean;

    }
}
