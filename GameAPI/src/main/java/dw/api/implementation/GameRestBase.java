package dw.api.implementation;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;

import datalayer.quiz.Quiz;
import dw.backend.businesslayer.GameEJB;
import dw.backend.datalayer.Game;
import dw.backend.datalayer.QuizObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Created by thang on 23.11.2016.
 */
public class GameRestBase {


    protected final GameEJB gameEJB = new GameEJB();

    protected HttpURLConnection getConnection(String requestUrl, String requestMethod) throws Exception {

        URL obj = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(requestMethod);
        /*
        //Hystrix here.
        if(new CallQuiz(con).execute() == 200){
            return con;
        }else{
            return null;
        }
        */
        return con;
    }

    protected void addQuizIds(Long id, String path, BufferedReader in) throws IOException {
        List<String> items = Arrays.asList(getJsonString(in).split("\\s*,\\s*"));
        for (String a : items) {
            String cleaned = removeChar(a);
            gameEJB.addQuizIdToGameList(id, path, cleaned);
        }
        in.close();
    }

    protected void addQuizToGame(Long id, BufferedReader in) throws IOException {
        //TODO: Read the JSON object and parse it to the hashmap.

        String s = getJsonString(in);
        s = s.replace("[", ",").replace("]", ",").replace("\"", "").replace("{", ",").replace("}", ",").replace(":", "").replace("questionsAndAnswersList", "");
        s = s.substring(1, s.length() - 1);
        String[] parts = s.split(",");

        Game game = gameEJB.get(id);

        QuizObject quizObject = gameEJB.createQuizObject();

        List<String> realList = new ArrayList<>();

        for (int i = 0; i < parts.length; i++) {
            if (!parts[i].contains("id") &&
                    !parts[i].contains("subSubCategoryId") &&
                    !parts[i].contains("rootCategory") &&
                    !parts[i].contains("subCategory") &&
                    !parts[i].contains("subsubCategory") &&
                    !parts[i].contains("quizName") &&
                    !parts[i].contains("Solution") &&
                    parts[i].length() >= 2) {
                realList.add(parts[i]);
            }
        }

        realList.remove("");
        realList.remove("\n");
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < realList.size(); i++) {
            temp.add(realList.get(i));
            if(temp.size() == 5){
                String question = temp.get(0);
                temp.remove(0);
                quizObject.getHashMapQuiz().put(question, temp);
                temp = new ArrayList<>();
            }
        }
        gameEJB.addQuizToGame(game.getId(), quizObject);
        in.close();
    }

    protected String getJsonString(BufferedReader in) throws IOException {
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        return response.toString();
    }

    protected String removeChar(String a) {
        return a.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
    }


    private class CallQuiz extends HystrixCommand<Integer> {

        private final HttpURLConnection con;

        protected CallQuiz(HttpURLConnection con) {
            super(HystrixCommandGroupKey.Factory.asKey("Interactions with Quiz"));
            this.con = con;
        }

        @Override
        protected Integer run() throws Exception {
                //Note: this synchronous call could fail (and so throw an exception),
                //or even just taking a long while (if server is under heavy load)
            return con.getResponseCode();
        }

        @Override
        protected Integer getFallback() {
            //this is what is returned in case of exceptions or timeouts
            return 404;
        }
    }
}
