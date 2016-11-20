package dto;


import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Question;
import datalayer.quiz.Quiz;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thang on 31.10.2016.
 */
public class Converter {

    private Converter() {
    }

    public static CategoryDto transform(Category entity) {
        Objects.requireNonNull(entity);

        CategoryDto dto = new CategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.rootCategory = entity.getCategoryName();
        return dto;
    }

    public static SubCategoryDto transform(CategorySub entity) {
        Objects.requireNonNull(entity);

        SubCategoryDto dto = new SubCategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.rootId = String.valueOf(entity.getCategory().getId());
        dto.subCategory = entity.getCategorySubName();
        dto.rootCategory = entity.getCategory().getCategoryName();
        return dto;
    }

    public static SubSubCategoryDto transform(CategorySubSub entity) {
        Objects.requireNonNull(entity);

        SubSubCategoryDto dto = new SubSubCategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.rootId = String.valueOf(entity.getCategory().getId());
        dto.subCategoriId = String.valueOf(entity.getCategorySub().getId());
        dto.rootCategory = entity.getCategory().getCategoryName();
        dto.subCategory = entity.getCategorySub().getCategorySubName();
        dto.subSubCategory = entity.getCategorySubSubName();
        return dto;
    }

    public static QuizDto transform(Quiz entity) {
        Objects.requireNonNull(entity);

        QuizDto dto = new QuizDto();
        dto.id = String.valueOf(entity.getId());
        dto.subSubCategoryId = String.valueOf(entity.getCategorySubSub().getId());
        dto.rootCategory = entity.getCategorySubSub().getCategory().getCategoryName();
        dto.subCategory = entity.getCategorySubSub().getCategorySub().getCategorySubName();
        dto.subsubCategory = entity.getCategorySubSub().getCategorySubSubName();
        dto.quizName = entity.getQuizName();

        Map<String, List<String>> listHashMap = new HashMap<>();
        List<String> holderAnswer;

        for(Question a: entity.getQuestionList()){
            holderAnswer = new ArrayList<>();
            holderAnswer.add(a.getAnswer().getChoiceOne());
            holderAnswer.add(a.getAnswer().getChoiceTwo());
            holderAnswer.add(a.getAnswer().getChoiceThree());
            holderAnswer.add(a.getAnswer().getChoiceFour());
            holderAnswer.add("Solution: " + a.getAnswer().getSolutionToAnswer());
            listHashMap.put(a.getQuestion(), holderAnswer);
        }
        dto.questionsAndAnswersList = listHashMap;
        return dto;
    }

    public static List<CategoryDto> transform(List<Category> entities) {
        Objects.requireNonNull(entities);
        return entities.stream().map(Converter::transform).collect(Collectors.toList());
    }

    public static List<SubCategoryDto> transformSub(List<CategorySub> entities) {
        Objects.requireNonNull(entities);
        return entities.stream().map(Converter::transform).collect(Collectors.toList());
    }

    public static List<SubSubCategoryDto> transformSubSub(List<CategorySubSub> entities) {
        Objects.requireNonNull(entities);
        return entities.stream().map(Converter::transform).collect(Collectors.toList());
    }

    public static List<QuizDto> transformQuiz(List<Quiz> entities) {
        Objects.requireNonNull(entities);
        return entities.stream().map(Converter::transform).collect(Collectors.toList());
    }
}