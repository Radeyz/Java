import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.lang.Math;

import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class mainclass {
	static Graph g;// = null;// = new Graph();
	static int select1 = 0, select2 = 0, select3 = 0;
	static boolean buttonPressed = false;
    public static void main(String[] args) throws ClassNotFoundException,IOException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
    	
    	javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        myFrame jf = new myFrame();
        jf.createFrame();
        
    }
    //

    //*******************************************************************************************

    //

    public static class myFrame {

        JFrame mainFrame = new JFrame("�������� ����� - ��������");
        String labelstr = new String("1");
        JTextArea textAr = new JTextArea();
        

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable();
        JPanel leftPanel = new JPanel();
        //Graph leftPanel = new Graph();	//FACEPALM
        JPanel rightPanel = new JPanel();
        JToolBar upperPanel = new JToolBar();
        JButton b_nextStep = new JButton("�����");
        JButton button1 = new JButton("�������� �� �����");
        JButton button2 = new JButton("������ ����");
        JButton button3 = new JButton("�������");
        JButton button4 = new JButton("��������");
        
        Border bord =  BorderFactory.createLineBorder(Color.black, 1);        
        public  void createFrame(){
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.setVisible(true);
            addMainPanels();

            textAr.setEditable(false);
            textAr.setCursor(null); 
            textAr.setOpaque(false); 
            textAr.setFocusable(false);
            mainFrame.setSize(1100, 950);
        }
        
        public class ButtonClear implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	g = null;
            	textAr.replaceRange("", 0, textAr.getSelectionEnd());
            	while (model.getRowCount() > 0)  model.removeRow(0);
            	model.setColumnCount(0);
            	labelstr = "1";
            	b_nextStep.setEnabled(true);
            	b_nextStep.setText("�����");
            	button1.setEnabled(true);
            	button2.setEnabled(true);
            	leftPanel.repaint();
            }
        }
        //Action listener to start and step
        public class ButtonAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	String name = b_nextStep.getText();
            	if (name.equals("�����"))
            	{
            		int source = Integer.parseInt(JOptionPane.showInputDialog("������� �������� �������"));
            		if (g.isNegativeCycle(source)) 
                		JOptionPane.showMessageDialog(null, "� ����� ���� ������������� ����"
                				+ "\n ����� ��������� n-1 �����", "��������", JOptionPane.INFORMATION_MESSAGE);	
            		g.Ford_Bellman(source);
            		button1.setEnabled(false);
            		button2.setEnabled(false);
            	}
                int i = 0;
                
                int[] dl = g.get_table();
                String[] _dl= new String[dl.length+1];
                textAr.replaceRange("", 0, textAr.getSelectionEnd());
                textAr.insert("��� " + labelstr + "\n", textAr.getSelectionEnd());
                textAr.insert("��������������� �����: "+g.getEdges()+"\n",textAr.getSelectionEnd() );
                textAr.insert("������ �����:\n", textAr.getSelectionEnd());
                i = Integer.valueOf(labelstr);
                _dl[0]= Integer.toString(i-1);
                for (int j = 0; j<dl.length; j++){
                	if(dl[j] == 1400) _dl[j+1] = "INF";
                	else
                	_dl[j+1]= Integer.toString(dl[j]); 
                }
                textAr.insert(g.getEdge(), textAr.getSelectionEnd());
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(_dl);
                
                g.drawGraph(leftPanel.getGraphics());
                //table.add(_dl[i], null);
                if ( g.step()){
                	
                    b_nextStep.setText("����� ���������");
                    b_nextStep.setEnabled(false);
                }
                else{
                    b_nextStep.setText("��������� ���");
                }
                labelstr = Integer.toString(++i);
               
            }
        }
        //Action listener to read from file
        public class ButtonRFF implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	try{
            		
            		JFileChooser fileopen = new JFileChooser();
            		int ret = fileopen.showDialog(null, "������� ����");
            		if (ret == JFileChooser.APPROVE_OPTION) {
	            		File file = fileopen.getSelectedFile();
	            		
	            		g = new Graph( file );
	            		
	            		JOptionPane.showMessageDialog(null, "������ ���������.", "��������", JOptionPane.INFORMATION_MESSAGE);
	                    
	                    model.addColumn("���");
	                    for(int i= 0;i<g.getN();i++){
	                    	model.addColumn(""+(i+1));
	                    }
	                    textAr.setFont(new Font("ARIAL", 0, 14));
	                    textAr.insert("������ �����:\n", textAr.getSelectionEnd());
	                    textAr.insert(g.getEdge(), textAr.getSelectionEnd());
                   // table = new JTable(model);
            		}
                    //String [] linesAsArray = lines.toArray(new String[lines.size()]);
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "���� �� ������.", "��������", JOptionPane.WARNING_MESSAGE);

                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            	
            		
            }
        }

        //Action listener to input date
        public class ButtonID implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                String strU = new String();
                String strV = new String("0");
                strV = JOptionPane.showInputDialog("������� ���������� ������");
                while(Integer.parseInt(strV) >50){
                	JOptionPane.showMessageDialog(null, "������� ���������� ������. ������� ���������� ������ ������ 50", "����", JOptionPane.WARNING_MESSAGE);
                	strV = JOptionPane.showInputDialog("������� ���������� ������");
                };
                g = new Graph(Integer.parseInt(strV));
                if (!strV.equals("")&&!strV.equals("0")) {
                    strU = JOptionPane.showInputDialog("������� ���������� �����");
                }
                if (!strU.equals("")) {
                    String[] ribs = new String[Integer.valueOf(strU)];
                    for (int i = 0; i < Integer.valueOf(strU); i++) {
                        ribs[i] = JOptionPane.showInputDialog("������� ����� � ���� [u, v, w], ��� \nu->v, w[u,v]");
                    }
                    int x, y, z;
                    String tmp = "";
            		String val = "";
            		for (int i = 0; i < Integer.parseInt(strU); i++) //���������� ������ �����
            		{
            			tmp = ribs[i];
            			int len = tmp.length();
            			int j1 = 0;
            			int j2 = 0;
            			while ((tmp.charAt(j2) != ' ') && (j2 < len-1))
            			{
            				j2++;
            			}
            			//j1 = ������ �����
            			//j2 = ������ �������-1
            			val = tmp.substring(0, j2);
            			x = Integer.parseInt(val);		//������ �������
            			
            			j1 = ++j2;
            			//���������� ������ �������
            			while ((tmp.charAt(j2) != ' ') && (j2 < len-1))
            			{
            				j2++;
            			}
            			val = tmp.substring(j1, j2);
            			y = Integer.parseInt(val);		//������ �������
            			
            			j1 = ++j2;
            			//����������� ����
            			while ((j2 < len) && (tmp.charAt(j2) != ' '))
            			{
            				j2++;
            			}
            			val = tmp.substring(j1, j2);
            			z = Integer.parseInt(val);		//���
            			
            			g.addEdge(x, y, z);	//���������� �����
            		}
                }
                model.addColumn("���");
                for(int i= 0;i<g.getN();i++){
                	model.addColumn(""+(i+1));
                }
                textAr.setFont(new Font("ARIAL", 0, 14));
                textAr.insert("������ �����:\n", textAr.getSelectionEnd());
                textAr.insert(g.getEdge(), textAr.getSelectionEnd());
                JOptionPane.showMessageDialog(null, "���� �������.", "����", JOptionPane.INFORMATION_MESSAGE);

            }
        }
        //Action listener to help
        public class ButtonHELP implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                String HELP = "������ \"�������� �� �����\" ��������� �������� ������� ������;\n������ \"������ ����\"������������� ����������� ��� ������� ������ ������;\n������ \"�����\" �������� ���������� ���������.";
                JOptionPane.showMessageDialog(null, HELP, "�������", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        public void addMainPanels(){
            leftPanel.setBackground(Color.white);
            leftPanel.setPreferredSize(new Dimension(1100 - 350, 950));
            leftPanel.addMouseListener(new CustomListener());
            
            rightPanel.setPreferredSize(new Dimension(350, 950));
            rightPanel.setBackground(Color.white);

            addRightPanels();
            addUpperPanels();
            mainFrame.getContentPane().add(upperPanel, BorderLayout.NORTH);
            mainFrame.getContentPane().add(leftPanel, BorderLayout.CENTER);
            mainFrame.getContentPane().add(rightPanel, BorderLayout.WEST);
            
        }
        
        public void addUpperPanels(){
            //upperPanel.setLayout(new FlowLayout());
            //Read from file
            ActionListener RFF = new ButtonRFF();
            button1.addActionListener(RFF);
            //Input date
            ActionListener ID = new ButtonID();
            button2.addActionListener(ID);
            //HELP
            ActionListener HELP = new ButtonHELP();
            button3.addActionListener(HELP);
            
            ActionListener Clear = new ButtonClear();
            button4.addActionListener(Clear);
            upperPanel.add(button1);
            upperPanel.addSeparator(new Dimension(8, 0));
            upperPanel.add(button2);
            upperPanel.addSeparator(new Dimension(8, 0));
            upperPanel.add(button3);
            upperPanel.addSeparator(new Dimension(8, 0));
            upperPanel.add(button4);
            
        }
        public void addRightPanels(){

            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            /*DefaultTableModel model = new DefaultTableModel();
            model.addColumn("���");
            for(int i= 1;i<=7;i++){
            	model.addColumn(""+i);
            }*/
            table = new JTable(model);
            JScrollPane scroll_text = new JScrollPane(textAr,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            JScrollPane scroll_table = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            scroll_table.setBorder(bord);
            scroll_text.setBorder(bord);
           // scroll_table.setSize(new Dimension(rightPanel.getWidth(), 0));

            ActionListener AL = new ButtonAction();
            b_nextStep.addActionListener(AL);
            //b_nextStep.setPreferredSize(new Dimension(100, 30));

            rightPanel.add(scroll_text);
            rightPanel.add(scroll_table);
            mainFrame.add(b_nextStep, BorderLayout.SOUTH);

        }    
        
        public class CustomListener implements MouseListener
        {
        	public void mouseClicked(MouseEvent e)
        	{
        		int x = e.getX();
        		int y = e.getY();
        		int a = e.getClickCount();
        		if ((g == null) && (a == 2)) 
        		{
        			g = new Graph();
        			g.addVert(x, y);
        			g.drawGraph(leftPanel.getGraphics());
        			return;
        		}
        		else if (g == null)
        		{
        			return;
        		}
        			
        		int i = g.isInVert(x, y);
        		
        		if (e.getClickCount() == 2 && !g.alg)
        		{//���������� ����� �������
        			if (!g.isToClose(x, y))
        				g.addVert(x,y);
        			else 
        				JOptionPane.showMessageDialog(null, "������� ������ � ������ ��������.", "����", JOptionPane.INFORMATION_MESSAGE);
        		}
        		else if (i != 0)
        		{
	        		if (e.getButton() == MouseEvent.BUTTON1)
	        		{//����� �������
	        			if (select1 == 0)
	        			{
	        				//g.setColor(select3, 0);
	        				select1 = i;
	        				g.setColor(select1, 1);
	        			}
	        			else if (select2 == 0)
	        			{
	        				//g.setColor(select3, 0);
	        				select2 = i;
	        				g.setColor(select2, 1);
	        				int w = 0;
	        				
	        				try
	        				{
	        					w = Integer.parseInt(JOptionPane.showInputDialog("������� ��� �������"));
	        				} 
	        				catch (NumberFormatException exptn)
	        				{
	            				JOptionPane.showMessageDialog(null, "����� ������ �����.", "������", JOptionPane.INFORMATION_MESSAGE);	
	            				g.setColor(select1, 0);
		        				g.setColor(select2, 0);
		        				select1 = select2 = 0;
	            				return;
	        				}     				
	        				g.setColor(select1, 0);
	        				g.setColor(select2, 0);
	        				g.addEdge(select1, select2, w);
	        				select1 = select2 = 0;
	        			}
	        			
	        		}
	        		else if (e.getButton() == MouseEvent.BUTTON2)
	        		{
	        			select1 = select2 = select3 = 0;
	        		}
        		}
        		g.drawGraph(leftPanel.getGraphics());
        	}
        	public void mouseEntered(MouseEvent e){}
        	public void mouseExited(MouseEvent e){}
        	public void mousePressed(MouseEvent e)	{}
        	public void mouseReleased(MouseEvent e) {}
        }
    }

    //

    //******************************************************************************************

    //

}

