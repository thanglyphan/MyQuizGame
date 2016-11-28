package api.implementation;

/**
 * Created by thang on 31.10.2016.
 */

import api.rest.QuizRestApi;
import businesslayer.CategoryEJB;
import businesslayer.QuizEJB;
import com.google.common.base.Throwables;
import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Question;
import datalayer.quiz.Quiz;
import dto.Converter;
import dto.QuizDto;
import dto.SubCategoryDto;
import dto.collection.ListDto;
import dto.hal.HalLink;
import io.swagger.annotations.ApiParam;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/*
    The actual implementation could be a EJB, eg if we want to handle
    transactions and dependency injections with @EJB.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class QuizRest implements QuizRestApi {
    @Context
    UriInfo uriInfo;
    private CategorySubSub categorySubSub;
    @EJB
    protected CategoryEJB categoryEJB;

    @EJB
    protected QuizEJB quizEJB;

    @Override
    public ListDto<QuizDto> get(
            @ApiParam("Offset in the list of news") @DefaultValue("0") Integer offset,
            @ApiParam("Limit of news in a single retrieved page") @DefaultValue("10") Integer limit)
    {
        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        List<Quiz> quizList;
        int maxResults = 50;

        quizList = quizEJB.getQuizListWithMaxLimit(maxResults);



        if(offset != 0 && offset >=  quizList.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+quizList.size(), 400);
        }

        ListDto<QuizDto> dto = Converter.transformCollectionQuiz(
                quizList, offset, limit);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/quizzes")
                .queryParam("limit", limit);

        dto._links.self = new HalLink(builder.clone()
                .queryParam("offset", offset)
                .build().toString()
        );

        if (!quizList.isEmpty() && offset > 0) {
            dto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            );
        }
        if (offset + limit < quizList.size()) {
            dto._links.next = new HalLink(builder.clone()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            );
        }
        return dto;
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
            /*
            Question question = quizEJB.createQuestion(quizEJB.get(id), dto.question);
            quizEJB.createAnswerToQuestion(question, dto.choiceOne, dto.choiceTwo, dto.choiceThree, dto.choiceFour);
            */
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public QuizDto getById(@ApiParam(ID_PARAM) Long id) {
        return Converter.transform(quizEJB.get(id));
    }

    @Override
    public void delete(@ApiParam(ID_PARAM) Long id) {
        quizEJB.deleteQuiz(id);
    }

    @Override
    public void update(Long pathId, QuizDto dto) {
        Long id;
        try{
            id = Long.parseLong(dto.id);
        } catch (Exception e){
            throw new WebApplicationException("Invalid id: " + pathId, 400);
        }
        if(!id.equals(pathId)){
            // in this case, 409 (Conflict) sounds more appropriate than the generic 400
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }
        if(! quizEJB.isPresent(id)){
            throw new WebApplicationException("Not allowed to create a news with PUT, and cannot find news with id: "+id, 404);
        }

        try {
            quizEJB.update(id, Long.parseLong(dto.subSubCategoryId), dto.quizName);
        } catch (Exception e){
            throw wrapException(e);
        }
    }

    @Override
    public void patch(@ApiParam("The unique id of the quiz") Long id, @ApiParam("Change quiz name") String text) {
        Quiz quiz = quizEJB.get(id);
        if (quiz == null) {
            throw new WebApplicationException("Cannot find counter with id " + id, 404);
        }
        String quizName;
        try {
            quizName = text;
        } catch (NumberFormatException e) {
            throw new WebApplicationException("Invalid instructions. Should contain just a number: \"" + text + "\"");
        }
        quizEJB.updatePatch(id, quizName);
        Converter.transform(quiz);
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

    //------------------------------------------------ DEPRECATED ------------------------------------------------//

    @Override
    public Response deprecatedGetById(@ApiParam(ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("quizzes/" + id).build())
                .build();
    }

    @Override
    public Response deprecatedDelete(@ApiParam(ID_PARAM) Long id) {
        return Response.status(301)
                .location(UriBuilder.fromUri("quizzes/" + id).build())
                .build();    }

    @Override
    public Response deprecatedUpdate(@ApiParam(ID_PARAM) Long id, @ApiParam("The sub sub category that will replace the old one. Cannot change its id though.") QuizDto dto) {
        return Response.status(301)
                .location(UriBuilder.fromUri("quizzes/" + id).build())
                .build();    }

    @Override
    public Response deprecatedPatch(@ApiParam("The unique id of the quiz") Long id, @ApiParam("Change quiz name") String text) {
        return Response.status(301)
                .location(UriBuilder.fromUri("quizzes/" + id).build())
                .build();
    }
}
