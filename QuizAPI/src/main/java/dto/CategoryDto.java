package dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("A Category")
public class CategoryDto {
    @ApiModelProperty("The id of the category")
    public String id;

    @ApiModelProperty("The root category")
    public String rootCategory;

    public CategoryDto(){}

    public CategoryDto(String id, String rootcategory){
        this.id = id;
        this.rootCategory = rootcategory;
    }
}