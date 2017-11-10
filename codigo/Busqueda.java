/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexacion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.StringTokenizer;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import static org.apache.lucene.document.IntPoint.newRangeQuery;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
/**
 *
 * @author pablo
 */
public class Busqueda {
        //1-busca usuarios
        //2-busca usuarios y los tweets de esos usuarios
        //3-busca usuairos y su nickname
        //4-buscar en tweets y mostrar fecha
        //5-busca en biografia y muestra el usuario
        //6-busca usuarios y muestra su nacionalidad
        //7-busca en tweets y muesta el lugar donde fue escrito
        //8-busca en tweets y muestra el lenguaje
        //9-busca los tweets retweeteados entre dos valores
        //10-busca los favoritos retweeteados entre dos valores
    
        
    
        final String USERNAME = "UserName";
        final String NICKNAME = "Nickname";
        final String TWEET = "TweetContent";  
        final String DATE = "Date";
        final String BIO = "Bio";
        final String COUNTRY = "Country";
        final String PLACE = "Place";
        final String LANGUAGE = "Language";  
        final String FAVS = "Favs";
        final String RTS = "RTs";   
        
        /*final String FOLLOWERS = "Followers";
        final String FOLLOWING = "Following";*/
        
        
     //Para cuando tengo la opcion de nombre de usuarios solo
    
     public void buscarUsuarios(String ruta,String busqueda) throws IOException, ParseException, Exception{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        BooleanQuery.Builder Constructor = new BooleanQuery.Builder();
        StringTokenizer st =new StringTokenizer(busqueda);
        while(st.hasMoreTokens()){
            TermQuery query = new TermQuery(new Term(USERNAME,st.nextElement().toString()));
            Constructor.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery q = Constructor.build();
        TopDocs top=isearcher.search(q,1000);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<top.scoreDocs.length;i++){
            int docId =top.scoreDocs[i].doc;
            Document foundDocument = isearcher.doc(docId);
            System.out.println(foundDocument.get(USERNAME));
        }
        System.out.println("Encontrado " + top.scoreDocs.length + "resultados");
        System.out.println("----------------------------------------------------");
        d.close();
    }
     
    
    //Para cuando tengo la opcion de nombre de usuarios con la de tweets, busca por nombre de usuario y muestra sus tweets
    
     public void buscarUsuariosYTweets(String ruta,String busqueda) throws IOException, ParseException, Exception{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        BooleanQuery.Builder Constructor = new BooleanQuery.Builder();
        StringTokenizer st =new StringTokenizer(busqueda);
        while(st.hasMoreTokens()){
            TermQuery query = new TermQuery(new Term(USERNAME,st.nextElement().toString()));
            Constructor.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery q = Constructor.build();
        TopDocs top=isearcher.search(q,1000);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<top.scoreDocs.length;i++){
            int docId =top.scoreDocs[i].doc;
            Document foundDocument = isearcher.doc(docId);
            System.out.println(foundDocument.get(USERNAME) + "  " + foundDocument.get(TWEET));
        }
        System.out.println("Encontrado " + top.scoreDocs.length + "resultados");
        System.out.println("----------------------------------------------------");
        d.close();
    }
     
     //busca por nombre de usuario y muestra su nickname
     
