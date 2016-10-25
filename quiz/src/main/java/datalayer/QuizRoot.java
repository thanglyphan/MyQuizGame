package datalayer;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thang on 24.10.2016.
 */
@Entity
@NamedQueries(value = {
        @NamedQuery(name = QuizRoot.FIND_ALL, query = "SELECT a FROM QuizRoot a")
})
public class QuizRoot implements Serializable{

    public static final String FIND_ALL = "QuizRoot.find_all";

    @Id @GeneratedValue
    private Long quizId;

    @NotBlank
    private String name;

    @NotBlank
    private String categori;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<QuizSub> quizSubs; //Choose between categories








    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuizSub> getQuizSubs() {
        return quizSubs;
    }

    public void setQuizSubs(List<QuizSub> quizSubs) {
        this.quizSubs = quizSubs;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getCategori() {
        return categori;
    }

    public void setCategori(String categori) {
        this.categori = categori;
    }
}