class Graph
{
	static int INF = 1400;		//�����, ����������� �� �������������
	static int MAX = 50;		//����. ����� ������
	static int rad = 20;		//������ �������
	int n; 			//����� ������
	int m; 			//����� �����
	int k, u, v; 	//�������� ��� ��������� ��.
	int dl[]; 		//������ �� ��������� �������
	int consEdge;   //����� ���������������� ����� ��� ��������� ��
	int s; 			//�������� �������
	int p[]; 		//��� �������������� �����
	boolean any, alg;	//��� ��������� ��
	Node e; 		//������ �� ������ ������ �����
	Vert vert[];		//������ �� ������
	
	class Node 			//����� - ������ �����.
	{
		int a, b, w; 	//a->b, weight = w
		int color;		//���� �����
		Node next; 		//��������� �� ��������� ��. ������
		
		public Node(int u, int v, int weight)  //�������� ����� u->v � ����� weight, ���� = 0
		{
			a = u;
			b = v;
			w = weight;
			color = 0;
			next = null;
		}
		public void setColor(int new_color)
		{
			color = new_color;
		}
		public Node getNode(int i)	//���������� ��. � ������� i, ���� ������ ���, �� null
		{
			int j = 0;		//�������
			Node p = e; 	//������ �� ������ ������
			while ( (j < i) && (p != null))
			{
				j++;
				p = p.next;
			}
			return p;
		}
	};
	class Vert 		//����� - �����
	{
		int x,y; //����������
		int color; //���� �������
		public Vert(int X, int Y, int Color)
		{
			x = X;
			y = Y;
			color = Color;
		}
		public Vert()
		{
			x = y = color = 0;
		}
		public void set(int newX, int newY)
		{
			x = newX;
			y = newY;
		}
		public void setColor(int new_color)
		{
			color = new_color;
		}
		public int getX()
		{
			return x;
		}
		public int getY()
		{
			return y;
		}
	}
	public Graph() 		//�����������
	{
		n = 0;
		m = 0;
		dl = new int[MAX];
		p = new int[MAX];
		vert = new Vert[MAX];
		for (int i = 0; i < MAX; i++)
			vert[i] = new Vert();
		e = null;
		alg = false; //�������� �� �������
	}
	public Graph(int number) //�����������
	{
		n = number;
		m = 0;
		dl = new int[MAX];
		p = new int[MAX];
		vert = new Vert[MAX];
		for (int i = 0; i < MAX; i++)
			vert[i] = new Vert();
		
		e = null;
		alg = false; //�������� �� �������
	}
	
