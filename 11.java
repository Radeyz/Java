package myFrame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.lang.Math;

import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class mainClass {
	static Graph g;// = null;// = new Graph();
	static int select1 = 0, select2 = 0, select3 = 0;
	static boolean buttonRFFPressed = true;
    public static void main(String[] args) throws ClassNotFoundException,IOException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
    	
    	javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        myFrame jf = new myFrame();
        jf.createFrame();
    }
    

    //*******************************************************************************************

    //

    public static class myFrame {

        JFrame mainFrame = new JFrame("Алгоритм Форда - Беллмана");
        String labelstr = new String("1");
        JTextArea textAr = new JTextArea();
        

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable();
        JPanel leftPanel = new JPanel();
        //Graph leftPanel = new Graph();	//FACEPALM
        JPanel rightPanel = new JPanel();
        JToolBar upperPanel = new JToolBar();
        JButton b_nextStep = new JButton("Старт");
        JButton button1 = new JButton("Загрузить");
        JButton button2 = new JButton("Нарисовать");
        JButton button3 = new JButton("Справка");
        JButton button4 = new JButton("Сбросить");
        
        Border bord =  BorderFactory.createLineBorder(Color.black, 1);        
        public  void createFrame(){
        	mainFrame.setResizable(false);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.setVisible(true);
            addMainPanels();

            textAr.setFont(new Font("Arial", 0, 14));
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
            	b_nextStep.setText("Старт");
            	button1.setEnabled(true);
            	button2.setEnabled(true);
            	buttonRFFPressed = false;
            	leftPanel.repaint();
            }
        }
        //Action listener to start and step
        public class ButtonAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
         
            	String name = b_nextStep.getText();
            	if (name.equals("Старт"))
            	{
            		model.addColumn("Шаг");
                    for(int i= 0;i<g.getN();i++){
                    	model.addColumn(""+(i+1));
                    }
            		int source = Integer.parseInt(JOptionPane.showInputDialog("Введите исходную вершину"));
            		if (g.isNegativeCycle(source)) 
                		JOptionPane.showMessageDialog(null, "В графе есть отрицательный цикл"
                				+ "\n Будет выполнено n-1 шагов", "Загрузка", JOptionPane.INFORMATION_MESSAGE);	
            		g.Ford_Bellman(source);
            		button1.setEnabled(false);
            		button2.setEnabled(false);
            	}
                int i = 0;
                
                int[] dl = g.get_table();
                String[] _dl= new String[dl.length+1];
                textAr.replaceRange("", 0, textAr.getSelectionEnd());
                textAr.insert("Шаг " + labelstr + "\n", textAr.getSelectionEnd());
                textAr.insert("Рассматриваемое ребро: "+g.getEdges()+"\n",textAr.getSelectionEnd() );
                textAr.insert("Список ребер:\n", textAr.getSelectionEnd());
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
                
                //g.drawGraph(leftPanel.getGraphics());
                //table.add(_dl[i], null);
                if ( g.step()){
                	for (int j = 0; j < g.getM(); j++)
                	{
                		g.setEdgeColor(j, 0);
                	}
                	for (int j = 1; j <= g.getN(); j++)
                		g.setColor(j, 0);
                	
                    b_nextStep.setText("Конец алгоритма");
                    b_nextStep.setEnabled(false);
                }
                else{
                    b_nextStep.setText("Следующий шаг");
                }
                labelstr = Integer.toString(++i);
                g.drawGraph(leftPanel.getGraphics());
               
            }
        }
        //Action listener to read from file
        public class ButtonRFF implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	try{
            		
            		JFileChooser fileopen = new JFileChooser();
            		int ret = fileopen.showDialog(null, "Открыть файл");
            		if (ret == JFileChooser.APPROVE_OPTION) {
	            		File file = fileopen.getSelectedFile();
	            		
	            		g = new Graph( file );
	            		
	            		JOptionPane.showMessageDialog(null, "Данные загружены.", "Загрузка", JOptionPane.INFORMATION_MESSAGE);
	                    buttonRFFPressed = true;
	                    button2.setEnabled(false);
	                    /*
	                    model.addColumn("Шаг");
	                    for(int i= 0;i<g.getN();i++){
	                    	model.addColumn(""+(i+1));
	                    }
	                    */
	                    textAr.setFont(new Font("ARIAL", 0, 14));
	                    textAr.insert("Список ребер:\n", textAr.getSelectionEnd());
	                    textAr.insert(g.getEdge(), textAr.getSelectionEnd());
                   // table = new JTable(model);
            		}
                    //String [] linesAsArray = lines.toArray(new String[lines.size()]);
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "Файл не найден.", "Загрузка", JOptionPane.WARNING_MESSAGE);

                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            	
            		
            }
        }

        //Action listener to input date
        public class ButtonID implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(null, "Чтобы нарисовать вершину кликните два раза по белой области\n"
            			+ "Чтобы соединить вершины выберите две вершины\n"
            			+ "Выбрать вершину можно кликнув по ней, она окраситься в синий цвет", "Помощь",JOptionPane.INFORMATION_MESSAGE );
                button1.setEnabled(false);
                buttonRFFPressed = false;
            }
        }
        //Action listener to help
        public class ButtonHELP implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                String HELP = "Кнопка \"Загрузить\" загружает введеные заранее данные;\n\nКнопка \"Нарисовать\"предоставляет возможность Вам вручную задать граф;\n\nКнопка \"Старт\" начинает выполнение алгоритма.";
                JOptionPane.showMessageDialog(null, HELP, "Справка", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        public void addMainPanels(){
            leftPanel.setBackground(Color.white);
            //leftPanel.setPreferredSize(new Dimension(1100 - 350, 950));
            leftPanel.addMouseListener(new CustomListener());
            leftPanel.addMouseMotionListener(new CustomListener2());
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
            model.addColumn("Шаг");
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
        public class CustomListener2 implements MouseMotionListener
        {
        	public void mouseMoved(MouseEvent e)
        	{
        		g.drawGraph(leftPanel.getGraphics());
        	}
        	public void mouseDragged(MouseEvent e){};
        }
        public class CustomListener implements MouseListener
        {
        	public void mouseClicked(MouseEvent e)
        	{
        		if (buttonRFFPressed)
        		{
        			g.drawGraph(leftPanel.getGraphics());
        			return;
        		}
        		
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
        		{//добавление новой вершины
        			if (!g.isToClose(x, y))
        				g.addVert(x,y);
        			else 
        				JOptionPane.showMessageDialog(null, "Слишком близко к другим вершинам.", "Ввод", JOptionPane.INFORMATION_MESSAGE);
        		}
        		else if (i != 0)
        		{
	        		if (e.getButton() == MouseEvent.BUTTON1)
	        		{//выбор вершины
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
	        					w = Integer.parseInt(JOptionPane.showInputDialog("Введите вес вершины"));
	        				} 
	        				catch (NumberFormatException exptn)
	        				{
	            				JOptionPane.showMessageDialog(null, "Нужно ввести число.", "Ошыбка", JOptionPane.INFORMATION_MESSAGE);	
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
	static int INF = 1400;		//число, принимаемое за бесконечность
	static int MAX = 50;		//макс. число вершин
	static int rad = 20;		//радиус вершины
	int n; 			//число вершин
	int m; 			//число ребер
	int k, u, v; 	//счетчика для алгоритма ФБ.
	int dl[]; 		//массив из временных пометок
	int consEdge;   //номер рассматриваемого ребра при алгоритме ФБ
	int s; 			//исходная вершина
	//int p[]; 		//для восстановления путей
	boolean any, alg;	//для алгоритма ФБ\
	boolean changes;
	Node e; 		//ссылка на начало списка ребер
	Vert vert[];		//массив из вершин
	
	class Node 			//класс - список ребер.
	{
		int a, b, w; 	//a->b, weight = w
		int color;		//цвет ребра
		Node next; 		//указатель на следующий эл. списка
		
		public Node(int u, int v, int weight)  //создание ребра u->v с весом weight, цвет = 0
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
		public Node getNode(int i)	//возвращает эл. с номером i, если такого нет, то null
		{
			int j = 0;		//счетчик
			Node p = e; 	//ссылка на начало списка
			while ( (j < i) && (p != null))
			{
				j++;
				p = p.next;
			}
			return p;
		}
	};
	class Vert 		//класс - точка
	{
		int x,y; //координаты
		int color; //цвет вершины
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
	public Graph() 		//конструктор
	{
		n = 0;
		m = 0;
		dl = new int[MAX];
		//p = new int[MAX];
		vert = new Vert[MAX];
		for (int i = 0; i < MAX; i++)
			vert[i] = new Vert();
		e = null;
		alg = false; //алгоритм не запущем
		changes = false;
	}
	public Graph(int number) //конструктор
	{
		n = number;
		m = 0;
		dl = new int[MAX];
		//p = new int[MAX];
		vert = new Vert[MAX];
		for (int i = 0; i < MAX; i++)
			vert[i] = new Vert();
		
		e = null;
		alg = false; //алгоритм не запущем
		changes = false;
	}
	
	public Graph(File file)
	throws IOException
	{
		int x, y, z; //для считывания из файла
		BufferedReader fin = new BufferedReader (new FileReader(file));
		n = Integer.parseInt( fin.readLine() ); //считали число вершин
		int edges = Integer.parseInt( fin.readLine() ); //считали число ребер
		dl = new int[MAX];	//создали массив пометок
		//p = new int[MAX];
		vert = new Vert[MAX];	//массив из координат вершин
		for (int i = 0; i < MAX; i++)
			vert[i] = new Vert();
		
		e = null; 		//список ребер
		alg = false; 	//алгоритм не запущем
		changes = false;
		String tmp = "";
		String val = "";
		for (int i = 0; i < edges; i++) //считывание списка ребер
		{
			tmp = fin.readLine();
			int len = tmp.length();
			int j1 = 0;
			int j2 = 0;
			while ((tmp.charAt(j2) != ' ') && (j2 < len-1))
			{
				j2++;
			}
			//j1 = начало числа
			//j2 = индекс пробела-1
			val = tmp.substring(0, j2);
			x = Integer.parseInt(val);		//первая вершина
			
			j1 = ++j2;
			//считывание второй вершины
			while ((tmp.charAt(j2) != ' ') && (j2 < len-1))
			{
				j2++;
			}
			val = tmp.substring(j1, j2);
			y = Integer.parseInt(val);		//вторая вершина
			
			j1 = ++j2;
			//считываение веса
			while ((j2 < len) && (tmp.charAt(j2) != ' '))
			{
				j2++;
			}
			val = tmp.substring(j1, j2);
			z = Integer.parseInt(val);		//вес
			
			addEdge(x, y, z);	//добавление ребра
		}
		
		/*Формирование координат вершин
		vert[0].x = 500;		//я так захотел
		vert[0].y = 70;			//я так захотел
		double dx, dy;
		for (int i = 1; i < n; i++)
		{//radius = 450
			dx = 450*Math.cos(2*Math.PI / i*n);
			dy = 450*Math.sin(2*Math.PI / i*n);
			vert[i].x = vert[i-1].x + (int)dx;
			vert[i].y = vert[i-1].y + (int)dy;
		}
		//если не работает, то ожидаемо
		*/
		int CentrX=250, CentrY=250;
		double angle = 6.2831853/n; //вычиление расположения вершин
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
	
	public void addVert() 						//добавление новой вершины в граф
	{
		if (n >= MAX) return;
		dl[n] = INF;
		n++;
	}
	public void addVert(int x, int y) 			//добавление вершины в граф, x,y - координаты вершины
	{
		if (n >= MAX) return;
		dl[n] = INF;
		vert[n].set(x,y);
		vert[n].setColor(0);
		changes = true;
		n++;
	}
	public void addEdge(int u, int v, int w) 	//добавление ребра в граф
	{
		if (u == v) return;
		u--; v--; 		//внешняя нумерация начинается с 1
		changes = true;
		if (e == null)  //если список пуст
		{ 	
			e = new Node(u,v,w);
			m++;
			return;
		}
		
		Node newNode = e;  //ссылку на список ставим в начало

		//идем до конца списка
		while (newNode.next != null )
		{	
			if ( newNode.a == u && newNode.b == v) return; //нашли существующую вершину
			newNode = newNode.next;
		}
		
		if (newNode.next == null)
		{
			newNode.next = new Node(u,v,w); //создаем запись
			m++;
		}
	}

	public void draw_dl() 						//вывод пометок на консоль
	{
		for (int i = 0; i < n; i++)
			if (dl[i] < INF)
				System.out.print(dl[i] + "  ");
			else
				System.out.print(" inf ");
		//System.out.println();
	}
	public void drawGraph(Graphics g1) 			//вывод ребер на консоль
	{
		g1.setColor(Color.WHITE);
		if (changes)
			g1.fillRect(0, 0, 1100-350, 900);
		changes = false;
		Graphics2D g = (Graphics2D)g1;
		//нужно рисовать на белом-белом покрывале января
		//g.drawOval(10, 10, 50, 50);
		Font myFont = new Font("Arial", Font.BOLD, 16);
		g.setFont(myFont); 			//устанавливаем шрифт
		g.setStroke(new BasicStroke(2) );
		String myStr = "";
		/*прорисовка вершин*/
		for (int i = 0; i < n; i++)
		{
			int x = vert[i].x - rad;
			int y = vert[i].y - rad;
			myStr = Integer.toString(i+1);
			
			switch(vert[i].color)
			{
			case 1:		//рисовать синим
				g.setColor(Color.BLUE);
				break;
			case 2:		//рисовать красным
				g.setColor(Color.RED);
				break;
			case 3:		//рисовать зеленым
				g.setColor(Color.GREEN);
				break;
			default: //рисовать черным
				g.setColor(Color.BLACK);
				break;
			}
			
			g.drawOval(x, y, 2*rad, 2*rad);			//вписываем в квадрат со стороной в 2 радиуса
			g.drawString(myStr, x + 15 , y + 20 );	//рисуем внутри номер вершины
			
			if (alg)		//если алгоритм запущен
			{//рисуем пометки над вершинами
				g.setColor(Color.RED);
				if (dl[i] < INF) myStr = Integer.toString(dl[i]);
				else myStr = "inf";
				g.drawString(myStr, x + 15, y - 10);
			}
		}
		/*прорисовка ребер*/
		for (int j = 0; j < m; j++)
		{
			int x1,y1,x2,y2;
			/*1 точка (p1)*/
			x1 = vert[ e.getNode(j).a ].x;
			y1 = vert[ e.getNode(j).a ].y;
			/*2 точка (p2)*/
			x2 = vert[ e.getNode(j).b ].x;
			y2 = vert[ e.getNode(j).b ].y;
			
			//расстановка точек начала и конца ребер
			if ( x1 <= x2) //p1 слева от p2
			{
				if (y1 <= y2) //p1 ниже p2
				{
					y1 += rad;
					x2 -= rad;
				}
				else
				{//(p1.Y > p2.Y)   //p1 выше p2
					y1 -= rad;
					x2 -= rad;
				}
			}
			else
			// (p1.X > p2.X) //p1 справа от p2
			{
				if (y1 < y2)    //p1 ниже p2
				{
					y1 += rad;
					x2 += rad;
				}
				else  
				{//(p1.Y > p2.Y)   //p1 выше p2
					y1 -= rad;
					x2 += rad;
				}
			}
			switch( e.getNode(j).color )
			{
			case 1:		//рисовать синим
				g.setColor(Color.CYAN);
				break;
			case 2:		//рисовать красным
				g.setColor(Color.RED);
				break;
			case 3:		//рисовать зеленым
				g.setColor(Color.GREEN);
				break;
			default: //рисовать черным
				g.setColor(Color.BLACK);
				break;
			}
			g.drawLine(x1, y1, x2, y2); //нарисовать стрелку
			myStr = Integer.toString(e.getNode(j).w);
			int x = Math.abs(x2 + x1)/2;
			int y = Math.abs(y2 + y1)/2;
			g.drawString(myStr, x + 15, y + 15);
			/*стрелка*/
			Polygon a = new Polygon();
			a.addPoint(x2, y2);
			double beta = Math.atan2(y1 - y2, x2 - x1); 
			double alfa = Math.PI/10;
			int len = 15; //длина
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
	
	public int isInVert(int X, int Y)			//возвращает номер выбранной вершины
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
	public boolean isToClose(int X, int Y)		//true - точка слишком близко к другой вершине
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
	
	public int[] get_table() 	//возвращает ссылку на массив пометок
	{
		return dl;
	}
	public String getEdge()			//возвращает список ребер в виде строки
	{
		String res = "";
		Node ptr = e;
		while (ptr != null)
		{
			res += "[ " + (ptr.a + 1) + ", " + (ptr.b + 1) + " ], вес=" + ptr.w + "\n"; 
			ptr = ptr.next;
		}
		return res;
	}
	public int getN()				//возвращает число вершин
	{
		return n;
	}
	public String getEdges()		//возвращает просмативаемое ребро
	{
		String res = "[ " + (e.getNode(consEdge).a+1) + ", " + (e.getNode(consEdge).b+1) + " ]";
		return res;
	}
	
	public int getStep()			//возвращает номер шага
	{
		return k;
	}
	
	public boolean isRunning()
	{
		return alg;
	}
	public int getM()	//возвращает число ребер
	{
		return m;
	}

	public boolean isNegativeCycle(int source)		//true - в графе отрицательный цикл 
	{
		/*Инициализация*/
		for (int i = 0; i < n; i++)
			dl[i] = INF;
		dl[source - 1] = 0;					
		
		/*Выполняем алгоритм ФБ n-1 раз*/
		for (int i = 0; i < n-1; i++)
		{
			for (int j = 0; j < m; j++)
				if (dl[ e.getNode(j).a] < INF )
				{
					dl[ e.getNode(j).b ] =  
					min( dl[ e.getNode(j).b ], (dl [ e.getNode(j).a ] + e.getNode(j).w)); // релаксация
					//ВНУТРИ МЕНЯЕТСЯ any !!НЕ ТРОГАТЬ!!
				}
		}
		/*Выполняем алгоритм ФБ n-ый раз, если что-то изменилось, то в графе отрицательный цикл*/
		any = false;
		for (int j = 0; j < m; j++)
			if (dl[ e.getNode(j).a] < INF )
			{
				dl[ e.getNode(j).b ] =  
				min( dl[ e.getNode(j).b ], (dl [ e.getNode(j).a ] + e.getNode(j).w)); // релаксация
				//ВНУТРИ МЕНЯЕТСЯ any !!НЕ ТРОГАТЬ!!
			}
		return any; //any == true, когда есть изменение.
	}
	
	public boolean step() 		//шаг алгоритма ф-б.
	{
		changes = true;
		vert[ e.getNode(consEdge).b ].setColor(0);
		e.getNode(consEdge).setColor(0);
		consEdge = v; //сохранили номер рассматриваемого ребра
		vert[ e.getNode(v).b ].setColor(2);
		e.getNode(v).setColor(2);	//окрасили в красный цвет
		if (v < m) 
		{
			if (dl[ e.getNode(v).a] < INF )
			{
				
				dl[ e.getNode(v).b ] =  
				min( dl[ e.getNode(v).b ], (dl [ e.getNode(v).a ] + e.getNode(v).w)); // релаксация
				//ВНУТРИ МЕНЯЕТСЯ any !!НЕ ТРОГАТЬ!!
			}
			v++;
			if (v >= m)
			{//Провели релаксацию по всем ребрам (закончили шаг)
				
				k++;
				v= 0;	
				if (any && (k < n-1)) 
				{//any = true -> что-то поменялось, алгоритм не закончен
					any = false;
					return false;
				}
				v = 0;
				return true;
				//если any = false -> состояние сохранилось, алгоритм нашел ответ
			}
		}
		return false; // алгоритм не закончен
	}
	public void Ford_Bellman(int source)
	{
		changes = true;
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
		changes = true;
		v--;
		vert[v].color = new_color;
	}
	public void setEdgeColor(int v, int new_color)
	{
		changes = true;
		e.getNode(v).setColor(new_color);
	}
}

