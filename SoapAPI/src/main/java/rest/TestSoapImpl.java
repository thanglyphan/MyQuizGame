package rest;

/**
 * Created by thang on 11.12.2016.
 */

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;


@WebService(
        endpointInterface = "rest.TestSoap"
)
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TestSoapImpl implements TestSoap{


    @Override
    public String get() {
        return "Halla";
    }
}