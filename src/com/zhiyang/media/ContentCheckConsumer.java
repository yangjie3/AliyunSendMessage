package com.zhiyang.media;



import java.util.List;
import org.bson.Document;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import com.mongodb.client.MongoCollection;
import com.zhiyang.media.bean.Result;
import com.zhiyang.media.bean.TXTResult;
import com.zhiyang.media.dao.DaoImpl;
import com.zhiyang.media.utils.CheckUtils;


public class ContentCheckConsumer {
	public static void comsume() {
		CloudAccount account = new CloudAccount(
                ServiceSettings.getMNSAccessKeyId(),
                ServiceSettings.getMNSAccessKeySecret(),
                ServiceSettings.getMNSAccountEndpoint());
        MNSClient client = account.getMNSClient(); //this client need only initialize once
        	System.out.println("ContentCheck_client创建成功");
        CloudQueue queue = client.getQueueRef("ContentCheck");// replace with your queue name
        // Demo for receive message code
        System.out.println("ContentCheck_queue链接成功");
		DaoImpl dao = new DaoImpl();
		String databaseName = "aimengchong";
		String collectionName = "content";
		MongoCollection<Document> collection = dao.getCollection(databaseName,collectionName);
        while(true) {
        try{
                Message popMsg = queue.popMessage();
                if (popMsg != null){
                    System.out.println("message handle: " + popMsg.getReceiptHandle());
                    System.out.println("message body: " + popMsg.getMessageBodyAsString());
                    System.out.println("message id: " + popMsg.getMessageId());
                    System.out.println("message dequeue count:" + popMsg.getDequeueCount());
                    //<<to add your special logic.>>
                    //消息主题进行拆分
//                    String _id = popMsg.getMessageBodyAsString();
                    String msg = popMsg.getMessageBodyAsString();
                    String[] split = msg.split("_");
                    if(split.length==2) {
                    String _id = split[0];
                    String redisId = split[1];
                    if(split.length==2) {
                    //1、从Mongo中获取数据信息，截取其中的字段
                    Document document = dao.getDocumentById(collection, _id);
                    if(document.containsKey("img")) {
                    	//2、对其中的txt和imgurl提交至审核
                    	TXTResult txtScan = dao.TXTScan(collection, _id);
                    	List<Result> imgScan = dao.IMGScan(collection,_id);
                    	
                    	//3、对审核的结果进行处理,更新至Mongo中
                    	CheckUtils.imgCheckAudit(txtScan, imgScan, collection, _id,redisId);
                    	
                    }else {
                    	//2、对其中的txt提交至审核
                    	TXTResult txtScan = dao.TXTScan(collection, _id);
                    	//3、对审核的结果进行处理,更新至Mongo中
                    	CheckUtils.txtCheckAudit(txtScan, collection, _id,redisId);
                    }
                    //remember to  delete message when consume message successfully.
                    queue.deleteMessage(popMsg.getReceiptHandle());
                    System.out.println("delete message successfully.\n");
                    }else {
                    	System.out.println("data error!!!");
                    }
                    }
                }
            }
          catch (ClientException ce)
        {
            System.out.println("Something wrong with the network connection between client and MNS service."
                    + "Please check your network and DNS availablity.");
            ce.printStackTrace();
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                System.out.println("Queue is not exist.Please create queue before use");
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                System.out.println("The request is time expired. Please check your local machine timeclock");
            }
            /*
            you can get more MNS service error code in following link.
            https://help.aliyun.com/document_detail/mns/api_reference/error_code/error_code.html?spm=5176.docmns/api_reference/error_code/error_response
            */
            se.printStackTrace();
        } catch (Exception e)
        {
            System.out.println("Unknown exception happened!");
            e.printStackTrace();
        }
       
        }

	}
}
