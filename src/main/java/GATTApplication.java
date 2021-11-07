import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GATTApplication {

    public GATTApplication() {
        // turn off htmlunit warnings
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
    }

    protected List<HtmlAnchor> getLinks() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        List<HtmlAnchor> links=null;

        try {
            HtmlPage page = webClient.getPage("https://en.wikipedia.org/wiki/Intuit");
            HtmlElement body = page.getBody();
            links= page.getAnchors();
            //close connections
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
            // recipesFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return links;
    }
    public void getLinksJsoup(){
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        List<HtmlAnchor> links=null;

        try {
            Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Intuit").get();
            Elements body=doc.select("div[class=vector-body] div[class=mw-parser-output] a" )
                    .not("table[class=infobox vcard]");
            for(int i=0;i< body.size();i++){
                String title=body.get(i).text();
                String href=body.get(i).attr("href");
               if(href.startsWith("/")||href.startsWith("https")&&!title.isEmpty())
                    System.out.println("Title: "+title+" Link: "+href);
            }


        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }
   public void printLinks(){
       List<HtmlAnchor> links=getLinks();
       for (HtmlAnchor link : links) {
           String name = link.getTextContent();
           String href = link.getHrefAttribute();
           if (href.startsWith("https") || href.startsWith("/wiki")) {
               System.out.println("Name: " + name + " Link: " + href);
           }
       }
   }
   protected ArrayList<FinanceDetails> getFinanceTable(){
       ArrayList<FinanceDetails> financeDetails=new ArrayList<FinanceDetails>();
       try {
               Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Intuit").get();
               Elements ths=null;
               for (Element table : doc.select("table[class=wikitable float-left]")) {
                   int j=1;
                   for (Element row : table.select("tr")) {
                       if(row.select("th").size()>0)
                            ths = row.select("th");
                       Elements tds = row.select("td");
                       if( tds.size()>0) {
                           tds = row.select("td");
                           int i=0;
                           for (Element td : tds) {
                               FinanceDetails finance=new FinanceDetails();
                               String th=ths.get(i).text();
                               finance.set(j,th,td.text());
                               financeDetails.add(finance);
                              // System.out.println("Index: "+j+" "+th+" "+td.text());
                               i++;
                           }
                           j++;
                       }

                   }

               }
       } catch (Exception e) {
           System.out.println("An error occurred: " + e);
       }
       return financeDetails;
   }
   public void printFinanceTable(){
        ArrayList<FinanceDetails> financeDetails=getFinanceTable();

        try {
            for (int i = 0; i < financeDetails.size(); i++) {
                System.out.println("[" + financeDetails.get(i).recordName + "," +
                        financeDetails.get(i).columnName + "," + financeDetails.get(i).value + "]");
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e);
    }
   }
   public void getCategories(){
       try {
           FileWriter categories = new FileWriter("categories.txt", true);
           Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Intuit").get();
           for (Element table : doc.select("div[class=toc]")) {
               for (Element li : table.select("li")) {
                   Elements a=li.select("a");
                   System.out.println(li.text());
                   categories.write(li.text());
                   for (Element href:a) {
                       String ref=href.attr("href");
                       ref="https://en.wikipedia.org/wiki/Intuit"+ref;
                       Document d = Jsoup.connect(ref).get();
                       categories.write(d.html());
                       System.out.println(d.html());
                   }
               }
               categories.close();
           }
       } catch (Exception e) {
           System.out.println("An error occurred: " + e);
       }
   }
   public void getPopularUser(){
        try {
            Document doc = Jsoup.connect("https://xtools.wmflabs.org/articleinfo/en.wikipedia.org/Intuit").get();
            Elements user=doc.select("td[class=sort-entry--username] a");
            String url=user.attr("href");
            String name= user.text();
            name=name.split(" ")[0];
            System.out.println("The Most Active User: "+name);
            Document d = Jsoup.connect(url).get();
            Elements userBox=d.select("div[class=wikipediauserbox] td");
            System.out.println("Details on Active User: "+userBox.text());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        }
   }

}