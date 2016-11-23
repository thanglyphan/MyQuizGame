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
import javax.ws.rs.core.Response;
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
    public List<SubSubCategoryDto> get() {return Converter.transformSubSub(categoryEJB.getCategoryListSubSub());}
    @Override
    public List<SubSubCategoryDto> getWithQuizzes(boolean withQuizzes) {
        if(withQuizzes){
            return Converter.transformSubSub(categoryEJB.getCategoryListSubSubWithQuizzes());
        }
        return new ArrayList<>();
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
/*
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
*/
    @Override
    public void update(Long pathId, SubSubCategoryDto dto) {
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
        if(! categoryEJB.isPresentSubSub(id) || !categoryEJB.getSubSub(id).isSubSub()){
            throw new WebApplicationException("Not allowed to create a news with PUT, and cannot find news with id: "+id, 404);
        }
        if( categoryEJB.get(Long.parseLong(dto.rootId)) == null || categoryEJB.getSub(Long.parseLong(dto.subCategoriId)) == null){
            throw new WebApplicationException("Invalid id", 404);
        }

        try {
            categoryEJB.updateSubSub(id, Long.parseLong(dto.rootId), Long.parseLong(dto.subCategoriId), dto.subSubCategory);
        } catch (Exception e){
            throw wrapException(e);
        }
    }

    @Override
    public void patch(@ApiParam("The unique id of the counter") Long id, @ApiParam("Change sub sub category") String text) {
        CategorySubSub categorySubSub = categoryEJB.getSubSub(id);
        if (categorySubSub == null || !categorySubSub.isSubSub()) {
            throw new WebApplicationException("Cannot find counter with id " + id, 404);
        }
        String subCategory;
        try {
            subCategory = text;
        } catch (NumberFormatException e) {
            throw new WebApplicationException("Invalid instructions. Should contain just a number: \"" + text + "\"");
        }
        categoryEJB.updatePatchSubSub(id, subCategory);
        Converter.transform(categorySubSub);
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
        return Responder.response("subsubcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedDelete(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("subsubcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedUpdate(@ApiParam(ID_PARAM) Long id, @ApiParam("The sub sub category that will replace the old one. Cannot change its id though.") SubSubCategoryDto dto) {
        return Responder.response("subsubcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedPatch(@ApiParam("The unique id of the counter") Long id, @ApiParam("Change sub sub category") String text) {
        return Responder.response("subsubcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedGetSubSubCategoriesBySubCategoryId(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("/subcategories/", 301, id, "/subsubcategories");
    }
}
