import businesslayer.CategoryEJB;
import businesslayer.QuizEJB;
import datalayer.quiz.Quiz;

import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Question;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.DeleterEJB;

import javax.ejb.EJB;
import java.io.UnsupportedEncodingException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by thang on 25.10.2016.
 */
@RunWith(Arquillian.class)
public class QuizTest123 {
    @Deployment
    public static JavaArchive createTestArchive() throws UnsupportedEncodingException {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage("datalayer.essentials")
                .addPackage("datalayer.categories")
                .addPackage("datalayer.quiz")
                .addPackage("businesslayer")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private QuizEJB quizEJB;

    @EJB
    private CategoryEJB categoryEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Before @After
    public void clean(){
        deleterEJB.deleteAllQuiz();
        deleterEJB.deleteAllCategories();
    }

    @Test
    public void testCreateCategory(){
        Category category = categoryEJB.createCategory("Thangs hobby");
        assertEquals("Thangs hobby", category.getCategoryName());
        assertEquals(1, categoryEJB.getCategoryList().size());

        CategorySub categorySub = categoryEJB.addSubToCategory(category, "Lol");
        assertEquals("Lol", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubName());

        CategorySubSub categorySubSub = categoryEJB.addSubSubToCategorySub(category, categorySub, "Hei");
        assertEquals("Hei", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubSubs().get(0).getCategorySubSubName());

        Quiz quiz = quizEJB.createQuiz(categorySubSub, "Thangs quiz");
        assertEquals("Thangs quiz", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubSubs().get(0).getQuizList().get(0).getQuizName());

        Question question = quizEJB.createQuestion(quiz, "What does the fox say?");
        assertEquals("What does the fox say?", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubSubs().get(0).getQuizList().get(0).getQuestionList().get(0).getQuestion());

        quizEJB.createAnswerToQuestion(question, "JakJakJak", "WoffWoff", "MjauMjau", "KoKo");
        assertEquals("JakJakJak", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubSubs().get(0).getQuizList().get(0).getQuestionList().get(0).getAnswer().getSolutionToAnswer());

        //Create new quiz under category "Hei"
        Quiz quiz2 = quizEJB.createQuiz(categorySubSub, "Thangs quiz second");
        assertEquals("Thangs quiz second", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubSubs().get(0).getQuizList().get(1).getQuizName());

        Question question2 = quizEJB.createQuestion(quiz2, "What does the man say?");
        assertEquals("What does the man say?", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubSubs().get(0).getQuizList().get(1).getQuestionList().get(0).getQuestion());

        quizEJB.createAnswerToQuestion(question2, "JaktJaktJakt", "WoffWoff", "MjauMjau", "KoKo");
        assertEquals("JaktJaktJakt", categoryEJB.getCategoryList().get(0).getCategorySubs().get(0).getCategorySubSubs().get(0).getQuizList().get(1).getQuestionList().get(0).getAnswer().getSolutionToAnswer());
    }
}
