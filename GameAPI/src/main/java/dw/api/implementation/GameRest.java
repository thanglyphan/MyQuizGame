package dw.api.implementation;

import com.google.common.base.Throwables;
import dw.api.dto.GameConverter;
import dw.api.dto.GameDto;
import dw.api.rest.GameRestApi;
import dw.backend.datalayer.Game;
import dw.backend.datalayer.QuizObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
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

    private String quizAddress = System.getProperty("foo", "localhost:8080");

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
    public Long createGame(String x, String gameName, String secondParam) throws Exception {
        int quizCount = Integer.parseInt(x);
        Long id;
        Game game;

        //Now, find quizzes from the QuizApi with http call, LIMIT quizCount. Then add to "game"
        String REQUEST_URL = "http://"+quizAddress+"/myrest/api/randomquiz/randomQuizzes?n=" + x + "&filter=" + secondParam;
        HttpURLConnection con = getConnection(REQUEST_URL, "POST");

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK ) { // success
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
    public Response answerGameByID(@ApiParam("The numeric id of the games") Long id, String qid, String answer) throws Exception {

        //Init
        String theAnswer = answer;
        String correct = "Yalla";
        String questionForUser = "";
        String pathForQuestion = "http://localhost:8080/myrest/api/qa/question/" + qid; //This question I want to answer.
        boolean quitInstant = false;
        Game game;
        try {
            game = gameEJB.get(id);
        } catch (Exception e) {
            throw wrapException(e);
        }
        HttpURLConnection connection = getConnection(pathForQuestion, "GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            questionForUser = moddedToQuestionOnly(getJsonString(new BufferedReader(new InputStreamReader(connection.getInputStream()))));
            String questionForUserModded = URLEncoder.encode(questionForUser, "UTF-8").replace("+", "%20");
            //Create a list with ID for quiz only.
            List<String> idOnly = new ArrayList<>();
            for (String a : game.getQuizIdList()) {
                idOnly.add(a.substring(a.lastIndexOf("/") + 1));
            }

            for (int i = 0; i < game.getQuizIdList().size(); i++) {
                HttpURLConnection con = getConnection("http://localhost:8080/myrest/api/qa/" + idOnly.get(i) + "/" + questionForUserModded, "GET");
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String solution = getJsonString(new BufferedReader(new InputStreamReader(con.getInputStream()))).toLowerCase();

                    if (solution.contains(theAnswer.toLowerCase())) {
                        correct = "You answered question: " + questionForUser + " with answer: " + theAnswer + ". That is correct!";

                        //User has answered correctly, remove the question/quiz from game, and if the hashmap isempty, remove the whole quiz.
                        for (QuizObject quizObject : game.getQuizList()) {
                            if (quizObject.getHashMapQuiz().containsKey(qid + "; " + questionForUser)) {
                                quizObject.getHashMapQuiz().remove(qid + "; " + questionForUser);
                            }
                        }
                        break;
                    } else {
                        quitInstant = true;
                    }
                }
            }
        } else {
            correct = "Wrong input, try again";
        }
        System.out.println(game.getQuizList().size());

        for (QuizObject a : game.getQuizList()) {
            if(a != null){
                if (a.getHashMapQuiz().isEmpty()) {
                    game.getQuizList().remove(a);
                }

                if (game.getQuizList().isEmpty() || quitInstant) {
                    Game temp = game;
                    delete(game.getId());
                    if (quitInstant) {
                        correct = "Make new game, try again! You answered wrong.";
                    } else {
                        correct = "You completed the game! The last question was: "
                                + questionForUser + ". You answered: " + theAnswer +
                                ". That is correct! Gz, " + temp.getGameName() + " is now finished, this is the end :)";
                    }
                    return Response.ok(correct, MediaType.TEXT_PLAIN).build();
                }
            }

        }

        return Response.ok(correct, MediaType.TEXT_PLAIN).build();
    }

    private String moddedToQuestionOnly(String a) {
        String s = a.replace("[", ",").replace("]", ",").replace("\"", "").replace("{", ",").replace("}", ",").replace(":", "").replace("questionsAndAnswersList", "");
        s = s.substring(1, s.length() - 1);
        String[] parts = s.split(",");
        return parts[0];
    }

    private void holdMyShit() {
        //Get game by ID
        /*
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

        /*
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
                }
            }
        }
        */
    }

    @Override
    public void delete(@ApiParam(ID_PARAM) Long id) {
        gameEJB.deleteGame(id);
    }

    protected WebApplicationException wrapException(Exception e) throws WebApplicationException {

        /*
            Errors:
            4xx: the user has done something wrong, eg asking for something that does not exist (404)
            5xx: internal server error (eg, could be a bug in the code)
         */

        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
