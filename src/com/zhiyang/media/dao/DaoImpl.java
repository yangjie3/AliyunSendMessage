package com.zhiyang.media.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zhiyang.media.bean.Result;
import com.zhiyang.media.bean.TXTResult;
import com.zhiyang.media.utils.DocumentUtils;
import com.zhiyang.media.utils.ScanUtils;
import com.zhiyang.media.utils.MongoUtils;
import com.zhiyang.media.utils.ResultUtils;
import com.zhiyang.media.utils.ScanUtils;


public class DaoImpl {
	
	/*public static void main(String[] args) throws Exception {
		DaoImpl dao = new DaoImpl();
		String databaseName = "aimengchong";
		String collectionName = "content";
		String _id = "5a5d590621321a265b1477b4";
		MongoCollection<Document> collection = dao.getCollection(databaseName,collectionName);
		 Document document = dao.getDocumentById(collection,_id);
		 System.out.println(document);
		 Object object = document.get("img");
		 System.out.println(object.toString());
		 String imgs = object.toString();
		 
		 String replaceAll = imgs.replaceAll("Document|}","").replace("{{imgurl=", "").replace("]", "").replace("[", "").replace("imgtype=", "");
		 System.out.println("replace:\t"+replaceAll);
		 String[] split = replaceAll.split(", ");
		 String[] split1 = new String[split.length/2];
		 String[] split2 = new String[split.length/2];
		 for(int i = 0;i<split.length;i++) {
			 if(i%2==0) {
				 split1[i/2]=split[i];
			 }else {
				 split2[(i-1)/2]=split[i];
			 }
		 }
		 System.out.println("=========================================");
		 for (String string : split2) {
			 System.out.println(string);
		 }
		 System.out.println("=========================================");
		 for (String string : split1) {
			 System.out.println(string);
		 }
		 System.out.println("=========================================");
//		 List<Document> list = new ArrayList<Document>();
//		 List<Result> resultList = new ArrayList<Result>();
//		 for (String string : split1) {
////				System.out.println(string);
//				//调用图片审核工具类
//				List<Result> imgList = ScanUtils.scanIMG(string, string);
//				Result result = ResultUtils.ListToResult(imgList);
//				resultList.add(result);
//				Document dresult = DocumentUtils.resultToDocument(result);
//				list.add(dresult);
//			}
//		 for(int i = 0;i < split1.length;i++) {
//			 String url = split1[i];
//			 String type = split1[i];
//			 List<Result> imgList = ScanUtils.scanIMG(url,type);
//			 Result result = ResultUtils.ListToResult(imgList);
//			 resultList.add(result);
//			 Document dresult = DocumentUtils.resultToDocument(result);
//			 list.add(dresult);
//		 }
//			//对img文档进行封装
//				Document append1 = document.append("img", list);
				//更新图片动态
//			collection.updateOne(dao.getDocumentById(collection, _id),new Document("$set",append1));
	}*/
/**
 * TODO 获取所属集合
 * @param dbName
 * @param collectionName
 * @param id
 * @return
 */
	public  MongoCollection<Document>  getCollection(String dbName,String collectionName) {
		//获取数据库
			MongoDatabase db = MongoUtils.getDatabase(dbName);
		//获取集合
			MongoCollection<Document> collection = db.getCollection(collectionName);
			return collection;
	}
/**
 * TODO 获取文档
 * @param collection
 * @param id
 * @return
 */
	public  Document  getDocumentById(MongoCollection<Document> collection,String id) {
			//获取文档
				Document find = collection.find(Filters.eq("_id",new ObjectId(id))).first();
	//			System.out.println(find);
				return find;
		}
/**
 * TODO 图片扫描	
 * @param collection
 * @param _id
 * @return
 * @throws Exception
 */
	public	List<Result> IMGScan(MongoCollection<Document> collection,String _id) throws Exception {
		DaoImpl dao = new DaoImpl();
		Document document = dao.getDocumentById(collection, _id);//文档
		
				Object ob= document.get("img");
				String imgs = ob.toString();
		String replaceAll = imgs.replaceAll("Document|}","").replace("{{imgurl=", "").replace("]", "").replace("[", "").replace("imgtype=", "");
		String[] split = replaceAll.split(", ");
		String[] split1 = new String[split.length/2];
		String[] split2 = new String[split.length/2];
		 for(int i = 0;i<split.length;i++) {
			 if(i%2==0) {
				 split1[i/2]=split[i];
			 }else {
				 split2[(i-1)/2]=split[i];
			 }
		 }
		//IMG获取url 
		List<Document> list = new ArrayList<Document>();
		List<Result> resultList = new ArrayList<Result>();
		//对图文内容进行审核
		for(int i = 0;i < split1.length;i++) {
			 String url = split1[i];
			 String type = split2[i];
			 List<Result> imgList = ScanUtils.scanIMG(url,type);
			 Result result = ResultUtils.ListToResult(imgList);
			 resultList.add(result);
			 Document dresult = DocumentUtils.resultToDocument(result);
			 list.add(dresult);
		 }
		//对img文档进行封装
			Document append1 = document.append("img", list);
			//更新图片动态
		collection.updateOne(dao.getDocumentById(collection, _id),new Document("$set",append1));
		return resultList;
		
	}
	
/**
 * TODO 文字扫描	
 * @param collection
 * @param _id
 * @return
 * @throws Exception
 */
	public TXTResult TXTScan(MongoCollection<Document> collection,String _id) throws Exception {
		DaoImpl dao = new DaoImpl();
		Document document = dao.getDocumentById(collection, _id);//文档
		/**
		 * 文本审核		
		 */
		Document object = (Document) document.get("txt");
		if(object.get("txtcontent")==null) {
			return null;
		}else {
		Object object2 = object.get("txtcontent");
		TXTResult result = ScanUtils.ScanTXT(object2.toString());
			//----------------------对文本审核内容进行变更------------------------------------	
		Document doc = DocumentUtils.TXTresultToDocument(result);
		Document append = document.append("txt", doc);
		collection.updateOne(dao.getDocumentById(collection, _id),new Document("$set",append));
		return result;
		}
	}
	
}
