package util;
import businesslayer.CategoryEJB;
import businesslayer.QuizEJB;
import datalayer.categories.Category;
import datalayer.quiz.Quiz;

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

    @EJB
    private CategoryEJB categoryEJB;

    public void deleteQuiz(Long quizId){
        Quiz quiz = em.find(Quiz.class, quizId);
        em.remove(quiz);
    }

    public int deleteAllQuiz(){
        int i = 0;
        for(Quiz a: quizEJB.getQuizList()){
            i++;
            deleteQuiz(a.getId());
        }
        return i;
    }
    public void deleteCategory(Long categoryId){
        Category category = em.find(Category.class, categoryId);
        em.remove(category);
    }
    public int deleteAllCategories(){
        int i = 0;
        for(Category a: categoryEJB.getCategoryList()){
            i++;
            deleteCategory(a.getId());
        }
        return i;
    }

}
