package api.implementation;

/**
 * Created by thang on 31.10.2016.
 */

import api.rest.CategoryRestApi;
import businesslayer.CategoryEJB;
import businesslayer.QuizEJB;
import com.google.common.base.Throwables;
import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Answer;
import datalayer.essentials.Question;
import datalayer.quiz.Quiz;
import dto.Converter;
import dto.CategoryDto;
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

import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class CategoryRest implements CategoryRestApi {
    private Category category;
    @Context
    UriInfo uriInfo;

    @EJB
    protected CategoryEJB categoryEJB;

    @EJB
    protected QuizEJB quizEJB;

    @Override
    public ListDto<CategoryDto> get(
            @ApiParam("Offset in the list of news") @DefaultValue("0") Integer offset,
            @ApiParam("Limit of news in a single retrieved page") @DefaultValue("10") Integer limit,
            @ApiParam("Whether to retrieve or not votes and comments for the given news") @DefaultValue("false") boolean expand) {

        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        List<Category> categoryList;
        int maxResults = 50;

        categoryList = categoryEJB.getWholePackage(expand, maxResults);



        if(offset != 0 && offset >=  categoryList.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+categoryList.size(), 400);
        }

        ListDto<CategoryDto> dto = Converter.transformCollection(
                categoryList, offset, limit, expand);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/categories")
                .queryParam("limit", limit)
                .queryParam("expand", expand);

        dto._links.self = new HalLink(builder.clone()
                .queryParam("offset", offset)
                .build().toString()
        );

        if (!categoryList.isEmpty() && offset > 0) {
            dto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            );
        }
        if (offset + limit < categoryList.size()) {
            dto._links.next = new HalLink(builder.clone()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            );
        }
        return dto;
    }


    @Override
    public Long createCategory(@ApiParam("Categoryname") CategoryDto dto) {
        Long id;
        try {
            this.category = categoryEJB.createCategory(dto.rootCategory);
            id = category.getId();
        } catch (Exception e) {
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public List<CategoryDto> getCategoriesWithQuiz(boolean withQuizzes) {
        List<Category> list = new ArrayList<>();
        if(withQuizzes){
            for (Quiz a : quizEJB.getQuizList()) {
                list.add(a.getCategorySubSub().getCategory());
            }
            return Converter.transform(list, false);
        }
        return Converter.transform(list, false);
    }

    @Override
    public CategoryDto getById(
            @ApiParam(ID_PARAM) Long id,
            @ApiParam("Whether to retrieve or not votes and comments for the given news") @DefaultValue("false") boolean expand) {
        Category category = categoryEJB.get(id);
        if(category.isRoot()){
            return Converter.transform(categoryEJB.get(id), expand);
        }else{
            throw new WebApplicationException("Not found, not root category: " + id, 404);
        }
    }

    @Override
    public void delete(@ApiParam(ID_PARAM) Long id) {
        categoryEJB.deleteCategory(id);
    }

    @Override
    public void update(Long pathId, CategoryDto dto) {
        Long id;
        try {
            id = Long.parseLong(dto.id);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid id: " + dto.id, 400);
        }

        if (!id.equals(pathId)) {
            // in this case, 409 (Conflict) sounds more appropriate than the generic 400
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if (!categoryEJB.isPresent(id) || !categoryEJB.get(id).isRoot()) {
            throw new WebApplicationException("Not allowed to create a news with PUT, and cannot find news with id: " + id, 404);
        }

        try {
            categoryEJB.update(id, dto.rootCategory);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    @Override
    public void patch(@ApiParam("The unique id of the counter") Long id, @ApiParam("Change root category") String text) {
        Category category = categoryEJB.get(id);
        if (category == null || !category.isRoot()) {
            throw new WebApplicationException("Cannot find counter with id " + id, 404);
        }
        String rootCategory;
        try {
            rootCategory = text;
        } catch (NumberFormatException e) {
            throw new WebApplicationException("Invalid instructions. Should contain just a number: \"" + text + "\"");
        }
        categoryEJB.updatePatch(id, rootCategory);
        Converter.transform(category, false);
    }

    /*
    @Override
    public List<SubSubCategoryDto> getSubSubCategoriesWithQuiz() {
        List<CategorySubSub> list = new ArrayList<>();
        for (Quiz a : quizEJB.getQuizList()) {
            list.add(a.getCategorySubSub());
        }
        return Converter.transformSubSub(list);
    }
    */

    @Override
    public List<SubCategoryDto> getSubCategoriesByParentId(@ApiParam(ID_PARAM) Long id) {
        List<CategorySub> list = new ArrayList<>();
        try {
            Category found = categoryEJB.get(id);
            for (CategorySub a : found.getCategorySubs()) {
                list.add(a);
            }
        } catch (Exception e) {
            throw wrapException(e);
        }

        return Converter.transformSub(list);
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

    //------------------------------------------------ DEPRECATED ------------------------------------------------//
    @Override
    public Response deprecatedGetById(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("categories/", 301, id, "");
    }

    @Override
    public Response deprecatedUpdate(@ApiParam(ID_PARAM) Long id, @ApiParam("The category that will replace the old one. Cannot change its id though.") CategoryDto dto) {
        return Responder.response("categories/", 301, id, "");

    }

    @Override
    public Response deprecatedPatch(@ApiParam("The unique id of the counter") Long id, @ApiParam("Change root category") String text) {
        Category category = categoryEJB.createCategory("The category");
        CategorySub categorySub = categoryEJB.addSubToCategory(category, "The subcategory");
        CategorySubSub categorySubSub = categoryEJB.addSubSubToCategorySub(category, categorySub, "The subsubcategory");

        Quiz quiz = quizEJB.createQuiz(categorySubSub, "The quiz");
        Question question = quizEJB.createQuestion(quiz, "How much coffee does Thang drink?");
        quizEJB.createAnswerToQuestion(question, "One cup per day", "Two cups per day", "Three cups per day", "None");
        return Responder.response("categories/", 301, id, "");

    }

    @Override
    public Response deprecatedDelete(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("categories/", 301, id, "");

    }

    @Override
    public Response deprecatedGetSubCategoriesByParentId(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("categories/", 301, id, "/subcategories");

    }

    @Override
    public Response deprecatedGetCategoriesWithQuiz() {
        return Response.status(307)
                .location(UriBuilder.fromUri("categories/").queryParam("withQuizzes", true).build())
                .build();
    }

    @Override
    public Response deprecatedGetSubSubCategoriesWithQuiz() {
        return Response.status(307)
                .location(UriBuilder.fromUri("/subsubcategories/").queryParam("withQuizzes", true).build())
                .build();
    }
}
