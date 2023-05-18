package security.service;

import edu.fudan.common.entity.ErrorSceneFlag;
import edu.fudan.common.util.Response;
import org.springframework.http.HttpHeaders;
import security.entity.*;

/**
 * @author fdse
 */
public interface SecurityService {

    Response findAllSecurityConfig(HttpHeaders headers);

    Response addNewSecurityConfig(SecurityConfig info, HttpHeaders headers);

    Response modifySecurityConfig(SecurityConfig info, HttpHeaders headers);

    Response deleteSecurityConfig(String id, HttpHeaders headers);

    Response check(ErrorSceneFlag errorSceneFlag, String accountId, HttpHeaders headers);

}
