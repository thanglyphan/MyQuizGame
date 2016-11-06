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
import datalayer.essentials.Question;
import datalayer.quiz.Quiz;
import dto.Converter;
import dto.CategoryDto;
import dto.SubCategoryDto;
import dto.SubSubCategoryDto;
import io.swagger.annotations.ApiParam;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
    The actual implementation could be a EJB, eg if we want to handle
    transactions and dependency injections with @EJB.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class CategoryRest implements CategoryRestApi {
    private Category category;

    @EJB
    protected CategoryEJB categoryEJB;

    @EJB
    protected QuizEJB quizEJB;

    @Override
    public List<CategoryDto> get() {
/*
        Category a = categoryEJB.createCategory("Thang");
        CategorySub b = categoryEJB.addSubToCategory(a, "Hobbies");
        CategorySubSub c = categoryEJB.addSubSubToCategorySub(a, b, "Bodybuilding");
        Quiz d = quizEJB.createQuiz(c, "The fitness quiz");

        Question e = quizEJB.createQuestion(d, "How much does Thang bench?");
        quizEJB.createAnswerToQuestion(e, "120kg", "110kg", "100kg", "90kg");
*/
        return Converter.transform(categoryEJB.getCategoryList());
    }

    @Override
    public Long createCategory(@ApiParam("Categoryname") CategoryDto dto) {
        Long id;
        try{
            this.category = categoryEJB.createCategory(dto.rootCategory);
            id = category.getId();
        }catch (Exception e){
            throw wrapException(e);
        }

        return id;
    }

    @Override
    public CategoryDto getById(Long id) {
        return Converter.transform(categoryEJB.get(id));
    }

    @Override
    public void delete(@ApiParam(ID_PARAM) Long id) {
        categoryEJB.deleteCategory(id);
    }

    @Override
    public void update(Long pathId, CategoryDto dto) {
        Long id;
        try{
            id = Long.parseLong(dto.id);
        } catch (Exception e){
            throw new WebApplicationException("Invalid id: " + dto.id, 400);
        }

        if(!id.equals(pathId)){
            // in this case, 409 (Conflict) sounds more appropriate than the generic 400
            throw new WebApplicationException("Not allowed to change the id of the resource", 409);
        }

        if(! categoryEJB.isPresent(id) || !categoryEJB.get(id).isRoot()){
            throw new WebApplicationException("Not allowed to create a news with PUT, and cannot find news with id: "+id, 404);
        }

        try {
            categoryEJB.update(id, dto.rootCategory);
        } catch (Exception e){
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
        Converter.transform(category);
    }


    @Override
    public List<CategoryDto> getCategoriesWithQuiz() {
        List<Category> list = new ArrayList<>();
        for(Quiz a: quizEJB.getQuizList()){
            list.add(a.getCategorySubSub().getCategory());
        }
        return Converter.transform(list);
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategoriesWithQuiz() {
        List<CategorySubSub> list = new ArrayList<>();
        for(Quiz a: quizEJB.getQuizList()){
            list.add(a.getCategorySubSub());
        }
        return Converter.transformSubSub(list);
    }

    @Override
    public List<SubCategoryDto> getSubCategoriesByParentId(@ApiParam(ID_PARAM) Long id) {
        List<CategorySub> list = new ArrayList<>();
        try{
            Category found = categoryEJB.get(id);
            for(CategorySub a: found.getCategorySubs()){
                list.add(a);
            }
        }catch (Exception e){
            throw wrapException(e);
        }

        return Converter.transformSub(list);
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