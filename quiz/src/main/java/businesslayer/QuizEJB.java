package businesslayer;

import datalayer.quiz.Quiz;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Answer;
import datalayer.essentials.Question;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by thang on 25.10.2016.
 */

@Stateless
public class QuizEJB implements Serializable{

    @PersistenceContext
    EntityManager em;

    public QuizEJB(){}

    private void persistInATransaction(Object... obj) {
        for(Object o : obj) {
            em.persist(o);
        }
    }
    public Quiz createQuiz(CategorySubSub categorySubSub, String quizName){
        Quiz quiz = new Quiz();
        quiz.setQuizName(quizName);
        quiz.setCategorySubSub(categorySubSub);
        quiz.setQuestionList(new ArrayList<>());
        persistInATransaction(quiz);
        categorySubSub.getQuizList().add(quiz);

        return quiz;
    }

    public Question createQuestion(Quiz quiz, String q){
        Question question = new Question();
        question.setQuestion(q);
        question.setQuiz(quiz);
        persistInATransaction(question);

        quiz.getQuestionList().add(question);
        em.merge(quiz);
        return question;
    }

    public void createAnswerToQuestion(Question question, String choiceOne, String choiseTwo, String choiceThree, String choiceFour){
        Answer answer = new Answer();
        answer.setChoiceOne(choiceOne);
        answer.setChoiceTwo(choiseTwo);
        answer.setChoiceThree(choiceThree);
        answer.setChoiceFour(choiceFour);
        answer.setQuestion(question);
        answer.setSolutionToAnswer(choiceOne);
        persistInATransaction(answer);

        question.setAnswer(answer);
        em.merge(question);
    }








    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/

    public List<Quiz> getQuizList(){
        return em.createNamedQuery(Quiz.FIND_ALL).getResultList();
    }
    public Quiz get(Long id){
        return em.find(Quiz.class, id);
    }
}
