package com.example.administrator.a2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/7.
 */
public class Card extends FrameLayout {
    public Card(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);//数字的大小
        label.setBackgroundColor(0x33ffffff);//数字的背景颜色
        label.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(-1,-1);//布满容器
        lp.setMargins(10,10,0,0);//每个卡片都是左上预留10像素的空隙
        addView(label,lp);

    }

    private int num = 0;

    public int getNum(){
        return num;
    }

    public void setNum(int num){
        this.num = num;

        if(num<=0){
            label.setText("");
        }else{
            label.setText(num+" ");
        }
    }

    //判断两张卡片是否相等
    public boolean equals(Card c){
        return this.getNum() == c.getNum();
    }

    private TextView label;

}
