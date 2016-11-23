package dw.api.implementation;

import dw.api.dto.GameConverter;
import dw.api.dto.GameDto;
import dw.api.rest.GameRestApi;
import dw.backend.datalayer.Game;
import dw.backend.datalayer.QuizObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thang on 20.11.2016.
 */
public class GameRest extends GameRestBase implements GameRestApi {

    @Override
    public List<GameDto> get(String x) {
        int count = Integer.parseInt(x);

        List<Game> list = gameEJB.getAll();

        List<Game> modifiedList = new ArrayList<>();
        Collections.shuffle(list);

        for (Game a : list) {
            if (modifiedList.size() < count) {
                modifiedList.add(a);
            }
        }
        return GameConverter.transform(modifiedList);
    }

    @Context
    private HttpServletResponse response;

    @Override
    public Long createGame(String x, String gameName, String secondParam) throws IOException {
        int quizCount = Integer.parseInt(x);
        Long id;
        Game game;

        //Now, find quizzes from the QuizApi with http call, LIMIT quizCount. Then add to "game"
        String REQUEST_URL = "http://localhost:8080/myrest/api/randomquiz/randomQuizzes?n=" + x + "&filter=" + secondParam;
        HttpURLConnection con = getConnection(REQUEST_URL, "POST");

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            //Create the game first.
            id = gameEJB.createGame(gameName);

            //Set response header and add quiz ID's to arraylist. Just to have it.
            response.setHeader("Location", "games/" + id);
            String redirectPath = "http://localhost:8080/myrest/api/quizzes/";
            addQuizIds(id, redirectPath, in); //Just an extra, not important here.

            //Make a new http request.
            game = gameEJB.get(id);
            for (String a : game.getQuizIdList()) {
                HttpURLConnection connection = getConnection(a, "GET");
                addQuizToGame(game.getId(), new BufferedReader(new InputStreamReader(connection.getInputStream())));
            }
        } else {
            System.out.println("GET request not worked:: " + con.getResponseCode());
            throw new WebApplicationException("Not enough quizzes :: >" + x, 405);
        }
        return game.getId();
    }

    @Override
    public GameDto getGameByID(@ApiParam(ID_PARAM) Long id) {
        return GameConverter.transform(gameEJB.get(id));
    }

    @Override
    public Response answerGameByID(@ApiParam("The numeric id of the games") Long id, String answer) throws IOException {
        //Init
        String theAnswer = answer;
        String correct = "Yalla";
        Set<String> solution = new HashSet<>();
        String questionForUser = "";

        //Get game by ID
        Game game = gameEJB.get(id);
        List<String> listModded = new ArrayList<>();
        List<String> questionList = new ArrayList<>();
        //Create a list with "filter params" to use for request.
        for (QuizObject a : game.getQuizList()) {
            for (String question : a.getHashMapQuiz().keySet()) {
                String moddedText = URLEncoder.encode(question, "UTF-8").replace("+", "%20");
                listModded.add(moddedText);
                questionList.add(question);
            }
        }

        //Get ONE specific question for user, after shuffle the questions.
        Collections.shuffle(questionList);
        questionForUser = questionList.get(0);

        //Create a list with ID for quiz only.
        List<String> idOnly = new ArrayList<>();
        for(String a: game.getQuizIdList()){
            idOnly.add(a.substring(a.lastIndexOf("/") + 1));
        }

        //Sort ID's
        java.util.Collections.sort(idOnly);

        //Getting the answers to the quiz by id and question-"filter". The URI is: GET /qa/{id}/{answerToQuestion}
        for(int i = 0; i < idOnly.size(); i++){
            for(String a: listModded){
                HttpURLConnection connection = getConnection("http://localhost:8080/myrest/api/qa/" + idOnly.get(i) + "/" + a, "GET");
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String s = getJsonString(new BufferedReader(new InputStreamReader(connection.getInputStream())));
                    solution.add(s.toLowerCase());
                }
            }
        }

        /*
            Checking if answer is correct by first:
            1. Get quizzes in the game
            2. Get the question in each quiz(key in hashmap)
            3. Hashmap stores a List<String> with answers, check if my answer is one of them.
            4. Check one more time if that answer is in the solution list.
         */
        for(QuizObject a: game.getQuizList()){
            if(a.getHashMapQuiz().get(questionForUser) != null){
                if(a.getHashMapQuiz().get(questionForUser).stream().map(String::toLowerCase)
                        .collect(Collectors.toList())
                        .stream()
                        .anyMatch(n -> n.contains(theAnswer.toLowerCase()))
                        && solution.contains(theAnswer.toLowerCase())){
                    correct = "You answered question: " + questionForUser + " with answer: " + theAnswer + ". That is correct!";
                    a.getHashMapQuiz().remove(questionForUser);
                    break;
                }else{
                    correct = "You answered question: " + questionForUser + " with answer: " + theAnswer + ". That is incorrect! The end.";
                    /*
                    java -Ddw.server.applicationConnectors[0].port=9090 -Ddw.server.adminConnectors[0].port=9091 -jar target/GameAPI-0.0.1-SNAPSHOT.jar server
                     */
                }
            }
        }

        return Response.ok(correct, MediaType.TEXT_PLAIN).build();
    }

    @Override
    public void delete(@ApiParam(ID_PARAM) Long id) {
        gameEJB.deleteGame(id);
    }


}
