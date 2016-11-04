package datalayer.essentials;

import datalayer.quiz.Quiz;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

/**
 * Created by thang on 25.10.2016.
 */
@Entity
public class Question {
    @Id
    @GeneratedValue
    private Long questionsId;

    @NotBlank
    private String question;
/*
    @ManyToOne
    private Quiz quiz;
    */

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, orphanRemoval = true)
    private Answer answer;







    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/


    public Long getQuestionsId() {
        return questionsId;
    }

    public void setQuestionsId(Long questionsId) {
        this.questionsId = questionsId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
/*
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
*/
    public String toString(){
        return question;
    }
}
