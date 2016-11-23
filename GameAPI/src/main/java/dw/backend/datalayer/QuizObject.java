package dw.backend.datalayer;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thang on 21.11.2016.
 */
@Entity
public class QuizObject {
    @Id @GeneratedValue
    private Long id;

    private HashMap<String, List<String>> hashMapQuiz = new HashMap<>();

    public QuizObject(){

    }




















    public HashMap<String, List<String>> getHashMapQuiz() {
        return hashMapQuiz;
    }

    public void setHashMapQuiz(HashMap<String, List<String>> hashMapQuiz) {
        this.hashMapQuiz = hashMapQuiz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    private String removeChar(String a){
        return a.replaceAll("\"", "");
    }

}
