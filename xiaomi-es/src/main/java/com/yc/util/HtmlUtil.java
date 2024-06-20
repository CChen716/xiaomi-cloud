package com.yc.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 爬虫数据类案例
 */
public class HtmlUtil {
    public static void main(String[] args) throws IOException {
        //获取请求  https://www.mi.com/shop
        String url="https://search.jd.com/Search?keyword=java";
        //解析网页  Jsoup返回Document就是浏览器的Document对象
        Document document= Jsoup.parse(new URL(url),30000);
        //所有在js中用到的方法，在这里都可以用
         Element element=document.getElementById("J_goodsList");
        //获取所有的li元素 这里的li就是每一个li标签
        Elements elements=element.getElementsByTag("li");


        for (Element el : elements) {
            String price=el.getElementsByClass("p-price").eq(0).text();
            String desc=el.getElementsByClass("p-name p-name-type-2").eq(0).text();
            System.out.println("===============================");
            System.out.println(price);
            System.out.println(desc);
        }

    }
}
