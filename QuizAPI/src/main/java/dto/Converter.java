package dto;


import businesslayer.CategoryEJB;
import datalayer.categories.Category;
import datalayer.categories.CategorySub;
import datalayer.categories.CategorySubSub;
import datalayer.essentials.Question;
import datalayer.quiz.Quiz;
import dto.collection.ListDto;

import javax.ejb.EJB;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thang on 31.10.2016.
 */
public class Converter {
    private Converter() {
    }

    public static CategoryDto transform(Category entity, boolean expand) {
        Objects.requireNonNull(entity);

        CategoryDto dto = new CategoryDto();
        dto.id = String.valueOf(entity.getId());
        dto.rootCategory = entity.getCategoryName();
        Map<String, String> list = new HashMap<>();

        if(expand){
            for(CategorySub a: entity.getCategorySubs()){
                if(a.getCategory().getId().equals(entity.getId())){
                    list.put(a.getId().toString(), a.getCategorySubName());
                    for(CategorySubSub b: a.getCategorySubSubs()){
                        if(b.getCategory().getId().equals(entity.getId())){
                            list.put(b.getId().toString(), b.getCategorySubSubName());
                        }
                    }
                }
            }
            dto.extraInfo = list;
        }

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
            listHashMap.put(a.getQuestionsId() + "; " + a.getQuestion(), holderAnswer);
        }
        dto.questionsAndAnswersList = listHashMap;
        return dto;
    }

    public static List<CategoryDto> transform(List<Category> entities, boolean expand) {
        List<CategoryDto> dtoList = null;
        if(entities != null){
            dtoList = entities.stream()
                    .map(n -> transform(n, expand))
                    .collect(Collectors.toList());
        }
        return dtoList;
    }
    public static ListDto<CategoryDto> transformCollection(List<Category> categoryList, int offset, int limit, boolean expand){
        List<CategoryDto> dtoList = null;
        if(categoryList != null){
            dtoList = categoryList.stream()
                    .skip(offset) // this is a good example of how streams simplify coding
                    .limit(limit)
                    .map(n -> transform(n, expand))
                    .collect(Collectors.toList());
        }


        ListDto<CategoryDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = categoryList.size();
        return dto;
    }
    public static ListDto<SubCategoryDto> transformCollectionSub(List<CategorySub> subList, int offset, int limit){
        List<SubCategoryDto> dtoList = null;
        if(subList != null){
            dtoList = subList.stream()
                    .skip(offset) // this is a good example of how streams simplify coding
                    .limit(limit)
                    .map(n -> transform(n))
                    .collect(Collectors.toList());
        }


        ListDto<SubCategoryDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = subList.size();
        return dto;
    }

    public static ListDto<SubSubCategoryDto> transformCollectionSubSub(List<CategorySubSub> subSubList, int offset, int limit){
        List<SubSubCategoryDto> dtoList = null;
        if(subSubList != null){
            dtoList = subSubList.stream()
                    .skip(offset) // this is a good example of how streams simplify coding
                    .limit(limit)
                    .map(n -> transform(n))
                    .collect(Collectors.toList());
        }


        ListDto<SubSubCategoryDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = subSubList.size();
        return dto;
    }

    public static ListDto<QuizDto> transformCollectionQuiz(List<Quiz> quizList, int offset, int limit) {
        List<QuizDto> dtoList = null;
        if (quizList != null) {
            dtoList = quizList.stream()
                    .skip(offset) // this is a good example of how streams simplify coding
                    .limit(limit)
                    .map(n -> transform(n))
                    .collect(Collectors.toList());
        }


        ListDto<QuizDto> dto = new ListDto<>();
        dto.list = dtoList;
        dto._links = new ListDto.ListLinks();
        dto.rangeMin = offset;
        dto.rangeMax = dto.rangeMin + dtoList.size() - 1;
        dto.totalSize = quizList.size();
        return dto;
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