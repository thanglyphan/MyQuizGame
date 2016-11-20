package api.implementation;

import api.rest.RandomQuizApi;
import businesslayer.CategoryEJB;
import businesslayer.QuizEJB;
import com.google.common.base.Throwables;
import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import datalayer.quiz.Quiz;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.InterceptorContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by thang on 17.11.2016.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class RandomQuizRest implements RandomQuizApi {

    @EJB
    protected QuizEJB quizEJB;

    @EJB
    protected CategoryEJB categoryEJB;

    @Override
    public Response getRandomQuiz(String filter) {
        List<Quiz> quizList = getQuizFromFilter(filter);

        if(quizList.size() > 0){
            String id = quizList.get((int) (Math.random() * quizList.size())).getId().toString();
            return Response.status(307)
                    .location(UriBuilder.fromUri("/quizzes/" + id).build()).build();
        }else{
            return Response.status(307)
                    .location(UriBuilder.fromUri("/quizzes/" + "not valid").build()).build();
        }

    }

    @Override
    public List<String> getRandomQuizzes(String y, String x) {
        String firstParam = y;

        if(firstParam.equals("")){
            firstParam = "5";
        }

        List<Quiz> quizList = getQuizFromFilter(x);
        Collections.shuffle(quizList);
        List<String> quizIdList = new ArrayList<>();

        for(Quiz a: quizList){
            if(Integer.parseInt(firstParam) > quizIdList.size()){
                quizIdList.add(a.getId().toString());
            }
        }
        if(quizIdList.size() < Integer.parseInt(firstParam)){
            throw new WebApplicationException("Cannot find enough quizzes on the filter: " + x, 405);
        }

        return quizIdList;
    }
    //---------------------------------------------------HELPER---------------------------------------------------//

    private List<Quiz> getQuizFromFilter(String filter){
        List<Quiz> quizList = new ArrayList<>();

        if (categoryEJB.isPresent(Long.parseLong(filter))) {
            for (Quiz a : quizEJB.getQuizList()) {
                if (a.getCategorySubSub().getCategory().getId().equals(categoryEJB.get(Long.parseLong(filter)).getId())) {
                    quizList.add(a);
                }
            }
        }
        if (categoryEJB.isPresentSub(Long.parseLong(filter))) {
            for (Quiz a : quizEJB.getQuizList()) {
                if (a.getCategorySubSub().getCategorySub().getId().equals(categoryEJB.getSub(Long.parseLong(filter)).getId())) {
                    quizList.add(a);
                }
            }
        }
        if (categoryEJB.isPresentSubSub(Long.parseLong(filter))) {
            for (Quiz a : quizEJB.getQuizList()) {
                if (a.getCategorySubSub().getId().equals(categoryEJB.getSubSub(Long.parseLong(filter)).getId())) {
                    quizList.add(a);
                }
            }
        }
        return quizList;
    }
}
