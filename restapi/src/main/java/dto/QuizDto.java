package dto;

import businesslayer.QuizEJB;
import datalayer.essentials.Question;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.ejb.EJB;
import java.util.List;


@ApiModel("A Quiz")
public class QuizDto {
    @ApiModelProperty(value = "The id of the category", hidden = true)
    public String id;
    @ApiModelProperty("The root category id")
    public String rootId;
    @ApiModelProperty("The sub category id")
    public String subCategoryId;
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
    @ApiModelProperty("The question")
    public String question;
    @ApiModelProperty("Choice one")
    public String choiceOne;
    @ApiModelProperty("Choice two")
    public String choiceTwo;
    @ApiModelProperty("Choice three")
    public String choiceThree;
    @ApiModelProperty("Choice four")
    public String choiceFour;
    @ApiModelProperty("The solution")
    public String solution;
    @ApiModelProperty("List of questions")
    public List<Question> quizQuestions;



    public QuizDto(){}

    public QuizDto(String quizId, String subSubCategoryId, String quizName, String question){
        this.id = quizId;
        this.subSubCategoryId = subSubCategoryId;
        this.quizName = quizName;
        this.question = question;
    }
}
