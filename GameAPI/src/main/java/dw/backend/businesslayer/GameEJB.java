package dw.backend.businesslayer;

import datalayer.quiz.Quiz;
import dw.backend.datalayer.Game;
import dw.backend.datalayer.QuizObject;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by thang on 25.10.2016.
 */

public class GameEJB implements Serializable {

    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("QUIZ_DB");
    private EntityManager entityManager;
    private HashMap<Long, Game> hashGame = new HashMap<>();


    public GameEJB() {
        entityManager = factory.createEntityManager();
    }

    public Long createGame(String gameName) {
        Game game = new Game();
        game.setGameName(gameName);
        game.setQuizCount(0);
        game.setQuizList(new ArrayList<>());
        game.setQuizIdList(new ArrayList<>());
        //game.setQuizObjectList(new ArrayList<>());

        persistInATransaction(game);
        hashGame.put(game.getId(), game);
        return game.getId();
    }

    public void addQuizToGame(Long id, QuizObject quizId){
        Game game = get(id);
        game.getQuizList().add(quizId);

        game.setQuizCount(game.getQuizIdList().size());
    }
    public void addQuizIdToGameList(Long id, String path, String quizId){
        Game game = get(id);
        game.getQuizIdList().add(path + quizId);
    }


    private void persistInATransaction(Object... obj) {
        for (Object o : obj) {
            entityManager.persist(o);
        }
    }

    public List<Game> getAll(){
        List<Game> list = new ArrayList<>(hashGame.values());
        return list;
    }

    public Game get(Long id) {
        return entityManager.find(Game.class, id);
    }
    public void deleteGame(Long id){
        hashGame.remove(id);
        entityManager.remove(entityManager.find(Game.class, id));
    }
}
