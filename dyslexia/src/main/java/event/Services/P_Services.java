package event.Services;

import java.util.List;
import java.util.List;

    public interface P_Services<T> {
        void Create(T p) throws Exception;
        void Update(T p) throws Exception;
        List<T> Display() throws Exception;
        void Delete(int id_participation) throws Exception;
    }

