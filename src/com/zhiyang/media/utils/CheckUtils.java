package com.zhiyang.media.utils;

import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.zhiyang.media.bean.Audit;
import com.zhiyang.media.bean.Result;
import com.zhiyang.media.bean.TXTResult;
import com.zhiyang.media.dao.DaoImpl;

import redis.clients.jedis.Jedis;

public class CheckUtils {
	/**
	 * TODO 图文检测结果处理，并进行回写
	 * @param txtScan
	 * @param imgScan
	 * @param collection
	 * @param _id
	 */
	public static void imgCheckAudit(TXTResult txtScan,List<Result> imgScan,MongoCollection<Document> collection,String _id,String redisId) {
		DaoImpl dao = new DaoImpl();
		if(txtScan!=null) {
			Result r = new Result();
			r.setLabel(txtScan.getLabel());
			r.setRate(txtScan.getRate());
			r.setScene(txtScan.getScene());
			r.setSuggestion(txtScan.getSuggestion());
			r.setUrl(txtScan.getTxtcontent());
			imgScan.add(r);
		}
       //创建Audit信息
 		Audit a = new Audit();
 		String[] level = new String[imgScan.size()];
         StringBuffer sf = new StringBuffer();
         for (int i = 0;i<imgScan.size();i++) {
         	Result result = imgScan.get(i);
				if(!result.getSuggestion().equals("pass")) {
					String lable = result.getLabel();
					sf.append(lable+",");
				}
				if(result.getSuggestion().equals("block")) {
					if(result.getLabel().equals("ad")||result.getLabel().equals("flood")||result.getLabel().equals("qrcode")) {
 					level[i] = "D";
 				}else {
 					level[i] = "E";
 				}
				}else if(result.getSuggestion().equals("pass")){
 				level[i] = "C";
 			}else if(result.getSuggestion().equals("review")) {
 				if(result.getLabel().equals("politics")||result.getLabel().equals("terrorism")||result.getLabel().equals("porn")||result.getLabel().equals("others")||result.getLabel().equals("customized")) {
 					level[i] = "E";
 				}else {
 					level[i] = "D";
 				}
 			}		
			}
        	 for (String string : level) {
        		 if("E".equals(string)) {
        			 a.setAuditlevel("E");
        			 break;
        		 }
        	 }
        	 if(!"E".equals(a.getAuditlevel())) {
        		 for (String string : level) {
            		 if("D".equals(string)) {
            			 a.setAuditlevel("D");
            			 break;
            		 }
            	 }
        	 }
        	 if(!"D".equals(a.getAuditlevel())&&!"E".equals(a.getAuditlevel())){
        		 a.setAuditlevel("C");
        	 }
       
         if(a.getAuditlevel().equals("C")) {
         	a.setAuditresult("pass");
         }else {
         	a.setAuditresult("pending");
         }
         if(sf.toString().isEmpty()) {
         	a.setAuditdescription("nomal");
         }else {
         	a.setAuditdescription(sf.deleteCharAt(sf.length()-1).toString());
         }
			//将content中的auditresult结果进行变更-------------测试，最终结果需要调用图文审核最终结果进行回写
			Document auditresult = dao.getDocumentById(collection, _id).append("auditresult", a.getAuditresult()).append("auditlevel", a.getAuditlevel()).append("auditor", a.getAuditor()).append("auditdescription", a.getAuditdescription());
			collection.updateOne(dao.getDocumentById(collection, _id),new Document("$set",auditresult));
		//变更通过统计	
		Jedis jedis = JedisUtils.getJedis();
		Map<String, String> h = jedis.hgetAll(redisId);
		if(!h.containsKey("passcontent")) {
			h.put("passcontent", "0");
		}
		String string = h.get("passcontent");
		h.put("passcontent", (Integer.parseInt(string)+1)+"");
		jedis.hmset(redisId, h);
		JedisUtils.returnResource(jedis);
	}
	/**
	 * TODO 纯文字检测结果处理并进行回写
	 * @param result
	 * @param collection
	 * @param _id
	 */
	public static void txtCheckAudit(TXTResult result,MongoCollection<Document> collection,String _id,String redisId) {
		DaoImpl dao = new DaoImpl();
	       //创建Audit信息
	 		Audit a = new Audit();
	 		if(result.getSuggestion().equals("block")) {
				if(result.getLabel().equals("ad")||result.getLabel().equals("flood")) {
					a.setAuditlevel("D");
				}else {
					a.setAuditlevel("E");
				}
			}else if(result.getSuggestion().equals("pass")){
				a.setAuditlevel("C");
			}else if(result.getSuggestion().equals("review")) {
				if(result.getLabel().equals("politics")||result.getLabel().equals("terrorism")||result.getLabel().equals("porn")||result.getLabel().equals("customized")) {
					a.setAuditlevel("E");
				}else {
					a.setAuditlevel("D");
				}
			}											//level
			a.setAuditdescription(result.getLabel());   //description
			 if(a.getAuditlevel().equals("E")) {
					a.setAuditresult("pending");
			 }else {
				a.setAuditresult("pass");
				
			}                                             //result
				//将content中的auditresult结果进行变更-------------测试，最终结果需要调用图文审核最终结果进行回写
				Document auditresult = dao.getDocumentById(collection, _id).append("auditresult", a.getAuditresult()).append("auditlevel", a.getAuditlevel()).append("auditor", a.getAuditor()).append("auditdescription", a.getAuditdescription());
				collection.updateOne(dao.getDocumentById(collection, _id),new Document("$set",auditresult));
			//变更通过统计	
			Jedis jedis = JedisUtils.getJedis();
			Map<String, String> h = jedis.hgetAll(redisId);
			if(!h.containsKey("passcontent")) {
				h.put("passcontent", "0");
			}
			String string = h.get("passcontent");
			h.put("passcontent", (Integer.parseInt(string)+1)+"");
			jedis.hmset(redisId, h);
			JedisUtils.returnResource(jedis);
		}
}
