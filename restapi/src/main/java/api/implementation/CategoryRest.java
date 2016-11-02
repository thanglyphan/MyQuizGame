package api.implementation;

/**
 * Created by thang on 31.10.2016.
 */

import api.rest.CategoryRestApi;
import businesslayer.CategoryEJB;
import com.google.common.base.Throwables;
import datalayer.categories.Category;
import dto.Converter;
import dto.CategoryDto;
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
public class CategoryRest implements CategoryRestApi {
    private Category category;
    @EJB
    protected CategoryEJB categoryEJB;



    @Override
    public List<CategoryDto> get() {
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
