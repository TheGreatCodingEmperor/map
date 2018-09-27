package com.map2.map2;

import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class Wind {
    private Document doc;
    private Elements Img,texts;
    private String[] wind = new String[480];
    private String[] name = new String[480];
    public Wind(){}

    public void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "https://www.cwb.gov.tw/V7/observe/real/windAll.htm";
                    Document doc = Jsoup.connect(url)
                            .header("Accept-Encoding", "gzip, deflate")
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                            .maxBodySize(0)
                            .timeout(0)
                            .get();
                    String title = doc.title();
                    Elements Img = doc.select("td[class*=TypeWD]");
                    Elements locations = doc.select("tr[class^=TypeA]>td");

                    /*for (Element e : elements) {
                        //String WD = Img.attr("title");
                        builder.append("\n").append("City: ").append(City)
                                .append("\n").append("Town: ").append(Town);
                    }*/
                    int count=1;
                    int level=0;
                    for (Element l : locations) {
                        if (count==2) name[level]=l.text();
                        else if(count==83){count=0;level++;}
                        count++;
                    }
                    count=1;
                    level = 0;
                    for(Element i : Img)
                    {
                        if (count==1)
                        {
                            String w = i.getElementsByTag("img").attr("title");
                            if(w == null||w.equals("")) w = i.text();
                            wind[level]=w;
                        }
                        else if(count==24){count=0;level++;}
                        count++;
                    }
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }).start();
    }
    public String[] getImg()
    {
        return wind;
    }
    public String[] getTexts()
    {
        return name;
    }
}
