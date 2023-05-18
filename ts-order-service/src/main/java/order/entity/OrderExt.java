package order.entity;

import edu.fudan.common.entity.ErrorSceneFlag;
import lombok.Data;

@Data
public class OrderExt extends Order {


    private ErrorSceneFlag errorSceneFlag;

}
