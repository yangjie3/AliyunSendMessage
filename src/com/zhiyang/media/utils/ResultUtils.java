package com.zhiyang.media.utils;

import java.util.List;

import com.zhiyang.media.bean.Result;

public class ResultUtils {
	public static Result ListToResult(List<Result> imgList) {
		Result r = new Result();
		for(int i = 0;i < imgList.size();i++) {
			Result result = imgList.get(i);
			if(result.getSuggestion().equals("block")) {
				r.setUrl(imgList.get(i).getUrl());
				r.setSuggestion(result.getSuggestion());
				r.setScene(result.getScene());
				r.setLabel(result.getLabel());
				r.setRate(result.getRate());
				r.setType(result.getType());
				return r;
			}
		}
		for(int i = 0;i < imgList.size();i++) {
			Result result = imgList.get(i);
			if(result.getSuggestion().equals("review")) {
				r.setUrl(imgList.get(i).getUrl());
				r.setSuggestion(result.getSuggestion());
				r.setScene(result.getScene());
				r.setLabel(result.getLabel());
				r.setRate(result.getRate());
				r.setType(result.getType());
				return r;
			}
		}
		for(int i = 0;i < imgList.size();i++) {
			Result result = imgList.get(i);
			if(result.getSuggestion().equals("pass")) {
				r.setUrl(imgList.get(i).getUrl());
				r.setSuggestion(result.getSuggestion());
				r.setScene(result.getScene());
				r.setLabel(result.getLabel());
				r.setRate(result.getRate());
				r.setType(result.getType());
				return r;
			}
		}
		return null;
		
	}
}
