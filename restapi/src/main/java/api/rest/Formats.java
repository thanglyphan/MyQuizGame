package api.rest;

/**
 * Created by thang on 07.11.2016.
 */
public interface Formats {
    String BASE_JSON = "application/json; charset=UTF-8";

    //note the "vnd." (which starts for "vendor") and the
    // "+json" (ie, treat it having json structure)
    String V1_JSON = "application/vnd.myquizgame.categories+json; charset=UTF-8; version=1";
}
