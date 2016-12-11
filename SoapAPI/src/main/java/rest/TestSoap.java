package rest;

/**
 * Created by thang on 11.12.2016.
 */
import javax.jws.WebService;
import java.util.List;


@WebService( name = "TestSoap")
public interface TestSoap {

    String get();

}