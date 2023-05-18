package edu.fudan.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;


@Data
public class ErrorSceneFlag {

    /**
     * 注入故障的service，以及对应的故障，key是service name,比如 ts-order-service;
     */

    private Map<String, ErrorScene> errorSceneMap = new HashMap<>();

    /**
     * 走哪条链路：1， 2
     */
    private Integer traceWay;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorScene {

        /**
         * json故障
         */
        private Boolean jsonError;

        /**
         * 序列化数据大小
         */
        private Integer jsonCount;

        /**
         * 大对象返回
         */
        private Boolean bigResultError;

        /**
         * 文件读取过慢
         */
        private Boolean ioError;

        /**
         * io故障文件名
         */
        private String fileName;

        /**
         * 接口抛出exception
         */
        private Boolean exceptionError;

        /**
         * 日志锁error
         */
        private Boolean logLockError;

        /**
         * dns解析失败
         */
        private Boolean dnsError;

        /**
         * dns访问地址
         */
        private String dnsUrl;

        /**
         * 从第三方接口下载文件
         */
        private Boolean apiDownload;

        /**
         * 第三方下载文件的地址
         */
        private String apiDownloadUrl;



    }
}
