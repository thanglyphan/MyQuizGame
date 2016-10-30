package util;
import businesslayer.QuizEJB;
import datalayer.QuizRoot;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by thang on 18.10.2016.
 */
@Stateless
public class DeleterEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private QuizEJB quizEJB;

    public void deleteQuiz(Long quizId){
        QuizRoot quiz = em.find(QuizRoot.class, quizId);
        em.remove(quiz);
    }

    public int deleteAllQuiz(){
        int i = 0;
        for(QuizRoot a: quizEJB.getQuizList()){
            i++;
            deleteQuiz(a.getQuizId());
        }
        return i;
    }

}
