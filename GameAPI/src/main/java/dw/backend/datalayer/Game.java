package dw.backend.datalayer;

import datalayer.categories.Category;
import datalayer.quiz.Quiz;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thang on 01.11.2016.
 */
@Entity
public class Game implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String gameName;

    @NotBlank
    private int quizCount;

    @NotBlank
    @ElementCollection(targetClass=String.class)
    private List<String> quizIdList;

    @NotBlank
    @ElementCollection(targetClass=QuizObject.class)
    private List<QuizObject> quizList;
/*
    @ElementCollection(targetClass = Quiz.class)
    private List<Quiz> quizObjectList;
*/
















    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
/*
    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }
*/
    public int getQuizCount() {
        return quizCount;
    }

    public void setQuizCount(int quizCount) {
        this.quizCount = quizCount;
    }

    public List<QuizObject> getQuizList() {
        return quizList;
    }

    public void setQuizList(List<QuizObject> quizList) {
        this.quizList = quizList;
    }
/*
    public List<Quiz> getQuizObjectList() {
        return quizObjectList;
    }

    public void setQuizObjectList(List<Quiz> quizObjectList) {
        this.quizObjectList = quizObjectList;
    }
*/
    public List<String> getQuizIdList() {
        return quizIdList;
    }

    public void setQuizIdList(List<String> quizIdList) {
        this.quizIdList = quizIdList;
    }
}

