package datalayer.categories;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thang on 01.11.2016.
 */
@Entity
@NamedQueries(value = {
        @NamedQuery(name = Category.FIND_ALL, query = "SELECT a FROM Category a WHERE a.isRoot = TRUE"),
        @NamedQuery(name = Category.FIND_ALL_SUB, query = "SELECT a FROM Category a WHERE a.isSub = TRUE"),
        @NamedQuery(name = Category.FIND_ALL_SUBSUB, query = "SELECT a FROM Category a WHERE a.isSubSub = TRUE")
})
public class Category implements Serializable{
    public static final String FIND_ALL = "Category.find_all";
    public static final String FIND_ALL_SUB = "Category.find_all_sub";
    public static final String FIND_ALL_SUBSUB = "Category.find_all_subsub";


    @Id @GeneratedValue
    private Long id;

    @NotBlank
    private String categoryName;

    @NotNull
    private boolean isRoot;

    @NotNull
    private boolean isSub;

    @NotNull
    private boolean isSubSub;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<CategorySub> categorySubs;















    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<CategorySub> getCategorySubs() {
        return categorySubs;
    }

    public void setCategorySubs(List<CategorySub> categorySubs) {
        this.categorySubs = categorySubs;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public boolean isSubSub() {
        return isSubSub;
    }

    public void setSubSub(boolean subSub) {
        isSubSub = subSub;
    }
}
