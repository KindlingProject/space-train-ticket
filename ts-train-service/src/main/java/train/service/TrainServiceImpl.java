package train.service;

import edu.fudan.common.constants.ServiceKey;
import edu.fudan.common.entity.ErrorSceneFlag;
import edu.fudan.common.entity.Seat;
import edu.fudan.common.util.ErrorUtil;
import edu.fudan.common.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import train.entity.TrainType;
import train.repository.TrainTypeRepository;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainTypeRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainServiceImpl.class);

    @Override
    public boolean create(TrainType trainType, HttpHeaders headers) {
        boolean result = false;
        if (trainType.getName().isEmpty()) {
            TrainServiceImpl.LOGGER.error("[create][Create train error][Train Type name not specified]");
            return result;
        }
        if (repository.findByName(trainType.getName()) == null) {
            TrainType type = new TrainType(trainType.getName(), trainType.getEconomyClass(), trainType.getConfortClass());
            type.setAverageSpeed(trainType.getAverageSpeed());
            repository.save(type);
            result = true;
        } else {
            TrainServiceImpl.LOGGER.error("[create][Create train error][Train already exists][TrainTypeId: {}]", trainType.getId());
        }
        return result;
    }

    @Override
    public TrainType retrieve(String id, HttpHeaders headers) {
        if (!repository.findById(id).isPresent()) {
            TrainServiceImpl.LOGGER.error("[retrieve][Retrieve train error][Train not found][TrainTypeId: {}]", id);
            return null;
        } else {
            return repository.findById(id).get();
        }
    }

    @Override
    public TrainType retrieveByName(ErrorSceneFlag errorSceneFlag, String name, HttpHeaders headers) {
        TrainType tt = repository.findByName(name);

        //自定义故障场景
        ErrorUtil.errorScene(errorSceneFlag, ServiceKey.TS_TRAIN_SERVICE);

        if (null != errorSceneFlag && null != errorSceneFlag.getTraceWay() && 2 != errorSceneFlag.getTraceWay()) {
            this.getRestTicketNumber(errorSceneFlag, headers);
        }

        if (tt == null) {
            TrainServiceImpl.LOGGER.error("[retrieveByName][RetrieveByName error][Train not found][TrainTypeName: {}]", name);
            return null;
        } else {
            return tt;
        }
    }

    @Override
    public String retrieveByName2(ErrorSceneFlag errorSceneFlag, HttpHeaders headers) {

        //自定义故障场景
        String result = ErrorUtil.errorScene(errorSceneFlag, ServiceKey.TS_TRAIN_SERVICE2);

        return result;
    }


    private String getServiceUrl(String serviceName) {
        return "http://" + serviceName;
    }

    private void getRestTicketNumber(ErrorSceneFlag errorSceneFlag, HttpHeaders headers) {
        HttpEntity requestEntity = new HttpEntity(errorSceneFlag, null);
        String seat_service_url = getServiceUrl("ts-seat-service");
        ResponseEntity<Response<String>> re = restTemplate.exchange(
                seat_service_url + "/api/v1/seatservice/seats/left_tickets2",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Response<String>>() {
                });
        TrainServiceImpl.LOGGER.info("[getRestTicketNumber][Get Rest tickets num][num is: {}]", re.getBody().toString());

    }

    @Override
    public List<TrainType> retrieveByNames(List<String> names, HttpHeaders headers) {
        List<TrainType> tt = repository.findByNames(names);
        if (tt == null || tt.isEmpty()) {
            TrainServiceImpl.LOGGER.error("[retrieveByNames][RetrieveByNames error][Train not found][TrainTypeNames: {}]", names);
            return null;
        } else {
            return tt;
        }
    }

    @Override
    @Transactional
    public boolean update(TrainType trainType, HttpHeaders headers) {
        boolean result = false;
        if (repository.findById(trainType.getId()).isPresent()) {
            TrainType type = new TrainType(trainType.getName(), trainType.getEconomyClass(), trainType.getConfortClass(), trainType.getAverageSpeed());
            type.setId(trainType.getId());
            repository.save(type);
            result = true;
        } else {
            TrainServiceImpl.LOGGER.error("[update][Update train error][Train not found][TrainTypeId: {}]", trainType.getId());
        }
        return result;
    }

    @Override
    public boolean delete(String id, HttpHeaders headers) {
        boolean result = false;
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            result = true;
        } else {
            TrainServiceImpl.LOGGER.error("[delete][Delete train error][Train not found][TrainTypeId: {}]", id);
        }
        return result;
    }

    @Override
    public List<TrainType> query(HttpHeaders headers) {
        return repository.findAll();
    }

}
