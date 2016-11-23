package dw.api.dto;

import datalayer.quiz.Quiz;
import dw.backend.datalayer.QuizObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;


@ApiModel("A Game")
public class GameDto {
    @ApiModelProperty("The id of the game")
    public String id;

    @ApiModelProperty("The game name")
    public String gameName;

    @ApiModelProperty("Count to show how many quizzes there are in the game.")
    public int quizCount;

    @ApiModelProperty("Quizzes")
    public Map<String, List<String>> quizObjectList;

/*
    @ApiModelProperty(value = "Q and A")
    public Map<String, List<String>> questionsAndAnswersList;
*/

    @ApiModelProperty("Quiz ID's")
    public List<String> quizIDList;


    public GameDto() {
    }

    public GameDto(String id, String gameName) {
        this.id = id;
        this.gameName = gameName;
    }
}