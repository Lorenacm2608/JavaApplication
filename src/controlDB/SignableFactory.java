package controlDB;
import libreries.Signable;
/**
 *
 * @author Nadir Essadi
 */
public class SignableFactory {
    public Signable getSignableImplementation( ){            
            return new DaoImplementation();
    }
}