package dto;

import businesslayer.QuizEJB;
import datalayer.essentials.Question;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.ejb.EJB;
import java.util.List;


@ApiModel("A Quiz")
public class QuizDto {
    @ApiModelProperty("The id of the category")
    public String id;
    @ApiModelProperty("The root category id")
    public String rootId;
    @ApiModelProperty("The sub category id")
    public String subCategoryId;
    @ApiModelProperty("The sub sub category id")
    public String subSubCategoryId;
    @ApiModelProperty("The root category")
    public String rootCategory;
    @ApiModelProperty("The sub category")
    public String subCategory;
    @ApiModelProperty("The sub sub category")
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

/*

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CategorySubSub categorySubSub;

    @NotBlank
    private String quizName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<Question> questionList;
 */