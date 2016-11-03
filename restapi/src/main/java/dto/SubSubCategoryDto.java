package dto;

/**
 * Created by thang on 01.11.2016.
 */
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("A Sub Sub Category")
public class SubSubCategoryDto {
    @ApiModelProperty(value = "The id of the sub sub category", hidden = true)
    public String id;
    @ApiModelProperty("The root category id")
    public String rootId;
    @ApiModelProperty("The sub category id")
    public String subCategoriId;
    @ApiModelProperty(value = "The root category", hidden = true)
    public String rootCategory;
    @ApiModelProperty(value = "The sub category", hidden = true)
    public String subCategory;
    @ApiModelProperty("The sub sub category")
    public String subSubCategory;


    public SubSubCategoryDto() {
    }

    public SubSubCategoryDto(String id, String rootId, String subCategoryId, String subSubCategory) { //, String subcategory, String subSubcategory, String question, String... answers) {
        this.id = id;
        this.rootId = rootId;
        this.subCategoriId = subCategoryId;
        this.subSubCategory = subSubCategory;
    }
}