package businesslayer;

import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thang on 01.11.2016.
 */
@Stateless
public class CategoryEJB {

    @PersistenceContext
    EntityManager em;

    private void persistInATransaction(Object... obj) {
        for(Object o : obj) {
            em.persist(o);
        }
    }

    public Category createCategory(String categoryName){
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategorySubs(new ArrayList<>());
        category.setRoot(true);
        category.setSub(false);
        category.setSubSub(false);
        persistInATransaction(category);
        return category;
    }

    public CategorySub addSubToCategory(Category category, String categorySubName){
        CategorySub categorySub = new CategorySub();
        categorySub.setCategoryName(category.getCategoryName());
        categorySub.setCategory(category);
        categorySub.setCategorySubName(categorySubName);
        categorySub.setCategorySubSubs(new ArrayList<>());
        categorySub.setRoot(false);
        categorySub.setSub(true);
        categorySub.setSubSub(false);
        persistInATransaction(categorySub);

        category.getCategorySubs().add(categorySub);
        em.merge(category);

        return categorySub;
    }

    public CategorySubSub addSubSubToCategorySub(Category category, CategorySub categorySub, String categorySubSubName){
        CategorySubSub categorySubSub = new CategorySubSub();
        categorySubSub.setCategoryName(category.getCategoryName());
        categorySubSub.setCategory(category);
        categorySubSub.setCategorySubSubName(categorySubSubName);
        categorySubSub.setCategorySub(categorySub);
        categorySubSub.setRoot(false);
        categorySubSub.setSub(false);
        categorySubSub.setSubSub(true);
        categorySubSub.setQuizList(new ArrayList<>());
        categorySubSub.setCategorySubName(categorySub.getCategorySubName());
        persistInATransaction(categorySubSub);

        return categorySubSub;
    }


    public List<Category> getCategoryList(){
        return em.createNamedQuery(Category.FIND_ALL).getResultList();
    }
    public List<CategorySub> getCategoryListSub(){
        return em.createNamedQuery(Category.FIND_ALL_SUB).getResultList();
    }
    public List<CategorySubSub> getCategoryListSubSub(){
        return em.createNamedQuery(Category.FIND_ALL_SUBSUB).getResultList();
    }
    public Category get(@NotNull Long categoryId) {
        return em.find(Category.class, categoryId);
    }
    public CategorySub getSub(@NotNull Long id){
        return em.find(CategorySub.class, id);
    }
    public CategorySubSub getSubSub(@NotNull Long id){
        return em.find(CategorySubSub.class, id);
    }
    public void deleteCategory(@NotNull Long categoryId){
        em.remove(em.find(Category.class, categoryId));
    }

}


