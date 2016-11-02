package api.implementation;

/**
 * Created by thang on 31.10.2016.
 */

import api.rest.QuizRestApi;
import businesslayer.CategoryEJB;
import businesslayer.QuizEJB;
import com.google.common.base.Throwables;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Question;
import dto.Converter;
import dto.CategoryDto;
import dto.QuizDto;
import io.swagger.annotations.ApiParam;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/*
    The actual implementation could be a EJB, eg if we want to handle
    transactions and dependency injections with @EJB.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class QuizRest implements QuizRestApi {
    private CategorySubSub categorySubSub;
    @EJB
    protected CategoryEJB categoryEJB;

    @EJB
    protected QuizEJB quizEJB;

    @Override
    public List<QuizDto> get() {
        return Converter.transformQuiz(quizEJB.getQuizList());
    }

    @Override
    public Long createQuiz(@ApiParam("Create a quiz") QuizDto dto) {
        Long id;
        try{
            this.categorySubSub = categoryEJB.getSubSub(Long.parseLong(dto.subSubCategoryId));
            if(!categorySubSub.isSubSub() || categorySubSub == null){
                throw new WebApplicationException("Invalid parameters: ", 500);
            }
            id = quizEJB.createQuiz(categorySubSub, dto.quizName).getId();
            Question question = quizEJB.createQuestion(quizEJB.get(id), dto.question);
            quizEJB.createAnswerToQuestion(question, dto.choiceOne, dto.choiceTwo, dto.choiceThree, dto.choiceFour);
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
    }





    //----------------------------------------------------------

    protected WebApplicationException wrapException(Exception e) throws WebApplicationException{

        /*
            Errors:
            4xx: the user has done something wrong, eg asking for something that does not exist (404)
            5xx: internal server error (eg, could be a bug in the code)
         */

        Throwable cause = Throwables.getRootCause(e);
        if(cause instanceof ConstraintViolationException){
            return new WebApplicationException("Invalid constraints on input: "+cause.getMessage(), 400);
        } else {
            return new WebApplicationException("Internal error", 500);
        }
    }
}
