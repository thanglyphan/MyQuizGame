package datalayer.categories;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thang on 01.11.2016.
 */
@Entity
public class CategorySub extends Category implements Serializable{

    @ManyToOne
    private Category category;

    @NotBlank
    private String categorySubName;

    @OneToMany(mappedBy = "categorySub", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<CategorySubSub> categorySubSubs;











    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/
    public String getCategorySubName() {
        return categorySubName;
    }

    public void setCategorySubName(String categorySubName) {
        this.categorySubName = categorySubName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CategorySubSub> getCategorySubSubs() {
        return categorySubSubs;
    }

    public void setCategorySubSubs(List<CategorySubSub> categorySubSubs) {
        this.categorySubSubs = categorySubSubs;
    }

}
