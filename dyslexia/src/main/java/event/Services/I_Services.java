package event.Services;
import java.util.List;
import event.Events_Attributs.Events;

import java.awt.*;

 public interface I_Services <T> {

    void Create (T e)throws Exception;
    void Update(T e)throws Exception;
    List<T> Display() throws Exception;
    void Delete(int id_events)throws Exception;


}
