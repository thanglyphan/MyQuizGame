package dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;


@ApiModel("A Category")
public class CategoryDto {
    @ApiModelProperty("The id of the category")
    public String id;

    @ApiModelProperty("The root category")
    public String rootCategory;

    @ApiModelProperty(value = "Extra info on the root", hidden = true)
    public Map<String, String> extraInfo;

    public CategoryDto(){}

    public CategoryDto(String id, String rootcategory){
        this.id = id;
        this.rootCategory = rootcategory;
    }
}