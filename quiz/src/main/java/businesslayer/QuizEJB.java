package businesslayer;

import datalayer.QuizRoot;
import datalayer.QuizSub;
import datalayer.QuizSubSub;
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
    public QuizRoot createWholeQuiz(String nameOfQuiz, String rootCategori, String subCategori, String subSubCategori, String question, String... answers){
        QuizRoot quizRoot = createQuiz(nameOfQuiz, rootCategori);
        QuizSub quizSub = createQuizSub(quizRoot, subCategori);
        QuizSubSub quizSubSub = createQuizSubSub(quizRoot, quizSub, subSubCategori);
        Question q = createQuestion(quizSubSub, question);
        createAnswerToQuestion(q, answers[0],answers[1],answers[2],answers[3]);

        return quizRoot;
    }

    public QuizRoot createQuiz(String nameOfQuiz, String categori){
        QuizRoot quizRoot = new QuizRoot();
        quizRoot.setRoot(true);
        quizRoot.setName(nameOfQuiz);
        quizRoot.setCategori(categori);
        quizRoot.setQuizSubs(new ArrayList<>());
        persistInATransaction(quizRoot);
        return quizRoot;
    }

    public QuizSub createQuizSub(QuizRoot quizRoot, String categori){
        QuizSub quizSub = new QuizSub();
        quizSub.setRoot(false);
        quizSub.setQuizRoot(quizRoot);
        quizSub.setCategori(categori);
        quizSub.setName(quizRoot.getName());
        quizSub.setQuizSubSubList(new ArrayList<>());
        addSubQuiz(quizRoot, quizSub);
        return quizSub;
    }

    public QuizSubSub createQuizSubSub(QuizRoot quizRoot, QuizSub quizSub, String categori){
        QuizSubSub quizSubSub = new QuizSubSub();
        quizSubSub.setName(quizRoot.getName());
        quizSubSub.setRoot(false);
        quizSubSub.setCategori(categori);
        quizSubSub.setQuizRoot(quizRoot);
        quizSubSub.setQuizSub(quizSub);
        quizSubSub.setQuestionList(new ArrayList<>());

        persistInATransaction(quizSubSub);
        return quizSubSub;
    }

    public Question createQuestion(QuizSubSub quizSubSub, String q){
        Question question = new Question();
        question.setQuestion(q);
        question.setQuizSubSub(quizSubSub);
        persistInATransaction(question);

        quizSubSub.getQuestionList().add(question);
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

    private void addSubQuiz(QuizRoot quizRoot, QuizSub quizSub){
        persistInATransaction(quizSub);

        quizRoot.getQuizSubs().add(quizSub);
        em.merge(quizRoot);
    }









    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/

    public QuizRoot getQuizRoot(Long id){
        return em.find(QuizRoot.class, id);
    }
    public List<QuizRoot> getQuizList(){
        return em.createNamedQuery(QuizRoot.FIND_ALL).getResultList();
    }
}
