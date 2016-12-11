package api.implementation;

/**
 * Created by thang on 31.10.2016.
 */

import api.rest.QuestionAnswersRestApi;
import businesslayer.CategoryEJB;
import businesslayer.QuizEJB;
import com.google.common.base.Throwables;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Question;
import datalayer.quiz.Quiz;
import dto.QuestionDto;
import io.swagger.annotations.ApiParam;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    The actual implementation could be a EJB, eg if we want to handle
    transactions and dependency injections with @EJB.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class QuestionAnswersRest implements QuestionAnswersRestApi {
    private CategorySubSub categorySubSub;
    @EJB
    protected CategoryEJB categoryEJB;

    @EJB
    protected QuizEJB quizEJB;

    @Override
    public Long createQuestion(@ApiParam("Create a question") QuestionDto dto) {
        Long id;
        try {
            Quiz found = quizEJB.get(Long.parseLong(dto.quizId));
            if (found == null) {
                throw new WebApplicationException("Invalid parameters: ", 500);
            }
            Question newQ = quizEJB.createQuestion(found, dto.question);
            id = newQ.getQuestionsId();
            quizEJB.createAnswerToQuestion(newQ, dto.choiceOne, dto.choiceTwo, dto.choiceThree, dto.choiceFour);
        } catch (Exception e) {
            throw wrapException(e);
        }
        return id;
    }

    @Override
    public Response getAnswer(Long id, String incomeQuestion) {
        String question = incomeQuestion;
        String answer = "";

        Quiz found = quizEJB.get(id);
        if (found == null) {
            throw new WebApplicationException("Invalid parameters: ", 500);
        }
        for (Question a : found.getQuestionList()) {
            if (a.getQuestion().toLowerCase().contains(question.toLowerCase())) {
                answer = a.getAnswer().getSolutionToAnswer();
            }
        }
        if (answer.equals("")) {
            throw new WebApplicationException("Invalid question: ", 405);
        }

        return Response.ok(answer).build();
    }

    @Override
    public Response getQuestion(Long id) {
        HashMap<String, List<String>> listQuestionsMap = new HashMap<>();
        List<String> answerList = new ArrayList<>();
        Question found;
        try{
            found = quizEJB.getQuestion(id);
        }catch (Exception e){
            throw new WebApplicationException("Invalid question ID: "+ id, 404);
        }
        answerList.add(found.getAnswer().getChoiceOne());
        answerList.add(found.getAnswer().getChoiceTwo());
        answerList.add(found.getAnswer().getChoiceThree());
        answerList.add(found.getAnswer().getChoiceFour());

        listQuestionsMap.put(found.getQuestion(), answerList);
        return Response.ok(listQuestionsMap).build();
    }

    //----------------------------------------------------------

    protected WebApplicationException wrapException(Exception e) throws WebApplicationException {

        /*
            Errors:
            4xx: the user has done something wrong, eg asking for something that does not exist (404)
            5xx: internal server error (eg, could be a bug in the code)
         */

        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof ConstraintViolationException) {
            return new WebApplicationException("Invalid constraints on input: " + cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
