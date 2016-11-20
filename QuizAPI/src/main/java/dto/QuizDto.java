package dto;

import businesslayer.QuizEJB;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import datalayer.essentials.Question;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.ejb.EJB;
import java.util.List;
import java.util.Map;


@ApiModel("A Quiz")
public class QuizDto {
    @ApiModelProperty("The id of the QUIZ")
    public String id;
    /*
    @ApiModelProperty("The root category id")
    public String rootId;
    @ApiModelProperty("The sub category id")
    public String subCategoryId;
    */
    @ApiModelProperty("The sub sub category id")
    public String subSubCategoryId;
    @ApiModelProperty(value = "The root category", hidden = true)
    public String rootCategory;
    @ApiModelProperty(value = "The sub category", hidden = true)
    public String subCategory;
    @ApiModelProperty(value = "The sub sub category", hidden = true)
    public String subsubCategory;
    @ApiModelProperty("The quiz name")
    public String quizName;
    /*
    @ApiModelProperty(value = "The question", notes = "POST/PUT only")
    public String question;
    @ApiModelProperty(value = "Choice one", notes = "POST/PUT only")
    public String choiceOne;
    @ApiModelProperty(value = "Choice two", notes = "POST/PUT only")
    public String choiceTwo;
    @ApiModelProperty(value = "Choice three", notes = "POST/PUT only")
    public String choiceThree;
    @ApiModelProperty(value = "Choice four", notes = "POST/PUT only")
    public String choiceFour;'
    */
    /*
    @ApiModelProperty(value = "Question list", hidden = true)
    public List<String> questionList;
    @ApiModelProperty(value = "Answers to the question", hidden = true)
    public List<String> answerList;
    */
    @ApiModelProperty(value = "Q and A", hidden = true)
    public Map<String, List<String>> questionsAndAnswersList;


    public QuizDto(){}

}
