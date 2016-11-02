package datalayer.categories;

import datalayer.quiz.Quiz;
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
public class CategorySubSub extends CategorySub implements Serializable{
    @ManyToOne
    private CategorySub categorySub;

    @NotBlank
    private String categorySubSubName;


    @OneToMany(mappedBy = "categorySubSub", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<Quiz> quizList;















    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/

    public CategorySub getCategorySub() {
        return categorySub;
    }

    public void setCategorySub(CategorySub categorySub) {
        this.categorySub = categorySub;
    }

    public String getCategorySubSubName() {
        return categorySubSubName;
    }

    public void setCategorySubSubName(String categorySubSubName) {
        this.categorySubSubName = categorySubSubName;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }
}
