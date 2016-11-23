package dw.api.dto;

import datalayer.essentials.Question;
import datalayer.quiz.Quiz;
import dw.backend.datalayer.Game;
import dw.backend.datalayer.QuizObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by thang on 21.11.2016.
 */
public class GameConverter {

    public static GameDto transform(Game entity) {
        Objects.requireNonNull(entity);

        GameDto dto = new GameDto();
        dto.id = String.valueOf(entity.getId());
        dto.gameName = entity.getGameName();
        dto.quizCount = entity.getQuizCount();

        HashMap<String, List<String>> listHashMap = new HashMap<>();
        for(QuizObject a: entity.getQuizList()){
            for(String key: a.getHashMapQuiz().keySet()){
                listHashMap.put(key, a.getHashMapQuiz().get(key));
            }
        }
        dto.quizObjectList = listHashMap;
        dto.quizIDList = entity.getQuizIdList();

        return dto;
    }

    public static List<GameDto> transform(List<Game> entities) {
        Objects.requireNonNull(entities);

        return entities.stream()
                .map(GameConverter::transform)
                .collect(Collectors.toList());
    }
}
