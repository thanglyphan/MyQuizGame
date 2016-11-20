package dw.api.implementation;

import dw.api.rest.GameRestApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by thang on 20.11.2016.
 */
public class GameRest implements GameRestApi {
    @Override
    public List<String> get(String x) {
        int count = Integer.parseInt(x);

        List<String> list = new ArrayList<>();
        list.add("Hello1");
        list.add("Hello2");
        list.add("Hello3");
        list.add("Hello4");
        list.add("Hello5");
        list.add("Hello6");
        list.add("Hello7");

        List<String> stringList = new ArrayList<>();
        Collections.shuffle(list);

        for(String a: list){
            if(stringList.size() < count){
                stringList.add(a);
            }
        }
        return stringList;
    }

    @Override
    public Long createGame(String x) {
        return null;
    }


}
