/*
 * 功能：坦克游戏的3.0
 * 1.画出坦克。
 * 2.我的坦克可以上下左右移动
 * 3.可以发射子弹，子弹连发(最多5颗)
 * 4.我的坦克发射的子弹打到敌人坦克时,让它消失（爆炸效果）
 */
package test_4;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class tank_4 extends JFrame{
    MyPanel mp=null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		tank_4 pt=new tank_4();
	}
	public tank_4()
	{
		mp=new MyPanel();
		//启动mp线程
		Thread t=new Thread(mp);
		t.start();
		this.add(mp);
		//注册监听
		this.addKeyListener(mp);
		this.setSize(400, 300);
		this.setVisible(true);
	}
}
//我的面板
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//定义一个我的坦克
	Hero hero=null;
	//定义敌人的坦克组
	Vector<EnemyTank>ets=new Vector<EnemyTank>();
	
	//定义炸弹集合
	Vector<Bomb>bombs=new Vector<Bomb>();
	int ensize=5;//坦克的数目
	
	//定义三张图片,三张图片才组成一颗炸弹
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//构造函数
	public MyPanel()
	{
		hero=new Hero(150,200);
		//初始化敌人的坦克
		for(int i=0;i<ensize;i++)
		{
			//先创建敌人坦克对象
			EnemyTank et=new EnemyTank((i+1)*60,0);
			et.setColor(0);
			et.setDirect(2);
			//加入数组
			ets.add(et);
			//启动线程
			Thread t=new Thread(et);
			t.start();
			//加入
			ets.add(et);
			
		}
		//初始化图片
		try {
			image1=ImageIO.read(new File("bomb_1.gif"));
			image2=ImageIO.read(new File("bomb_2.gif"));
			image3=ImageIO.read(new File("bomb_3.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		//画出我方的坦克
		this.drawTank(hero.getX(), hero.getY(), g,this.hero.getDirect(), 1);
		
		//从ss中取出每一颗子弹，并画出
		for(int i=0;i<hero.ss.size();i++)
		{
			Shot myshot=hero.ss.get(i);
			//画出我方的子弹
			if(myshot!=null&&myshot.isLive==true)
			{
			   g.draw3DRect(myshot.x,myshot. y, 1, 1, false);
			}
			if(myshot.isLive==false)
			{
				//从向量中删除该子弹
				hero.ss.remove(myshot);
			}
		}
		//画出我方的子弹
		if(hero.s!=null&&hero.s.isLive==true)
		{
		   g.draw3DRect(hero.s.x,hero.s. y, 1, 1, false);
		}
		
		//画出炸弹
		for(int i=0;i<bombs.size();i++)
		{
			Bomb b=bombs.get(i);
			if(b.life>6)
			{
				g.drawImage(image1, b.x, b.y,30,30,this);
			}else if(b.life>3)
			{
				g.drawImage(image2, b.x, b.y,30,30,this);
			}
			else
			{
				g.drawImage(image3, b.x, b.y,30,30,this);
			}
			//让b的生命值减小
			b.lifeDown();
			//如果炸弹生命值为0，就让炸弹从bombs向量中移除
			if(b.life==0)
			{
				bombs.remove(b);
			}
		}
		
		//画出敌人的坦克
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			if(et.isLive)
			{
				this.drawTank(et.getX(),et.getY() , g, et.getDirect(), 0);
			}
			
		}
		
	}
	
	//写一个函数判断子弹是否击中敌人坦克
	public void hitTank(Shot s,EnemyTank et)
	{
		//判断该坦克的方向
		switch(et.direct)
		{
		//敌人坦克（上下）（左右）
		case 0:
		case 2:
			if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
			{
				//击中
				//子弹死亡
				s.isLive=false;
				//敌人坦克死亡
				et.isLive=false;
				//创建一颗炸弹，放入Vector
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
		case 1:
		case 3:
			if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
			{
				//击中
				//子弹死亡
				s.isLive=false;
				//敌人坦克死亡
				et.isLive=false;	
				Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
		}
	}
	
	    //画出坦克的函数（扩展）
	    public void drawTank(int x,int y,Graphics g,int direct,int type)
	    {
	    	//判断是什么类型的坦克
	    	switch(type)
	    	{
	    	case 0:
	    		g.setColor(Color.cyan);
	    		break;
	    	case 1:
	    		g.setColor(Color.yellow);
	    		break;
	    	}
	    	//判断方向
	    	switch(direct)
	    	{
	    	//向上
	    	case 0:
			//画出坦克
			//①画出左边的矩形
			g.fill3DRect(x, y, 5, 30,false);
			//②画出右边的矩形
			g.fill3DRect(x+15, y, 5, 30,false);
			//③画出中间矩形
			g.fill3DRect(x+5, y+5,11, 20,false);
			//④画出圆形
			g.fillOval(x+5,y+10,8,8);
			//⑤画出中间的炮塔
		    g.drawLine(x+10,y+15,x+10,y-2);
	    	   break;
	    	case 1:
	    	//炮筒向右
	    	//①画出上面的矩形
	    	g.fill3DRect(x, y, 30, 5, false);
	    	//②画出下面的矩形
	    	g.fill3DRect(x, y+15, 30,5,false);
	    	//③画出中间矩形
			g.fill3DRect(x+5, y+5,20, 11,false);
			//④画出圆形
			g.fillOval(x+10,y+5,8,8);
			//⑤画出中间的炮塔
		    g.drawLine(x+14,y+9,x+32,y+9);
		    break;
	    	case 2:
	    	//向下
	    		//①画出左边的矩形
				g.fill3DRect(x, y, 5, 30,false);
				//②画出右边的矩形
				g.fill3DRect(x+15, y, 5, 30,false);
				//③画出中间矩形
				g.fill3DRect(x+5, y+5,11, 20,false);
				//④画出圆形
				g.fillOval(x+5,y+10,8,8);
				//⑤画出中间的炮塔
			    g.drawLine(x+10,y+15,x+10,y+32);
		    	   break;	
	    	case 3:
	    	//向左
	    		//①画出上面的矩形
		    	g.fill3DRect(x, y, 30, 5, false);
		    	//②画出下面的矩形
		    	g.fill3DRect(x, y+15, 30,5,false);
		    	//③画出中间矩形
				g.fill3DRect(x+5, y+5,20, 11,false);
				//④画出圆形
				g.fillOval(x+10,y+5,8,8);
				//⑤画出中间的炮塔
			    g.drawLine(x+14,y+9,x-2,y+9);
			    break;		
	    	}
	    }
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		//键按下处理 a左\s下\w上\d右
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==KeyEvent.VK_W)
			{
			  //设置我的坦克的方向
				this.hero.setDirect(0);
				this.hero.moveUp();
			}else if(e.getKeyCode()==KeyEvent.VK_D)
			{
				this.hero.setDirect(1);
				this.hero.moveRight();
			}else if(e.getKeyCode()==KeyEvent.VK_S)
			{
				this.hero.setDirect(2);
				this.hero.moveDown();
			}else if(e.getKeyCode()==KeyEvent.VK_A)
			{
				this.hero.setDirect(3);
			    this.hero.moveLeft();
			}
			if(e.getKeyCode()==KeyEvent.VK_J)
			{
				//判断玩家是否按j键，开火
				if(this.hero.ss.size()<5)
				{
					this.hero.shotEnemy();
				}
			}
			//必须重新绘制Panel
			this.repaint();
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void run() {
			//每隔100ms去重绘
			while(true)
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//判断是否击中
				for(int i=0;i<hero.ss.size();i++)
				{
					//取出子弹
					Shot myshot=hero.ss.get(i);
					//判断子弹是否有效
					if(myshot.isLive)
					{
						//取出每个敌人坦克，与之判断
					  for(int j=0;j<ets.size();j++)
					  {
						  //取出坦克
						  EnemyTank et=ets.get(j);
						  if(et.isLive)
						  {
							  this.hitTank(myshot, et);
						  }
					  }
					}
				}
				//重绘
				this.repaint();
			}
		}
}		
