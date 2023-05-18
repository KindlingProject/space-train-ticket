package security.controller;

import com.alibaba.fastjson.JSON;
import edu.fudan.common.entity.ErrorSceneFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import security.entity.*;
import security.service.SecurityService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/securityservice")
public class SecurityController {

    @Autowired
    private SecurityService securityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

    @GetMapping(value = "/welcome")
    public String home(@RequestHeader HttpHeaders headers) {
        return "welcome to [Security Service]";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/securityConfigs")
    public HttpEntity findAllSecurityConfig(@RequestHeader HttpHeaders headers) {
        SecurityController.LOGGER.info("[findAllSecurityConfig][Find All]");
        return ok(securityService.findAllSecurityConfig(headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/securityConfigs")
    public HttpEntity create(@RequestBody SecurityConfig info, @RequestHeader HttpHeaders headers) {
        SecurityController.LOGGER.info("[addNewSecurityConfig][Create][SecurityConfig Name: {}]", info.getName());
        return ok(securityService.addNewSecurityConfig(info, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(path = "/securityConfigs")
    public HttpEntity update(@RequestBody SecurityConfig info, @RequestHeader HttpHeaders headers) {
        SecurityController.LOGGER.info("[modifySecurityConfig][Update][SecurityConfig Name: {}]", info.getName());
        return ok(securityService.modifySecurityConfig(info, headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/securityConfigs/{id}")
    public HttpEntity delete(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        SecurityController.LOGGER.info("[deleteSecurityConfig][Delete][SecurityConfig Id: {}]", id);
        return ok(securityService.deleteSecurityConfig(id, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/securityConfigs/{accountId}")
    public HttpEntity check(@PathVariable String accountId,
                            @RequestBody ErrorSceneFlag errorSceneFlag,
                            @RequestHeader HttpHeaders headers) {
        SecurityController.LOGGER.info("security-service param = " + JSON.toJSONString(errorSceneFlag));
        return ok(securityService.check(errorSceneFlag, accountId, headers));
    }

}
