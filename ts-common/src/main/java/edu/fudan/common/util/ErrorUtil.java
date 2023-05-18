package edu.fudan.common.util;

import com.alibaba.fastjson.JSON;
import edu.fudan.common.entity.BigResult;
import edu.fudan.common.entity.ErrorSceneFlag;
import edu.fudan.common.exception.BaseException;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ErrorUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 自定义故障场景
     *
     * @param errorSceneFlag
     * @return
     */
    public static String errorScene(ErrorSceneFlag errorSceneFlag, String serviceKey) {
        if (null == errorSceneFlag || null == errorSceneFlag.getErrorSceneMap() || !errorSceneFlag.getErrorSceneMap().containsKey(serviceKey)) {
            return "success";
        }
        String result = "success";

        ErrorSceneFlag.ErrorScene errorScene = errorSceneFlag.getErrorSceneMap().get(serviceKey);


        ErrorUtil.LOGGER.info("scene test start");

        // cpu running json error
        List<BigResult> list = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < errorScene.getJsonCount(); i++) {
            list.add(handleResult());
        }
        if (null != errorScene.getJsonError() && errorScene.getJsonError()) {
            ErrorUtil.LOGGER.info("json serialize error start");
            net.sf.json.JSONArray.fromObject(list);
            ErrorUtil.LOGGER.info("json serialize error finish , time = " + (System.currentTimeMillis() - startTime));
        } else {
            ErrorUtil.LOGGER.info("json serialize right start");
            JSON.toJSONString(list);
            ErrorUtil.LOGGER.info("json serialize right finish , time = " + (System.currentTimeMillis() - startTime));
        }
        // io error

        if (null != errorScene.getIoError() && errorScene.getIoError()) {
            ErrorUtil.LOGGER.info("file io start");
            long ioStartTime = System.currentTimeMillis();
            String content = readFile(errorScene.getFileName());
            write(content);
            ErrorUtil.LOGGER.info("io finish , time = " + (System.currentTimeMillis() - ioStartTime));
        }

        // Big Result
        if (null != errorScene.getBigResultError() && errorScene.getBigResultError()) {
            result = readFile(errorScene.getFileName());
        }

        //访问域名场景
        if (null != errorScene.getDnsError() && errorScene.getDnsError() && !StringUtils.isEmpty(errorScene.getDnsUrl())) {
            try {
                ErrorUtil.LOGGER.error("DNS visit start");
                dnsTest(errorScene.getDnsUrl());
                ErrorUtil.LOGGER.error("DNS visit end");
            } catch (Exception e) {
                ErrorUtil.LOGGER.error("DNS visit error");
            }
        }

        // 从第三方下载文件
        if (null != errorScene.getApiDownload() && errorScene.getApiDownload() && !StringUtils.isEmpty(errorScene.getApiDownloadUrl())) {
            try {
                ErrorUtil.LOGGER.info("start to download");
                URL url = new URL(errorScene.getApiDownloadUrl());
                URLConnection connection = url.openConnection();

                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                FileOutputStream out = new FileOutputStream("/opt/downdata.txt");

                byte[] dataBuffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    out.write(dataBuffer, 0, bytesRead);
                }
                in.close();
                out.close();
                ErrorUtil.LOGGER.info("finish download");
                File file = new File("/opt/downdata.txt");
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("文件删除成功。");
                    } else {
                        System.err.println("文件删除失败。");
                    }
                }
            } catch (Exception e) {
                ErrorUtil.LOGGER.error("文件下载异常", e);
            }
        }
        // log lock 场景
        if (null != errorScene.getLogLockError() && errorScene.getLogLockError()) {
            ErrorUtil.LOGGER.error("我会记录大量的日志");
            ErrorUtil.LOGGER.error("这是一个测试场景");
            ErrorUtil.LOGGER.error("有很多用户会发生并发请求");
            ErrorUtil.LOGGER.error("没办法，日志是简单而伟大至极的排障数据");
            ErrorUtil.LOGGER.error("所以我必须记录下来");
            ErrorUtil.LOGGER.error("看到这条日志的人一定会发财！！！");
        }

        ErrorUtil.LOGGER.info("scene test end");

        // throw exception场景
        if (null != errorScene.getExceptionError() && errorScene.getExceptionError()) {
            throw new BaseException("some error happened");
        }
        return result;
    }


    private static String readFile(String fileName) {
        StringBuffer content = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) { // 逐行读取文件内容
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static void write(String fileContent) {
        String fileName = "data3.txt"; // 文件名
        long startTime = System.currentTimeMillis();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(fileContent); // 写入字符串内容
            System.out.println("写入文件耗时" + (System.currentTimeMillis() - startTime));
        } catch (IOException e) {
            System.err.println("创建文件失败: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close(); // 关闭文件流
                } catch (IOException e) {
                    System.err.println("关闭文件失败: " + e.getMessage());
                }
            }
//             删除文件
            File file = new File(fileName);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("文件删除成功。");
                } else {
                    System.err.println("文件删除失败。");
                }
            }
        }
    }

    private static BigResult handleResult() {

        BigResult result = new BigResult();

        List<HashMap<String, BigResult.InterResult>> list = new ArrayList<>();

        HashMap<String, BigResult.InterResult> map1 = new HashMap<>();
        map1.put("hhh", create());
        list.add(map1);
        result.setA("I am A");
        result.setB(4);
        result.setList(list);

        HashMap<String, List<BigResult.InterResult>> map = new HashMap<>();
        List<BigResult.InterResult> list1 = new ArrayList<>();
        list1.add(create());
        map.put("hhhhh", list1);
        result.setMap(map);
        return result;
    }


    private static BigResult.InterResult create() {
        BigResult.InterResult interResult = new BigResult.InterResult();
        interResult.setC("I am C in interResult");
        interResult.setD(1);
        List<HashMap<String, Object>> interList = new ArrayList<>();
        for (int k = 0; k < 5; k++) {
            HashMap<String, Object> interMap1 = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                interMap1.put("string" + i, "thing" + i);
                interMap1.put("some" + i, i);
            }
            interList.add(interMap1);
        }
        interResult.setList(interList);

        HashMap<String, List<String>> map = new HashMap<>();
        for (int y = 0; y < 10; y++) {
            List<String> list = new ArrayList<>();
            list.add("list" + y);
            map.put("map" + y, list);
        }
        interResult.setMap(map);
        return interResult;
    }


    public static void dnsTest(String url) throws IOException {
        ErrorUtil.LOGGER.info("Start to call another service:" + url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK) {
                ErrorUtil.LOGGER.info("The result is：{}", EntityUtils.toString(response.getEntity()));
            }
        } catch (ParseException e) {
        }
        ErrorUtil.LOGGER.info("End to call another service:" + url);
    }
}
