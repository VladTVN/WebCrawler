import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Crawler {
    private static String word = "be";
    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/";

        craw(1, url, new ArrayList<String>());
    }

    private static void craw(int level, String url, ArrayList<String> visitedUrls) {
        if(level <= 5){
            Document doc = getDocument(url, visitedUrls);

            if(doc !=  null) {
                int countWord = countWord(word,doc);
                writeInFile("\"" + word +"\"" +
                        " meets: " + countWord + " times" +
                        "\n--------------------");
                for (Element link : doc.select("a[href]")){
                    String next_link = link.absUrl("href");
                    if (!visitedUrls.contains(next_link)){
                        craw(level++, next_link, visitedUrls);
                    }
                }
            }
        }
    }

    public static void writeInFile(String text){
        File file = new File("newFile.txt");
        try(FileWriter writer = new FileWriter(file,true)) {
            writer.write(text + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countWord(String countWord, Document doc){
        int counter = 0;
        String[] docText = doc.text().toLowerCase().replaceAll("(\\w+)\\p{Punct}(\\s|$)", "$1$2").split(" ");
        for(String word : docText){
            if (word.equals(countWord)){
                counter++;
            }
        }
        return counter;
    }

    public static Document getDocument(String url, ArrayList<String> visitedUrls){
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if(con.response().statusCode() == 200){
                System.out.println("Crawling: " + url);
                writeInFile("Link: " + url);
                writeInFile(doc.title());
                visitedUrls.add(url);
                return doc;
            }else return null;

        }catch (IOException e){
            return null;
        }

    }

}
