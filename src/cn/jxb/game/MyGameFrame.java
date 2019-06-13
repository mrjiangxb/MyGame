package cn.jxb.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;

/**
 * �ɻ���Ϸ��������
 * @author JXB
 */
public class MyGameFrame extends JFrame{
	
	Image planeImg = GameUtil.getImage("images/plane.png");
	Image bg = GameUtil.getImage("images/bg.jpg");

	Plane plane = new Plane(planeImg,250,250);
	Shell shell = new Shell();
	Shell[] shells = new Shell[40];
	
	Explode bao;
	Date startTime = new Date();
	Date endTime;
	int period; //��Ϸ������ʱ��
	
	@Override
	public void paint(Graphics g) {  //�Զ�������   g�൱��һ֧����
		Color c = g.getColor();
		
		g.drawImage(bg, 0, 0, null);
		
		plane.drawSelf(g);//���ɻ�
		
		for(int i=0; i<shells.length; i++){
			shells[i].draw(g);//���ڵ�
			
			//�ɻ����ڵ�����ײ���
			boolean peng = shells[i].getRect().intersects(plane.getRect());		
			if(peng){
				plane.live = false;
				if(bao==null){
					bao = new Explode(plane.x,plane.y);//���ر�ը��Ч
				
					endTime = new Date();
					period = (int)((endTime.getTime() - startTime.getTime())/1000);
				}
							
				bao.draw(g);			
			}
			//��ʱ���ܣ�������ʾ
			if(!plane.live){
				g.setColor(Color.WHITE);
				Font f = new Font("����", Font.BOLD, 30);
				g.setFont(f);
				g.drawString("���ʱ�䣺"+period+"��", 150, 200);
			}
		}
		g.setColor(c);
	}
	
	/**
	 * ˫������ͼ����˸����
	 */
	private Image offScreenImage = null;
	
	public void updata(Graphics g){
		if(offScreenImage == null){
			offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
			
			Graphics gOff = offScreenImage.getGraphics();
			paint(gOff);
			g.drawImage(offScreenImage, 0, 0, null);
		}
	}
	
	class PaintThread extends Thread{ //�����ػ�����(ˢ�»���)
		@Override
		public void run() {
			while(true){
				repaint(); //�ػ�
				
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//������̼������ڲ���(���̿��Ʒɻ�����)
	class KeyMonitor extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
		}
	}
	
	
	/**
	 * ��ʼ������
	 */
	public void launchFrame(){
		this.setTitle("JXB��Ʒ");
		this.setBounds(430, 130, Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){ //������Ͻ�X��ʱ���˳�����
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new PaintThread().start();//�����ػ����ڵ��߳�
		addKeyListener(new KeyMonitor());//���������Ӽ��̵ļ���
		
		//��ʼ��40���ڵ�
		for(int i=0; i<shells.length; i++){
			shells[i] = new Shell();
		}
		
	}
	
	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.launchFrame();
	}
	
	
}
