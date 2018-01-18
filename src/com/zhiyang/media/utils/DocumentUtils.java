package com.zhiyang.media.utils;

import org.bson.Document;

import com.zhiyang.media.bean.Audit;
import com.zhiyang.media.bean.Result;
import com.zhiyang.media.bean.TXTResult;

public class DocumentUtils {
	    
	public static Document resultToDocument(Result result) {
		Document doc = new Document();
		doc.append("imgurl", result.getUrl());
		doc.append("lable", result.getLabel());
		doc.append("rate", result.getRate());
		doc.append("suggest", result.getSuggestion());
		doc.append("scene", result.getScene());
		doc.append("type", result.getType());
		doc.append("type", result.getType());
		return doc;
	}
	public static Document TXTresultToDocument(TXTResult result) {
		Document doc = new Document();
		doc.append("txtcontent", result.getTxtcontent());
		doc.append("lable", result.getLabel());
		doc.append("rate", result.getRate());
		doc.append("suggest", result.getSuggestion());
		doc.append("scene", result.getScene());
		return doc;
	}
	public static Document AuditToDocument(Audit result) {
		Document doc = new Document();
		doc.append("auditlevel", result.getAuditlevel());
		doc.append("auditresult", result.getAuditresult());
		doc.append("auditdescription", result.getAuditdescription());
		doc.append("auditor", result.getAuditor());
		return doc;
	}
	
	} 