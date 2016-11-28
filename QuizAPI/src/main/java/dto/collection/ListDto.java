package dto.collection;

import dto.hal.HalLink;
import dto.hal.HalLinkSet;
import dto.hal.HalObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by thang on 27.11.2016.
 */

@ApiModel(description = "Paginated list of resources with HAL links ")
public class ListDto<T> extends HalObject {

    @ApiModelProperty("The list of resources in the current retrieved page")
    public List<T> list;

    @ApiModelProperty("The index of first element in this page")
    public Integer rangeMin;

    @ApiModelProperty("The index of the last element of this page")
    public Integer rangeMax;

    @ApiModelProperty("The total number of elements in all pages")
    public Integer totalSize;

    /*
        Note: this technically does not override the _links in the superclass
        (that is not possible in Java), but rather hides it away.
        Jackson is smart enough to properly handle this situation, but
        other JSON libraries might not.
     */
    @ApiModelProperty("HAL links")
    public ListLinks _links;


    public static class ListLinks extends HalLinkSet {

        @ApiModelProperty("Link to the 'next' page")
        public HalLink next;

        @ApiModelProperty("Link to the 'previous' page")
        public HalLink previous;
    }
}