	public Graph(File file)
	throws IOException
	{
		int x, y, z; //��� ���������� �� �����
		BufferedReader fin = new BufferedReader (new FileReader(file));
		n = Integer.parseInt( fin.readLine() ); //������� ����� ������
		int edges = Integer.parseInt( fin.readLine() ); //������� ����� �����
		dl = new int[MAX];	//������� ������ �������
		p = new int[MAX];
		vert = new Vert[MAX];	//������ �� ��������� ������
		for (int i = 0; i < MAX; i++)
			vert[i] = new Vert();
		
		e = null; 		//������ �����
		alg = false; 	//�������� �� �������

		String tmp = "";
		String val = "";
		for (int i = 0; i < edges; i++) //���������� ������ �����
		{
			tmp = fin.readLine();
			int len = tmp.length();
			int j1 = 0;
			int j2 = 0;
			while ((tmp.charAt(j2) != ' ') && (j2 < len-1))
			{
				j2++;
			}
			//j1 = ������ �����
			//j2 = ������ �������-1
			val = tmp.substring(0, j2);
			x = Integer.parseInt(val);		//������ �������
			
			j1 = ++j2;
			//���������� ������ �������
			while ((tmp.charAt(j2) != ' ') && (j2 < len-1))
			{
				j2++;
			}
			val = tmp.substring(j1, j2);
			y = Integer.parseInt(val);		//������ �������
			
			j1 = ++j2;
			//����������� ����
			while ((j2 < len) && (tmp.charAt(j2) != ' '))
			{
				j2++;
			}
			val = tmp.substring(j1, j2);
			z = Integer.parseInt(val);		//���
			
			addEdge(x, y, z);	//���������� �����
		}
		
		/*������������ ��������� ������
		vert[0].x = 500;		//� ��� �������
		vert[0].y = 70;			//� ��� �������
		double dx, dy;
		for (int i = 1; i < n; i++)
		{//radius = 450
			dx = 450*Math.cos(2*Math.PI / i*n);
			dy = 450*Math.sin(2*Math.PI / i*n);
			vert[i].x = vert[i-1].x + (int)dx;
			vert[i].y = vert[i-1].y + (int)dy;
		}
		//���� �� ��������, �� ��������
		*/
		int CentrX=250, CentrY=250;
		double angle = 6.2831853/n; //��������� ������������ ������
		double nextangle = 0;
		int R = 200;
		int x_c, y_c;
		for(int i = 0; i<n; i++){
			x_c = (int) (CentrX + Math.cos(nextangle)*R);
			y_c = (int) (CentrY + Math.sin(nextangle)*R);
			nextangle += angle;
			vert[i].set(x_c,y_c);
		}
		fin.close();
	}
	
