package com.ez08.compass.parser;

import com.ez08.compass.entity.StockNews;
import com.ez08.compass.entity.StockNewsEntity;
import com.ez08.compass.entity.StockNotice;
import com.ez08.compass.entity.StockReport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class StockNewsParser {
	public StockNewsEntity parserResult(String result){
		StockNewsEntity entity=new StockNewsEntity();
		List<StockNews> stockNewsList=new ArrayList<StockNews>();
		List<StockNotice> stockNoticeList=new ArrayList<StockNotice>();
		List<StockReport> stockReportList=new ArrayList<StockReport>();
		JSONObject json=null;
		try{
			json=new JSONObject(result);
//			if(json!=null&&(Boolean) json.get("result")&&json.has("newslist")&&json.has("noticelist")&&json.has("reportlist")){
			if(json!=null){
//				JSONArray newslist=(JSONArray) json.get("newslist");
//				JSONArray noticelist=(JSONArray) json.get("noticelist");
//				JSONArray reportlist=(JSONArray) json.get("reportlist");

				JSONArray newslist=null;
				JSONArray noticelist=null;
				JSONArray reportlist=null;

				if(json.has("newslist")){
					newslist=(JSONArray) json.get("newslist");
				}
				if(json.has("noticelist")){
					noticelist=(JSONArray) json.get("noticelist");
				}
				if(json.has("reportlist")){
					reportlist=(JSONArray) json.get("reportlist");
				}

				if(newslist!=null){
					for(int i=0;i<newslist.length();i++){
						JSONArray news=newslist.getJSONArray(i);
						if(news!=null){
							StockNews stockNews=new StockNews();
							stockNews.setId(news.getString(0));
							stockNews.setTitle(URLDecoder.decode(news.getString(1),"UTF-8"));
							String date=news.getInt(2)+"";
							String[] lTime=date.split("");
							if (lTime != null && lTime.length > 8) {
								date = lTime[1] + lTime[2] + lTime[3] + lTime[4]
										+ "-" + lTime[5] + lTime[6] + "-"
										+ lTime[7] + lTime[8];
							}
							stockNews.setDate(date);
							stockNewsList.add(stockNews);
						}
					}
					entity.setNews(stockNewsList);
				}
				if(noticelist!=null){
					for(int i=0;i<noticelist.length();i++){
						JSONArray notice=noticelist.getJSONArray(i);
						if(notice!=null){
							StockNotice stockNotice=new StockNotice();
							stockNotice.setId(String.valueOf(notice.getInt(0)));
							stockNotice.setTitle(URLDecoder.decode(notice.getString(1),"UTF-8"));
							String date=notice.getInt(2)+"";
							String[] lTime=date.split("");
							if (lTime != null && lTime.length > 8) {
								date = lTime[1] + lTime[2] + lTime[3] + lTime[4]
										+ "-" + lTime[5] + lTime[6] + "-"
										+ lTime[7] + lTime[8];
							}
							stockNotice.setDate(date);
							stockNoticeList.add(stockNotice);
						}
					}
					entity.setNotice(stockNoticeList);
				}
				if(reportlist!=null){
					for(int i=0;i<reportlist.length();i++){
						JSONArray report=reportlist.getJSONArray(i);
						if(report!=null){
							StockReport stockReport=new StockReport();
							stockReport.setId(report.getString(0));
							stockReport.setTitle(URLDecoder.decode(report.getString(3),"UTF-8"));
							stockReport.setDate(report.getString(1));
							stockReportList.add(stockReport);
						}
					}
				}
				entity.setReport(stockReportList);
			}
			return entity;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
