package com.example.administrator.a2048;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.appcompat.R.styleable.AlertDialog;


public class GameView extends GridLayout {

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    //游戏初始化
    private void initGameView(){

        setColumnCount(4);//规定为4列
        setBackgroundColor(0xffbbada0);//这里是设置游戏的背景颜色

        setOnTouchListener(new OnTouchListener() {
            private float startX,startY,offsetX,offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    //手指按下屏幕事件
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    //手指离开屏事件
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX()-startX;
                        offsetY = event.getY()-startY;

                        if(Math.abs(offsetX)>Math.abs(offsetY)){
                            //如果水平方向上的距离大于垂直方向上的距离，就说明是水平方向上的移动
                            if(offsetX>5)
                                swipeRight();
                            else if(offsetX<-5)
                                swipeLeft();
                        }else{
                            if(offsetY<-5)
                                swipeUp();
                            else if(offsetY>5)
                                swipeDown();
                        }
                        break;
                }
                return true;
                //这里是要返回TRUE的，因为如果是FALSE的话，只能触发down事件，move和up事件触发不了，因为是FALSE，你down完就通知系统失败了，就没有后面什么事了
            }
        });

    }


    //当屏幕大小改变的时候会执行
    //又因为我们规定了屏幕只能是竖屏的，所以这个方法只会在第一次的时候执行
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int cardWidth = (Math.min(w,h)-10)/4;  //其中10是作为卡片和屏幕之间的空隙的
        addCards(cardWidth,cardWidth);
        startGame();
    }

    //添加卡片，卡片大小是一个正方形
    private void addCards(int cardWidth,int cardHeight){
        Card c;
        for(int y = 0;y<4;y++){
            for(int x = 0;x<4;x++){
                c = new Card(getContext());
                c.setNum(0);//一开始的时候全是0，也就是说没显示
                addView(c,cardWidth,cardHeight);

                cardMaps[x][y] = c;//把添加的卡片全部放到数组里面去
            }
        }
    }

    //开始游戏
    private void startGame(){
        MainActivity.getMainActivity().clear();

        //因为可能前面玩过游戏，也就是数组里面有值，所以要先清空
        for(int y = 0;y<4;y++){
            for(int x = 0;x<4;x++){
                cardMaps[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }

    //添加随机数字
    private void addRandomNum(){
        emptyPoints.clear();
        for(int y = 0;y<4;y++){
            for(int x = 0;x<4;x++){
                if(cardMaps[x][y].getNum()<=0){
                    emptyPoints.add(new Point(x,y));//把所有空格都记录下来
                }
            }
        }
        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        cardMaps[p.x][p.y].setNum(Math.random()>0.1?2:4);
        //Math.random()返回的是0-1之间的double数，当返回数大于0.1的时候赋值为2，否则赋值为4
        //这样2和4出现的概率比值为9:1
    }

    private void swipeLeft(){

        //判断有没有发生位置移动，有的话就增加随机数
        boolean b = false;

        for(int y = 0;y<4;y++){
            for(int x = 0;x<4;x++){
                for(int x1 = x+1;x1<4;x1++){
                    if(cardMaps[x1][y].getNum()>0){
                        //1.当前位置的值为0，就把值移到当前位置上
                        if(cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);
                            x--;//假设出现情况[0,2,0,2]，如果没有x--，最后结果就只是[2,2,0,0]

                            b = true;

                        }else if(cardMaps[x][y].equals(cardMaps[x1][y])){
                            //2.当前位置的值与后面的值相等，就合并
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            b = true;

                        }
                        break;
                    }
                }
            }
        }
        if(b){
            addRandomNum();
            checkGameOver();
        }
    }
    private void swipeRight(){
        boolean b = false;
        for(int y = 0;y<4;y++){
            for(int x = 3;x>=0;x--){
                for(int x1 = x-1;x1>=0;x1--){
                    if(cardMaps[x1][y].getNum()>0){
                        if(cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);
                            x++;
                            b = true;
                        }else if(cardMaps[x][y].equals(cardMaps[x1][y])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            b = true;
                        }
                        break;
                    }
                }
            }
        }
        if(b){
            addRandomNum();
            checkGameOver();
        }
    }
    private void swipeUp(){
        boolean b =false;
        for(int x = 0;x<4;x++){
            for(int y = 0;y<4;y++){
                for(int y1 = y+1;y1<4;y1++){
                    if(cardMaps[x][y1].getNum()>0){
                        if(cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);
                            y--;
                            b = true;
                        }else if(cardMaps[x][y].equals(cardMaps[x][y1])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            b = true;
                        }
                        break;
                    }
                }
            }
        }
        if(b){
            addRandomNum();
            checkGameOver();
        }
    }
    private void swipeDown(){
        boolean b = false;
        for(int x = 0;x<4;x++){
            for(int y = 3;y>=0;y--){
                for(int y1 = y-1;y1>=0;y1--){
                    if(cardMaps[x][y1].getNum()>0){
                        if(cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);
                            y++;
                            b = true;
                        }else if(cardMaps[x][y].equals(cardMaps[x][y1])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            b = true;
                        }
                        break;
                    }
                }
            }
        }
        if(b){
            addRandomNum();
            checkGameOver();
        }
    }

    //判断游戏是否结束了
    //每次添加随机数之后都要判断一次
    private void checkGameOver(){
        boolean GameOver = true;
        ALL:
        for(int y = 0;y<4;y++){
            for(int x = 0;x<4;x++){
                //当前为0或者与相邻的值相同的时候，都是可以继续玩下去的
                if(cardMaps[x][y].getNum() == 0||
                        (x>0&&cardMaps[x][y].equals(cardMaps[x-1][y]))||
                        (x<3&&cardMaps[x][y].equals(cardMaps[x+1][y]))||
                        (y>0&&cardMaps[x][y].equals(cardMaps[x][y-1]))||
                        (y<3&&cardMaps[x][y].equals(cardMaps[x][y+1]))){
                    GameOver = false;
                    break ALL;
                }
            }
        }
        if(GameOver){
            new AlertDialog.Builder(getContext()).setTitle("Sry")
                    .setMessage("GameOver")
                    .setPositiveButton("Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).show();

        }

    }

    //定义一个4*4二维数组记录卡片
    private Card[][] cardMaps = new Card[4][4];

    //Point中的x,y正好可以对应数组中的x,y
    private List<Point> emptyPoints = new ArrayList<Point>();
}
