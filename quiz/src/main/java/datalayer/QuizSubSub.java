package datalayer;

import datalayer.essentials.Question;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thang on 24.10.2016.
 */
@Entity
public class QuizSubSub extends QuizRoot implements Serializable {

    @OneToOne
    private QuizRoot quizRoot;

    @ManyToOne
    private QuizSub quizSub;

    @OneToMany(mappedBy = "quizSubSub", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<Question> questionList;











    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/

    public QuizRoot getQuizRoot() {
        return quizRoot;
    }

    public void setQuizRoot(QuizRoot quizRoot) {
        this.quizRoot = quizRoot;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public QuizSub getQuizSub() {
        return quizSub;
    }

    public void setQuizSub(QuizSub quizSub) {
        this.quizSub = quizSub;
    }
}
