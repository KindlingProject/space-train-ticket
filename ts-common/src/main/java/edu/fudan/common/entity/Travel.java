package edu.fudan.common.entity;

import edu.fudan.common.util.StringUtils;
import lombok.Data;

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
