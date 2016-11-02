package datalayer.quiz;

import datalayer.categories.CategorySubSub;
import datalayer.essentials.Question;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thang on 01.11.2016.
 */
@Entity
@NamedQueries(value = {
        @NamedQuery(name = Quiz.FIND_ALL, query = "SELECT a FROM Quiz a")
})
public class Quiz implements Serializable {

    public static final String FIND_ALL = "Quiz.find_all";

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private CategorySubSub categorySubSub;

    @NotBlank
    private String quizName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<Question> questionList;

















    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/


    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public CategorySubSub getCategorySubSub() {
        return categorySubSub;
    }

    public void setCategorySubSub(CategorySubSub categorySubSub) {
        this.categorySubSub = categorySubSub;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }


}

