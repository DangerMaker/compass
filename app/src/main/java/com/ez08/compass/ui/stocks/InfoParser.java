package com.ez08.compass.ui.stocks;

import com.ez08.compass.entity.InfoEntity;
import com.ez08.support.net.EzMessage;

public class InfoParser implements EzParser<InfoEntity> {
    @Override
    public InfoEntity invoke(EzMessage msg) {
        InfoEntity entity = new InfoEntity();
        String id = msg.getKVData("id").getStringWithDefault("");
        entity.setId(id);
        String title = msg.getKVData("title").getStringWithDefault("");
        entity.setTitle(title);
        String url = msg.getKVData("url").getStringWithDefault("");
        entity.setUrl(url);
        String imageid = msg.getKVData("imageid").getStringWithDefault("");
        entity.setImageid(imageid);
        long time = msg.getKVData("time").getInt64();
        entity.setTime(time);
        String content = msg.getKVData("content").getStringWithDefault("");
        entity.setContent(content);
        String category = msg.getKVData("category").getStringWithDefault("");
        entity.setCategory(category);
        return entity;
    }
}