	public void addVert() 						//���������� ����� ������� � ����
	{
		if (n >= MAX) return;
		dl[n] = INF;
		n++;
	}
	public void addVert(int x, int y) 			//���������� ������� � ����, x,y - ���������� �������
	{
		if (n >= MAX) return;
		dl[n] = INF;
		vert[n].set(x,y);
		vert[n].setColor(0);
		n++;
	}
	public void addEdge(int u, int v, int w) 	//���������� ����� � ����
	{
		u--; v--; 		//������� ��������� ���������� � 1
		if (e == null)  //���� ������ ����
		{ 	
			e = new Node(u,v,w);
			m++;
			return;
		}
		
		Node newNode = e;  //������ �� ������ ������ � ������

		//���� �� ����� ������
		while (newNode.next != null )
		{	
			if ( newNode.a == u && newNode.b == v) return; //����� ������������ �������
			newNode = newNode.next;
		}
		
		if (newNode.next == null)
		{
			newNode.next = new Node(u,v,w); //������� ������
			m++;
		}
	}

	public void draw_dl() 						//����� ������� �� �������
	{
		for (int i = 0; i < n; i++)
			if (dl[i] < INF)
				System.out.print(dl[i] + "  ");
			else
				System.out.print(" inf ");
		//System.out.println();
	}
	public void drawGraph(Graphics g1) 			//����� ����� �� �������
	{
		//super.paintComponents(g);
		Graphics2D g = (Graphics2D)g1;
		//����� �������� �� �����-����� ��������� ������
		//g.drawOval(10, 10, 50, 50);
		Font myFont = new Font("Arial", Font.BOLD, 16);
		g.setFont(myFont); 			//������������� �����
		g.setStroke(new BasicStroke(2) );
		String myStr = "";
		/*���������� ������*/
		for (int i = 0; i < n; i++)
		{
			int x = vert[i].x - rad;
			int y = vert[i].y - rad;
			myStr = Integer.toString(i+1);
			
			switch(vert[i].color)
			{
			case 1:		//�������� �����
				g.setColor(Color.BLUE);
				break;
			case 2:		//�������� �������
				g.setColor(Color.RED);
				break;
			case 3:		//�������� �������
				g.setColor(Color.GREEN);
				break;
			default: //�������� ������
				g.setColor(Color.BLACK);
				break;
			}
			
			g.drawOval(x, y, 2*rad, 2*rad);			//��������� � ������� �� �������� � 2 �������
			g.drawString(myStr, x + 15 , y + 20 );	//������ ������ ����� �������
			
			if (alg)		//���� �������� �������
			{//������ ������� ��� ���������
				g.setColor(Color.RED);
				if (dl[i] < INF) myStr = Integer.toString(dl[i]);
				else myStr = "inf";
				g.drawString(myStr, x + 15, y + 5);
			}
		}
		/*���������� �����*/
		for (int j = 0; j < m; j++)
		{
			int x1,y1,x2,y2;
			/*1 ����� (p1)*/
			x1 = vert[ e.getNode(j).a ].x;
			y1 = vert[ e.getNode(j).a ].y;
			/*2 ����� (p2)*/
			x2 = vert[ e.getNode(j).b ].x;
			y2 = vert[ e.getNode(j).b ].y;
			
			//����������� ����� ������ � ����� �����
			if ( x1 <= x2) //p1 ����� �� p2
			{
				if (y1 <= y2) //p1 ���� p2
				{
					y1 += rad;
					x2 -= rad;
				}
				else
				{//(p1.Y > p2.Y)   //p1 ���� p2
					y1 -= rad;
					x2 -= rad;
				}
			}
			else
			// (p1.X > p2.X) //p1 ������ �� p2
			{
				if (y1 < y2)    //p1 ���� p2
				{
					y1 += rad;
					x2 += rad;
				}
				else  
				{//(p1.Y > p2.Y)   //p1 ���� p2
					y1 -= rad;
					x2 += rad;
				}
			}
			switch( e.getNode(j).color )
			{
			case 1:		//�������� �����
				g.setColor(Color.CYAN);
				break;
			case 2:		//�������� �������
				g.setColor(Color.RED);
				break;
			case 3:		//�������� �������
				g.setColor(Color.GREEN);
				break;
			default: //�������� ������
				g.setColor(Color.BLACK);
				break;
			}
			g.drawLine(x1, y1, x2, y2); //���������� �������
			myStr = Integer.toString(e.getNode(j).w);
			int x = Math.abs(x2 + x1)/2;
			int y = Math.abs(y2 + y1)/2;
			g.drawString(myStr, x, y);
			/*�������*/
			Polygon a = new Polygon();
			a.addPoint(x2, y2);
			double beta = Math.atan2(y1 - y2, x2 - x1); 
			double alfa = Math.PI/10;
			int len = 15; //�����
			x = (int)Math.round(x2 - len*Math.cos(beta+alfa));
			y = (int)Math.round(y2 + len*Math.sin(beta+alfa));
			//g.drawLine(x2, y2, x, y);
			a.addPoint(x, y);
			x = (int)Math.round(x2 - len* Math.cos(beta - alfa));
			y = (int)Math.round(y2 + len* Math.sin(beta - alfa));
			a.addPoint(x, y);
			
			g.fillPolygon(a);
			

			
		}
	}
	
