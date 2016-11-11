package businesslayer;

import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import datalayer.quiz.Quiz;

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

    public boolean update(@NotNull Long id, @NotNull String rootCategory){
        Category category = em.find(Category.class, id);
        if(category == null){
            return false;
        }
        List<CategorySub> categorySubList = category.getCategorySubs();
        category.setCategoryName(rootCategory);
        category.setCategorySubs(categorySubList);
        category.setRoot(true);
        category.setSub(false);
        category.setSubSub(false);
        return true;
    }

    public boolean updateSub(@NotNull Long id, @NotNull Long rootId, @NotNull String subCategory){
        CategorySub categorySub = getSub(id);
        if(categorySub == null || get(rootId) == null){
            return false;
        }
        List<CategorySubSub> categorySubSubList = categorySub.getCategorySubSubs();
        Category category = get(rootId);
        category.getCategorySubs().remove(categorySub);

        categorySub.setCategoryName(category.getCategoryName());
        categorySub.setCategory(category);
        categorySub.setCategorySubName(subCategory);
        categorySub.setCategorySubSubs(categorySubSubList);
        categorySub.setRoot(false);
        categorySub.setSub(true);
        categorySub.setSubSub(false);

        category.getCategorySubs().add(categorySub);

        return true;
    }

    public boolean updateSubSub(@NotNull Long id, @NotNull Long rootId, @NotNull Long subId, @NotNull String subsubCategory){
        CategorySubSub categorySubSub = em.find(CategorySubSub.class, id);
        if(categorySubSub == null || get(rootId) == null || getSub(subId) == null){
            return false;
        }
        List<Quiz> quizList = categorySubSub.getQuizList();
        getSub(subId).getCategorySubSubs().remove(categorySubSub);

        categorySubSub.setCategoryName(get(rootId).getCategoryName());
        categorySubSub.setCategory(get(rootId));
        categorySubSub.setCategorySubSubName(subsubCategory);
        categorySubSub.setCategorySub(getSub(subId));
        categorySubSub.setRoot(false);
        categorySubSub.setSub(false);
        categorySubSub.setSubSub(true);
        categorySubSub.setQuizList(quizList);
        categorySubSub.setCategorySubName(getSub(subId).getCategorySubName());

        getSub(subId).getCategorySubSubs().add(categorySubSub);

        return true;
    }

    public boolean updatePatch(@NotNull Long id, @NotNull String rootCategory){
        Category category = em.find(Category.class, id);

        if(category == null){
            return false;
        }
        category.setCategoryName(rootCategory);
        return true;
    }

    public boolean updatePatchSub(@NotNull Long id, @NotNull String subCat){
        CategorySub categorySub = em.find(CategorySub.class, id);

        if(categorySub == null){
            return false;
        }
        categorySub.setCategorySubName(subCat);
        return true;
    }

    public boolean updatePatchSubSub(@NotNull Long id, @NotNull String subSubCat){
        CategorySubSub categorySubSub = em.find(CategorySubSub.class, id);

        if(categorySubSub == null){
            return false;
        }
        categorySubSub.setCategorySubSubName(subSubCat);
        return true;
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
    public List<CategorySubSub> getCategoryListSubSubWithQuizzes(){
        List<CategorySubSub> list = new ArrayList<>();
        for(CategorySubSub a: getCategoryListSubSub()){
            if(a.getQuizList().size() > 0){
                list.add(a);
            }
        }
        return list;
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
    public boolean isPresent(Long id){return em.contains(em.find(Category.class, id));}
    public boolean isPresentSub(Long id){return em.contains(em.find(CategorySub.class, id));}
    public boolean isPresentSubSub(Long id){return em.contains(em.find(CategorySubSub.class, id));}

}