     public void buscarUsuariosYNickname(String ruta,String busqueda) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        BooleanQuery.Builder Constructor = new BooleanQuery.Builder();
        StringTokenizer st =new StringTokenizer(busqueda);
        while(st.hasMoreTokens()){
            TermQuery query = new TermQuery(new Term(USERNAME,st.nextElement().toString()));
            Constructor.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery q = Constructor.build();
        TopDocs top=isearcher.search(q,1000);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<top.scoreDocs.length;i++){
            int docId =top.scoreDocs[i].doc;
            Document foundDocument = isearcher.doc(docId);
            System.out.println(foundDocument.get(USERNAME) +  " @" + foundDocument.get(NICKNAME));
        }
        System.out.println("Encontrado " + top.scoreDocs.length + "resultados");
        System.out.println("----------------------------------------------------");
        d.close();
    }
     
    //busca en el contenido de los tweets y muestra el tweet y su fecha de publicacion, para cuando tenga
    //la opcion de buscar el tweet y la de fecha
      
    public void buscarEnTweetsYFecha(String ruta ,Analyzer a,String busqueda) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        BooleanQuery.Builder Constructor = new BooleanQuery.Builder();
        StringTokenizer st =new StringTokenizer(busqueda);
        while(st.hasMoreTokens()){
            TermQuery query = new TermQuery(new Term(TWEET,st.nextElement().toString()));
            Constructor.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery q = Constructor.build();
        TopDocs top=isearcher.search(q,1000);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<top.scoreDocs.length;i++){
            int docId =top.scoreDocs[i].doc;
            Document foundDocument = isearcher.doc(docId);
            System.out.println(foundDocument.get(TWEET) + foundDocument.get(DATE));
        }
        System.out.println("Encontrado " + top.scoreDocs.length + "resultados");
        System.out.println("----------------------------------------------------");
        d.close();
    }
    
    //busca en el contenido de la biografia y muestra la biografia completa y el nombre del usuario que le pertenece
    
     public void buscarBioYUsuario(String ruta,Analyzer a,String busqueda) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        QueryParser parser = new QueryParser(BIO,a);
        Query query = parser.parse(busqueda);
        ScoreDoc[] hits =isearcher.search(query,1000).scoreDocs;
        for(int i=0;i<hits.length;i++){
            Document hitDoc=isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get(BIO).toString() + "  " + hitDoc.get(USERNAME).toString());
        }
        ireader.close();
        d.close();
    }
     
    //busca por el nombre de usuario y muestra el nombre del usuario y su nacionalidad
     
    public void buscarUsuarioYNacionalidad(String ruta,Analyzer a,String busqueda) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        BooleanQuery.Builder Constructor = new BooleanQuery.Builder();
        StringTokenizer st =new StringTokenizer(busqueda);
        while(st.hasMoreTokens()){
            TermQuery query = new TermQuery(new Term(USERNAME,st.nextElement().toString()));
            Constructor.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery q = Constructor.build();
        TopDocs top=isearcher.search(q,1000);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<top.scoreDocs.length;i++){
            int docId =top.scoreDocs[i].doc;
            Document foundDocument = isearcher.doc(docId);
            System.out.println(foundDocument.get(USERNAME) + "  " + foundDocument.get(COUNTRY));
        }
        System.out.println("Encontrado " + top.scoreDocs.length + "resultados");
        System.out.println("----------------------------------------------------");
        d.close();
    }
    
    //busca en el contenido de los tweets y miestra el tweet completo y el lugar donde se publico
    
    public void buscarEnTweetsYLugar(String ruta ,Analyzer a,String busqueda) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        BooleanQuery.Builder Constructor = new BooleanQuery.Builder();
        StringTokenizer st =new StringTokenizer(busqueda);
        while(st.hasMoreTokens()){
            TermQuery query = new TermQuery(new Term(TWEET,st.nextElement().toString()));
            Constructor.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery q = Constructor.build();
        TopDocs top=isearcher.search(q,1000);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<top.scoreDocs.length;i++){
            int docId =top.scoreDocs[i].doc;
            Document foundDocument = isearcher.doc(docId);
            System.out.println(foundDocument.get(TWEET) + foundDocument.get(PLACE));
        }
        System.out.println("Encontrado " + top.scoreDocs.length + "resultados");
        System.out.println("----------------------------------------------------");
        d.close();
    }
    
    //busca en el contenido de los tweets y los muestra completos y el lenguaje en el que estan escritos
    
    public void buscarEnTweetsYLanguage(String ruta ,Analyzer a,String busqueda) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);
        BooleanQuery.Builder Constructor = new BooleanQuery.Builder();
        StringTokenizer st =new StringTokenizer(busqueda);
        while(st.hasMoreTokens()){
            TermQuery query = new TermQuery(new Term(TWEET,st.nextElement().toString()));
            Constructor.add(query, BooleanClause.Occur.MUST);
        }
        BooleanQuery q = Constructor.build();
        TopDocs top=isearcher.search(q,1000);
        System.out.println("----------------------------------------------------");
        for(int i=0;i<top.scoreDocs.length;i++){
            int docId =top.scoreDocs[i].doc;
            Document foundDocument = isearcher.doc(docId);
            System.out.println(foundDocument.get(TWEET) + foundDocument.get(LANGUAGE));
        }
        System.out.println("Encontrado " + top.scoreDocs.length + "resultados");
        System.out.println("----------------------------------------------------");
        d.close();
    }
    
    //Se muestran los tweets retweeteados entre los valores a y b, EL PROBLEMA ES QUE NO SE PUEDE SABER CUANTOS TIENE CADA UNO
    //BORRA LO QUE ESTA EN MAYUSCULAS
    
    public void buscarMasReTweets(String ruta,int a , int b) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);  
        Query q = newRangeQuery(RTS,a,b);      
        ScoreDoc[] hits =isearcher.search(q,1000).scoreDocs;
        for(int i=0;i<hits.length;i++){
            Document hitDoc=isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get(TWEET).toString());
        }
        ireader.close();
        d.close();
    }
    
    //Se muestran los tweets favoritos entre los valores a y b, EL PROBLEMA ES QUE NO SE PUEDE SABER CUANTOS TIENE CADA UNO
    //BORRA LO QUE ESTA EN MAYUSCULAS
 
    public void buscarMasFavs(String ruta,int a , int b) throws IOException, ParseException{
        Directory d=FSDirectory.open(Paths.get(ruta));
        IndexReader ireader=DirectoryReader.open(d);
        IndexSearcher isearcher =new IndexSearcher(ireader);  
        Query q = newRangeQuery(FAVS,a,b);      
        ScoreDoc[] hits =isearcher.search(q,1000).scoreDocs;
        for(int i=0;i<hits.length;i++){
            Document hitDoc=isearcher.doc(hits[i].doc);
            System.out.println(hitDoc.get(TWEET).toString());
        }
        ireader.close();
        d.close();
    }
          
    public static void main(String[] args) throws Exception {
        String indexDir = args[0];
        
        Busqueda b = new Busqueda();
        Analyzer analyzer=new StandardAnalyzer();
        
        //LOS SIGUIENTES SON BUENOS EJEMPLOS PARA LA PRESENTACION
        
        //b.buscarBioYUsuario(indexDir, analyzer, "juan");
        //b.buscarEnTweetsYFecha(indexDir, analyzer, "monumento barcelona");
        //b.buscarEnTweetsYLanguage(indexDir, analyzer, "monumento estrellarse");
        //b.buscarEnTweetsYLugar(indexDir, analyzer, "monumento pinal");
        
        //Para ver que funciona probalos desde los que tienen 1 a 100 y desde 99 a 100
        //b.buscarMasFavs(indexDir, 1, 100);
        //b.buscarMasFavs(indexDir, 99, 100);
        
        //Para ver que funciona probalos desde los que tienen 1 a 100 y desde 90 a 100
        //b.buscarMasReTweets(indexDir, 1, 100);
        //b.buscarMasReTweets(indexDir, 90, 100);
        
        //b.buscarUsuarioYNacionalidad(indexDir, analyzer, "juan jose");
        //b.buscarUsuarios(indexDir, "juan jose");
        //b.buscarUsuariosYNickname(indexDir, "juan jose");
        b.buscarUsuariosYTweets(indexDir, "juan pedro");
        
        
        
    }  
}
