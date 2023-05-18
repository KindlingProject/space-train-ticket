package edu.fudan.common.entity;

import lombok.Data;

@Data
public class ErrorScene {


    /**
     * json故障
     */
    private Boolean jsonError;

    /**
     * 大对象返回
     */
    private Boolean bigResultError;

    /**
     * 文件读取过慢
     */
    private Boolean ioError;

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
     * io故障文件名
     */
    private String fileName;

    /**
     * 序列化数据大小
     */
    private Integer jsonCount;

}
