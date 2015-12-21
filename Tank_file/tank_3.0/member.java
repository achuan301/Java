package test_4;

import java.util.Vector;

public class Members {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
//炸弹类
class Bomb
{
   //定义炸弹的坐标
	int x,y;
	//炸弹的生命
    int life=9;
    boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//减少生命值
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}else{
			this.isLive=false;
		}
	}
}
//定义子弹类
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=2;
	boolean isLive=true;//判断子弹是否死亡
	public Shot(int x, int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	public void run() {
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(direct)
			{
			case 0://向上发射子弹
				y-=speed;
				break;
			case 1://向右发射子弹
				x+=speed;
				break;
			case 2://向下发射子弹
				y+=speed;
				break;
			case 3://向左发射子弹
				x-=speed;
				break;
			}
			//子弹何时死亡？？?
			//判断该子弹是否碰到边缘
			if(x<0||x>400||y<0||y>300)
			{
				this.isLive=false;
				break;
				
			}
		}
	}
}
//定义坦克类
class Tank
{
	//表示坦克的横坐标
	int x=0;
	//坦克纵坐标
  int y=0;
  
  //坦克方向
  //0表示上 1表示右 2表示下 3表示左
  int direct=0;
  int color;
  
   //坦克的速度
  int speed=2;
  
public int getColor() {
	return color;
}
public void setColor(int color) {
	this.color = color;
}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getDirect() {
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
}
//敌人坦克类，变成线程类
class EnemyTank extends Tank implements Runnable
{
   boolean isLive=true;
	public EnemyTank(int x, int y) {
		super(x, y);
	}
	
	public void run() {
		
		while(true)
		{
			switch(this.direct)
			{
			case 0:
				//说明坦克在向上
				for(int i=0;i<30;i++)
				{
					if(y>0)
					{
						y-=speed;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for(int i=0;i<30;i++)
				{
					if(x<353)
					{
						x+=speed;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for(int i=0;i<30;i++)
				{
					if(y<231)
					{
						y+=speed;
					}	
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 3:
				for(int i=0;i<30;i++)
				{
					if(x>0)
					{
						x-=speed;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			//让坦克随机产生一个新的方向
			this.direct=(int)(Math.random()*4);
			//判断敌人坦克是否是否死亡
			if(this.isLive==false)
			{
				//让坦克死亡后，退出线程
				break;
			}
		}
	}
	
}
//我的坦克
class Hero extends Tank
{
	//子弹
    Shot s=null;
    //子弹连发设置，集合类使用，线程安全
	Vector<Shot>ss=new Vector<Shot>();
	
	public  Hero(int x,int y)
	{
		super(x,y);
	}
	
	//开火
	public void shotEnemy()
	{
	  switch(this.direct)
	  {
	  case 0:
		  //创建一颗子弹
		   s=new Shot(x+10,y-2,0);
		   //把子弹加入到向量
		   ss.add(s);
		   break;
	  case 1:
		   s=new Shot(x+32,y+9,1);
		   ss.add(s);
		   break;
	  case 2:
		   s=new Shot(x+10,y+32,2);
		   ss.add(s);
		   break;
	  case 3:
		   s=new Shot(x-2,y+9,3);
		   ss.add(s);
		   break;
	  }
	  //启动子弹线程
	  Thread t1=new Thread(s);
	  t1.start();
	}
	//坦克向的移动 上、右、下、左
	public void moveUp()
	{
		if(y>0)
		y-=speed;
	}
	public void moveRight()
	{
		if(x<353)
		x+=speed;
	}
	public void moveDown()
	{
		if(y<231)
		y+=speed;
	}
	public void moveLeft()
	{
		if(x>0)
		x-=speed;
	}
}
