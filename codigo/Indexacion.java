/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexacion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.store.FSDirectory;


/**
 *
 * @author salvador
 */
public class Indexacion {
    
    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        /*Date UserName Nickname Bio TweetContent Favs RTs Country 
        Place (as appears on Bio) Followers Following Language*/
        final String DATE = "Date";
        final String USERNAME = "UserName";
        final String NICKNAME = "Nickname";
        final String BIO = "Bio";
        final String TWEET = "TweetContent";
        final String FAVS = "Favs";
        final String RTS = "RTs";
        final String COUNTRY = "Country";
        final String PLACE = "Place";
        final String FOLLOWERS = "Followers";
        final String FOLLOWING = "Following";
        final String LANGUAGE = "Language";
        
        
       
        
        try {            
            Analyzer analyzer = new StandardAnalyzer();
            // Store the index in memory:
            //Directory directory = new RAMDirectory();
            // en disco ... 
            Directory directory = FSDirectory.open(Paths.get("/home/salvador/Escritorio/UGR/RI/practica-final/index"));
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter iwriter = new IndexWriter(directory, config);
            
            BufferedReader bf = null;
            try {
                bf = new BufferedReader(new FileReader(args[0]));
                String bfRead = bf.readLine();
                while ((bfRead = bf.readLine()) != null) {
                    String[] text = bfRead.split("\t");
                    ArrayList<String> listaTokens = new ArrayList<>();
                    for (String s : text){
                        if (s.isEmpty())
                            listaTokens.add("null");
                        else
                            listaTokens.add(s);
                    }
                    Document doc = new Document();
                    doc.add(new TextField(DATE, listaTokens.get(0), Field.Store.YES));
                    doc.add(new TextField(USERNAME, listaTokens.get(1), Field.Store.YES));
                    doc.add(new TextField(NICKNAME, listaTokens.get(2), Field.Store.YES));
                    doc.add(new TextField(BIO, listaTokens.get(3), Field.Store.YES));
                    doc.add(new StringField(TWEET, listaTokens.get(4), Field.Store.YES));
                    if (!listaTokens.get(5).equals("null"))
                        doc.add(new IntPoint(FAVS, Integer.parseInt(listaTokens.get(5))));
                    if (!listaTokens.get(6).equals("null"))
                        doc.add(new IntPoint(RTS, Integer.parseInt(listaTokens.get(6))));
                    doc.add(new TextField(COUNTRY, listaTokens.get(7), Field.Store.YES));
                    doc.add(new TextField(PLACE, listaTokens.get(8), Field.Store.YES));
                    /*if (!listaTokens.get(9).equals("null"))
                        doc.add(new IntPoint(FOLLOWERS, Integer.parseInt(listaTokens.get(9))));
                    if (!listaTokens.get(10).equals("null"))
                        doc.add(new IntPoint(FOLLOWING, Integer.parseInt(listaTokens.get(10))));*/
                    doc.add(new TextField(LANGUAGE, listaTokens.get(11), Field.Store.YES));
                    
                    
                    
                    iwriter.addDocument(doc);
                }
            } catch (Exception ex) {
                Logger.getLogger(Indexacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            iwriter.close();
        } catch (Exception ex) {
            Logger.getLogger(Indexacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
