package train.service;

import edu.fudan.common.entity.ErrorSceneFlag;
import org.springframework.http.HttpHeaders;
import train.entity.TrainType;

import java.util.List;

public interface TrainService {
    //CRUD
    boolean create(TrainType trainType, HttpHeaders headers);

    TrainType retrieve(String id,HttpHeaders headers);

    TrainType retrieveByName(ErrorSceneFlag errorSceneFlag, String name, HttpHeaders headers);


    String retrieveByName2(ErrorSceneFlag errorSceneFlag, HttpHeaders headers);


    List<TrainType> retrieveByNames(List<String> name,HttpHeaders headers);

    boolean update(TrainType trainType,HttpHeaders headers);

    boolean delete(String id,HttpHeaders headers);

    List<TrainType> query(HttpHeaders headers);
}
