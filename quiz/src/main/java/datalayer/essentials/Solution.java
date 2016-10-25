package datalayer.essentials;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by thang on 25.10.2016.
 */

@Entity
public class Solution {
    @Id
    @GeneratedValue
    private Long solutionId;

    @NotBlank
    private String solutionToAnswer;














    /*----------------------------------------------GETTER AND SETTER----------------------------------------------*/

    public Long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Long solutionId) {
        this.solutionId = solutionId;
    }

    public String getSolutionToAnswer() {
        return solutionToAnswer;
    }

    public void setSolutionToAnswer(String solutionToAnswer) {
        this.solutionToAnswer = solutionToAnswer;
    }
}