	public int isInVert(int X, int Y)			//���������� ����� ��������� �������
	{
		double dist = 0;
		for (int i = 0; i < n; i++)
		{
			dist = Math.sqrt(Math.pow( (vert[i].x - X) ,2) + Math.pow( (vert[i].y - Y),2));
			if ( (int)dist < rad )
			{
				return i+1;
			}
		}
		return 0;
	}
	public boolean isToClose(int X, int Y)		//true - ����� ������� ������ � ������ �������
	{
		if (n == 0) return false;
		double dist = 0;
		for (int i = 0; i < n; i++)
		{
			dist = Math.sqrt(Math.pow( (vert[i].x - X) ,2) + Math.pow( (vert[i].y - Y),2 ));
			if ( (int)dist < 70 )
			{
				return true;
			}
		}
		return false;
	}
	
	public int[] get_table() 	//���������� ������ �� ������ �������
	{
		return dl;
	}
	public String getEdge()			//���������� ������ ����� � ���� ������
	{
		String res = "";
		Node ptr = e;
		while (ptr != null)
		{
			res += "[ " + (ptr.a + 1) + ", " + (ptr.b + 1) + " ], ���=" + ptr.w + "\n"; 
			ptr = ptr.next;
		}
		return res;
	}
	public int getN()				//���������� ����� ������
	{
		return n;
	}
	public String getEdges()		//���������� �������������� �����
	{
		String res = "[ " + (e.getNode(consEdge).a+1) + ", " + (e.getNode(consEdge).b+1) + " ]";
		return res;
	}
	
