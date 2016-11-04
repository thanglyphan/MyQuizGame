package api.implementation;

/**
 * Created by thang on 31.10.2016.
 */

import api.rest.SubSubCategoryRestApi;
import businesslayer.CategoryEJB;
import com.google.common.base.Throwables;
import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import dto.Converter;
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
public class SubSubCategoryRest implements SubSubCategoryRestApi {
    @EJB
    protected CategoryEJB categoryEJB;

    @Override
    public List<SubSubCategoryDto> get() {
        return Converter.transformSubSub(categoryEJB.getCategoryListSubSub());
    }

    @Override
        public Long createSubSubCategory(@ApiParam("Categoryname") SubSubCategoryDto dto) {
            Long id;
            try{
                Category found = categoryEJB.get(Long.parseLong(dto.rootId));
                CategorySub foundSub = categoryEJB.getSub(Long.parseLong(dto.subCategoriId));
                if(!found.isRoot() || !foundSub.isSub()){
                    throw new WebApplicationException("Invalid root id: " , 500);
                }
                id = categoryEJB.addSubSubToCategorySub(found, foundSub, dto.subSubCategory).getId();
            }catch (Exception e){
                throw wrapException(e);
            }
        return id;
    }

    @Override
    public SubSubCategoryDto getById(@ApiParam(ID_PARAM) Long id) {
        return Converter.transform(categoryEJB.getSubSub(id));
    }

    @Override
    public void delete(@ApiParam(ID_PARAM) Long id) {
            categoryEJB.deleteCategory(id);
    }

    @Override
    public List<SubSubCategoryDto> getSubSubCategoriesBySubCategoryId(@ApiParam(ID_PARAM) Long id) {
        List<CategorySubSub> categorySubSubs = new ArrayList<>();
        try{
            CategorySub found = categoryEJB.getSub(id);
            categorySubSubs.addAll(found.getCategorySubSubs());
        }catch (Exception e){
            throw wrapException(e);
        }
        return Converter.transformSubSub(categorySubSubs);
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
