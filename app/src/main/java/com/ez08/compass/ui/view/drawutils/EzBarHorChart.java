package com.ez08.compass.ui.view.drawutils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.ez08.compass.entity.CGPoint;
import com.ez08.compass.entity.ColumnValuesBarListModel;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1.
 */

public class EzBarHorChart extends EzDrawCharBase {
    private int preColor;
    private List<ColumnValuesBarListModel> values;// 图形数据
    private float columnWidth;// 柱宽
    private float gapWidth;// 柱间宽度

    @Override
    public void draw(Canvas canvas, CGPoint position) {
        super.draw(canvas, position);
        if(originPoint==null){
            return;
        }

        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.FILL);
//        paint.setAntiAlias(true);
        float startX = (float) ((originPoint.getX() ) * scale);
        float startY = (float) (originPoint.getY());
        for (int i = 0; i < values.size(); i++) {
            ColumnValuesBarListModel value = values.get(i);
            if(value.isPre()){
                paint.setColor(preColor);
            }else{
                paint.setColor(value.getColumnColor());
            }
//            Log.i("-i", " getValue = " + value.getValue());
            if (value.getValue() >= 0) {
//                if(startY>=0&&startY<=height) {
                if(startY>=startOffset&&startY<=height){
                    if(columnWidth>mMaxWidth){
//                        Paint paint2 = new Paint();
//                        paint2.setColor(Color.BLACK);

//                        canvas.drawRect(startX, startY, (float) (startX - value.getValue() * heightScale),
//                                (float) (startY + mMaxWidth * scale), paint);

                        canvas.drawRect( (float) (startX - value.getValue() * heightScale), startY,startX,
                                (float) (startY + mMaxWidth * scale), paint);


//                        canvas.drawRect(300, startY, 0,
//                                (float) (startY + mMaxWidth * scale), paint2);

                        Log.i("rect","left" + startX + ",top" + startY
                        +",right" + (float) (startX - value.getValue() * heightScale) + ",bottom" + (startY + mMaxWidth * scale)
                        + ",paint" + paint.getColor());
                    }else{
//                        canvas.drawRect(startX, startY, (float) (startX - value.getValue() * heightScale),
//                                (float) (startY + columnWidth * scale), paint);

                        canvas.drawRect((float) (startX - value.getValue() * heightScale), startY, startX,
                                (float) (startY + columnWidth * scale), paint);

                        Log.i("line","o no");
                    }

                }
            } else {
//                canvas.drawRect(startX, startY, (float) (startX + columnWidth
//                        * scale), (float) (startY - value.getValue()
//                        * heightScale), paint);
            }
            startY += columnWidth * scale + gapWidth * scale;
        }

//        canvas.drawRect(width, 0 , 100 ,
//                100, paint);
    }

    private int startOffset=0;
    private int mMaxWidth=0;

    public void setStartOffset(int offset,int maxWidth){
        startOffset=offset;
        mMaxWidth=maxWidth;
    }

    public void setPreIndex(int index){
        if(values==null||values.size()==0){
            return;
        }
        for(int i=0;i<values.size();i++){
            ColumnValuesBarListModel value=values.get(i);
            if(index==i){
                value.setPre(true);
            }else{
                value.setPre(false);
            }
        }
    }

    public void setPreColor(int color){
        preColor=color;
    }

    public List<ColumnValuesBarListModel> getValues() {
        return values;
    }

    public void setValues(List<ColumnValuesBarListModel> values) {
        this.values = values;
    }

    public float getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(float columnWidth) {
        this.columnWidth = columnWidth;
    }

    public float getGapWidth() {
        return gapWidth;
    }

    public void setGapWidth(float gapWidth) {
        this.gapWidth = gapWidth;
    }

}
