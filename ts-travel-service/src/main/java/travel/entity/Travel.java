package travel.entity;


import edu.fudan.common.entity.ErrorSceneFlag;
import edu.fudan.common.util.StringUtils;
import lombok.Data;

import travel.entity.Trip;

import java.util.Date;

/**
 * @author fdse
 */
@Data
public class Travel {

    private Trip trip;

    private String startPlace;

    private String endPlace;

    private String departureTime;

    private ErrorSceneFlag errorSceneFlag;

    public Travel(){
        //Default Constructor
    }

}
