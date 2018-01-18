package com.zhiyang.media.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20170112.ImageSyncScanRequest;
import com.aliyuncs.green.model.v20170112.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zhiyang.media.bean.Result;
import com.zhiyang.media.bean.TXTResult;

import java.util.*;
public class ScanUtils {
	/**
	 * TODO 图片扫描
	 * @param url
	 * @param type 
	 * @return
	 * @throws Exception
	 */
    public static List<Result> scanIMG(String url, String type) throws Exception {
        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAIBGuuqB9sgjr7", "vPdh99SPWIRA8jAPBEVA3bXXs9zOga");
        DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        imageSyncScanRequest.setContentType(FormatType.JSON);
        imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        imageSyncScanRequest.setEncoding("utf-8");
        imageSyncScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new LinkedHashMap<String, Object>();
        task.put("dataId", UUID.randomUUID().toString());
        task.put("url",url);
        task.put("time", new Date());
        tasks.add(task);
        JSONObject data = new JSONObject();
        /**
         * porn: 色情
         * terrorism: 暴恐
         * qrcode: 二维码
         * ad: 图片广告
         * ocr: 文字识别
         */
        data.put("scenes", Arrays.asList("porn","ad","terrorism","qrcode"));
        data.put("tasks", tasks);
        imageSyncScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        /**
         * 请务必设置超时时间
         */
        imageSyncScanRequest.setConnectTimeout(6000);
        imageSyncScanRequest.setReadTimeout(10000);
        List<Result> list = new ArrayList<Result>();
        try {
            HttpResponse httpResponse = client.doAction(imageSyncScanRequest);
            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
//                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject)sceneResult).getString("scene");
                                String rate = ((JSONObject)sceneResult).getString("rate");
                                String label = ((JSONObject)sceneResult).getString("label");
                                String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                //根据scene和suggetion做相关的处理
                                //do something
                                Result r = new Result();
                                r.setType(type);
                                r.setUrl(url);
                                r.setLabel(label);
                                r.setRate(Double.parseDouble(rate));
                                r.setScene(scene);
                                r.setSuggestion(suggestion);
                                list.add(r);
//                               System.out.println(r);
//                               System.out.println(list);
                            }
                            return list;
                        }else{
                            System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            } else {
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    } 
    /**
	 * TODO 	文本扫描
	 * @param txt
	 * @return
	 * @throws Exception
	 */
    public static TXTResult ScanTXT(String txt) throws Exception {
        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAIBGuuqB9sgjr7", "vPdh99SPWIRA8jAPBEVA3bXXs9zOga");
        DefaultProfile.addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        TXTResult r = new TXTResult();
        r.setTxtcontent(txt);
        task1.put("dataId", UUID.randomUUID().toString());
        task1.put("content", txt);
        tasks.add(task1);
        JSONObject data = new JSONObject();
        /**
          * 文本垃圾检测： antispam
          * 关键词检测： keyword
        **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
//        System.out.println(JSON.toJSONString(data, true));
        textScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        /**
         * 请务必设置超时时间
         */
        textScanRequest.setConnectTimeout(6000);
        textScanRequest.setReadTimeout(10000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
//                System.out.println(JSON.toJSONString(scrResponse, true));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject)sceneResult).getString("scene");
                                String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                String rate = ((JSONObject)sceneResult).getString("rate");
                                String lable = ((JSONObject)sceneResult).getString("label");
                                //根据审核结果做相关的处理
                                //do something
                                r.setLabel(lable);
                                r.setRate(Double.parseDouble(rate));
                                r.setScene(scene);
                                r.setSuggestion(suggestion);
                            }
                            return r;
                        }else{
                            System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                        }
                    }
                } else {
                    System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                }
            }else{
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
}