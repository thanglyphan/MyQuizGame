package datalayer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thang on 24.10.2016.
 */
@Entity
public class QuizSub extends QuizRoot implements Serializable{

    @ManyToOne
    private QuizRoot quizRoot;

    @OneToMany(mappedBy = "quizSub", fetch = FetchType.LAZY, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<QuizSubSub> quizSubSubList;












    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/

    public List<QuizSubSub> getQuizSubSubList() {
        return quizSubSubList;
    }

    public void setQuizSubSubList(List<QuizSubSub> quizSubSubList) {
        this.quizSubSubList = quizSubSubList;
    }


    public QuizRoot getQuizRoot() {
        return quizRoot;
    }

    public void setQuizRoot(QuizRoot quizRoot) {
        this.quizRoot = quizRoot;
    }
}
