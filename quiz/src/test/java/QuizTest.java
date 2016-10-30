import businesslayer.QuizEJB;
import datalayer.QuizRoot;
import datalayer.QuizSub;
import datalayer.QuizSubSub;
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
public class QuizTest {
    @Deployment
    public static JavaArchive createTestArchive() throws UnsupportedEncodingException {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackage("datalayer")
                .addPackage("datalayer.essentials")
                .addPackage("businesslayer")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private QuizEJB quizEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Before @After
    public void clean(){
        deleterEJB.deleteAllQuiz();
    }

    @Test
    public void testCreateQuiz(){
        QuizRoot quizRoot = quizEJB.createQuiz("My first quiz", "Science");
        assertEquals("Science", quizEJB.getQuizList().get(0).getCategori());

        QuizSub quizSub = quizEJB.createQuizSub(quizRoot, "Computer Science");
        assertEquals("Computer Science", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getCategori());

        QuizSubSub quizSubSub = quizEJB.createQuizSubSub(quizRoot, quizSub, "Network");
        assertEquals("Network", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getQuizSubSubList().get(0).getCategori());

        Question question = quizEJB.createQuestion(quizSubSub, "Who is beast?");

        assertEquals("Who is beast?", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getQuizSubSubList().get(0).getQuestionList().get(0).getQuestion());

        quizEJB.createAnswerToQuestion(question, "Thang", "Pupp", "Pikk", "Fitte");
        assertEquals("Thang", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getQuizSubSubList().get(0).getQuestionList().get(0).getAnswer().getSolutionToAnswer());

        //Lets make another question to the "Network"-quiz.
        Question question2 = quizEJB.createQuestion(quizSubSub, "How much does Thang bench?");

        assertEquals("How much does Thang bench?", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getQuizSubSubList().get(0).getQuestionList().get(1).getQuestion());

        quizEJB.createAnswerToQuestion(question2, "120kg", "110kg", "100kg", "90kg");
        assertEquals("120kg", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getQuizSubSubList().get(0).getQuestionList().get(1).getAnswer().getSolutionToAnswer());

        assertEquals(1, quizEJB.getQuizList().size());
    }

    @Test
    public void testOne(){
        assertEquals(0, quizEJB.getQuizList().size());
    }

    @Test
    public void testMakeWholeQuiz(){
        String a = "120kg";
        String b = "100kg";
        String c = "110kg";
        String d = "90kg";
        QuizRoot quizRoot = quizEJB.createWholeQuiz("Thangs quiz", "Thangs hobby", "Bodybuilding", "PR's", "How much weight does Thang bench", a, b, c, d);

        assertEquals("Thangs quiz", quizEJB.getQuizList().get(0).getName());
        assertEquals("Thangs hobby", quizEJB.getQuizList().get(0).getCategori());
        assertEquals("Bodybuilding", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getCategori());
        assertEquals("PR's", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getQuizSubSubList().get(0).getCategori());
        assertEquals("How much weight does Thang bench", quizEJB.getQuizList().get(0).getQuizSubs().get(0).getQuizSubSubList().get(0).getQuestionList().get(0).getQuestion());

        QuizSub quizSub = quizEJB.createQuizSub(quizRoot, "People");
        QuizSubSub quizSubSub = quizEJB.createQuizSubSub(quizRoot, quizSub, "Norwegians");
        Question question = quizEJB.createQuestion(quizSubSub, "Why are norwegian girls so fine?");
        quizEJB.createAnswerToQuestion(question, "They are vikings", "They eat healthy", "They watch movies", "They workout");

        assertEquals("They are vikings", quizEJB.getQuizList().get(0).getQuizSubs().get(1).getQuizSubSubList().get(0).getQuestionList().get(0).getAnswer().getSolutionToAnswer());
    }
}