	public int getStep()			//���������� ����� ����
	{
		return k;
	}
	
	public boolean isRunning()
	{
		return alg;
	}

	public boolean isNegativeCycle(int source)		//true - � ����� ������������� ���� 
	{
		/*�������������*/
		for (int i = 0; i < n; i++)
			dl[i] = INF;
		dl[source - 1] = 0;					
		
		/*��������� �������� �� n-1 ���*/
		for (int i = 0; i < n-1; i++)
		{
			for (int j = 0; j < m; j++)
				if (dl[ e.getNode(j).a] < INF )
				{
					dl[ e.getNode(j).b ] =  
					min( dl[ e.getNode(j).b ], (dl [ e.getNode(j).a ] + e.getNode(j).w)); // ����������
					//������ �������� any !!�� �������!!
				}
		}
		/*��������� �������� �� n-�� ���, ���� ���-�� ����������, �� � ����� ������������� ����*/
		any = false;
		for (int j = 0; j < m; j++)
			if (dl[ e.getNode(j).a] < INF )
			{
				dl[ e.getNode(j).b ] =  
				min( dl[ e.getNode(j).b ], (dl [ e.getNode(j).a ] + e.getNode(j).w)); // ����������
				//������ �������� any !!�� �������!!
			}
		return any; //any == true, ����� ���� ���������.
	}
	
