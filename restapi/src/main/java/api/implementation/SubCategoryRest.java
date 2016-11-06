package api.implementation;

/**
 * Created by thang on 31.10.2016.
 */

import api.rest.SubCategoryRestApi;
import businesslayer.CategoryEJB;
import com.google.common.base.Throwables;
import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import dto.Converter;
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

/*
    The actual implementation could be a EJB, eg if we want to handle
    transactions and dependency injections with @EJB.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) //avoid creating new transactions
public class SubCategoryRest implements SubCategoryRestApi {
    @EJB
    protected CategoryEJB categoryEJB;


    @Override
    public List<SubCategoryDto> get() {
        return Converter.transformSub(categoryEJB.getCategoryListSub());
    }

    @Override
    public Long createSubCategory(@ApiParam("Categoryname") SubCategoryDto dto) {
        Long id;
        try{
            Category found = categoryEJB.get(Long.parseLong(dto.rootId));
            if(!found.isRoot()){
                throw new WebApplicationException("Invalid root id: " , 400);
            }
            id = categoryEJB.addSubToCategory(found, dto.subCategory).getId();
        }catch (Exception e){
            throw wrapException(e);
        }
        return id;
    }

    @Override
    public SubCategoryDto getById(@ApiParam(ID_PARAM) Long id) {
        return Converter.transform(categoryEJB.getSub(id));
    }

    @Override
    public void delete(@ApiParam(ID_PARAM) Long id) {
            categoryEJB.deleteCategory(id);
    }

    @Override
    public List<SubCategoryDto> getSubcategoriesByParentId(@ApiParam(ID_PARAM) Long id) {
        List<CategorySub> categorySubs;
        try{
            Category found = categoryEJB.get(id);
            categorySubs = found.getCategorySubs();
        }catch (Exception e){
            throw wrapException(e);
        }
        return Converter.transformSub(categorySubs);
    }

    @Override
    public List<SubSubCategoryDto> getSubSubcategoriesById(@ApiParam(ID_PARAM) Long id) {
        List<CategorySubSub> categorySubSubs;
        try{
            CategorySub found = categoryEJB.getSub(id);
            categorySubSubs = found.getCategorySubSubs();
        }catch (Exception e){
            throw wrapException(e);
        }
        return Converter.transformSubSub(categorySubSubs);
    }

    @Override
    public void update(Long pathId, SubCategoryDto dto) {
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

        if(! categoryEJB.isPresentSub(id) || !categoryEJB.getSub(id).isSub()){
            throw new WebApplicationException("Not allowed to create a news with PUT, and cannot find news with id: "+id, 404);
        }

        try {
            categoryEJB.updateSub(id, Long.parseLong(dto.rootId), dto.subCategory);
        } catch (Exception e){
            throw wrapException(e);
        }
    }


    @Override
    public void patch(@ApiParam("The unique id of the counter") Long id, @ApiParam("Change sub category") String text) {
        CategorySub categorySub = categoryEJB.getSub(id);
        if (categorySub == null || !categorySub.isSub()) {
            throw new WebApplicationException("Cannot find counter with id " + id, 404);
        }
        String subCategory;
        try {
            subCategory = text;
        } catch (NumberFormatException e) {
            throw new WebApplicationException("Invalid instructions. Should contain just a number: \"" + text + "\"");
        }
        categoryEJB.updatePatchSub(id, subCategory);
        Converter.transform(categorySub);
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
