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
import dto.CategoryDto;
import dto.Converter;
import dto.SubCategoryDto;
import dto.SubSubCategoryDto;
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
public class SubCategoryRest implements SubCategoryRestApi {

    @Context
    UriInfo uriInfo;

    @EJB
    protected CategoryEJB categoryEJB;


    @Override
    public ListDto<SubCategoryDto> get(
            @ApiParam("Offset in the list of news") @DefaultValue("0") Integer offset,
            @ApiParam("Limit of news in a single retrieved page") @DefaultValue("10") Integer limit)
    {


        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        List<CategorySub> subList;
        int maxResults = 50;

        subList = categoryEJB.getCategoryListSubWithMax(maxResults);



        if(offset != 0 && offset >=  subList.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+subList.size(), 400);
        }

        ListDto<SubCategoryDto> dto = Converter.transformCollectionSub(
                subList, offset, limit);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/subcategories")
                .queryParam("limit", limit);

        dto._links.self = new HalLink(builder.clone()
                .queryParam("offset", offset)
                .build().toString()
        );

        if (!subList.isEmpty() && offset > 0) {
            dto._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            );
        }
        if (offset + limit < subList.size()) {
            dto._links.next = new HalLink(builder.clone()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            );
        }
        return dto;
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

    //------------------------------------------------ DEPRECATED ------------------------------------------------//

    @Override
    public Response deprecatedGetById(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("subcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedDelete(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("subcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedUpdate(@ApiParam(ID_PARAM) Long id, @ApiParam("The sub category that will replace the old one. Cannot change its id though.") SubCategoryDto dto) {
        return Responder.response("subcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedPatch(@ApiParam("The unique id of the counter") Long id, @ApiParam("Change sub category") String text) {
        return Responder.response("subcategories/", 301, id, "");
    }

    @Override
    public Response deprecatedGetSubCategoriesByParentId(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("/categories/", 301, id, "/subcategories");
    }

    @Override
    public Response deprecatedGetSubSubCategoriesBySubId(@ApiParam(ID_PARAM) Long id) {
        return Responder.response("subcategories/", 301, id, "/subsubcategories");
    }

}