	public boolean step() 		//��� ��������� �-�.
	{
		vert[ e.getNode(consEdge).b ].setColor(0);
		e.getNode(consEdge).setColor(0);
		consEdge = v; //��������� ����� ���������������� �����
		vert[ e.getNode(v).b ].setColor(2);
		e.getNode(v).setColor(2);	//�������� � ������� ����
		if (v < m) 
		{
			if (dl[ e.getNode(v).a] < INF )
			{
				
				dl[ e.getNode(v).b ] =  
				min( dl[ e.getNode(v).b ], (dl [ e.getNode(v).a ] + e.getNode(v).w)); // ����������
				//������ �������� any !!�� �������!!
			}
			v++;
			if (v >= m)
			{//������� ���������� �� ���� ������ (��������� ���)
				k++;
				v = 0;
				
				if (any && (k < n-1)) 
				{//any = true -> ���-�� ����������, �������� �� ��������
					any = false;
					return false;
				}
				
				return true;
				//���� any = false -> ��������� �����������, �������� ����� �����
			}
		}
		return false; // �������� �� ��������
	}
	public void Ford_Bellman(int source)
	{
		any = true;
		alg = true;
		s = --source;
		k = 0;
		v = 0;
		for (int i = 0; i < n; i++)
			dl[i] = INF;
		
		for (int i = 0; i < m; i++)
			if (e.getNode(i).a == s) dl[ e.getNode(i).b ] = e.getNode(i).w;

		vert[s].setColor(1);
		dl[source] = 0;
	}
	
	public int min(int a, int b)
	{
		if (a <= b) return a;
		any = true;
		return b; 
	}
	
	public void setColor(int v, int new_color)
	{
		v--;
		vert[v].color = new_color;
	}
}

