import businesslayer.QuizEJB;
import datalayer.QuizRoot;
import datalayer.QuizSub;
import datalayer.QuizSubSub;
import datalayer.essentials.Question;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

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
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private QuizEJB quizEJB;

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

    }
}
