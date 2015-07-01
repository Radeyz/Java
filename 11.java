import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class mainclass {
	static Graph g ;
	//static Canvas c = new Canvas();
	static BufferedImage img; 
	static Graphics graphics;
	static int WIDTH = 50;
	static int HEIGHT = 50;
	
    public static void main(String[] args) throws ClassNotFoundException,IOException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
    	
    	javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        myFrame jf = new myFrame();
        jf.createFrame();
    }
    
    
   
    
    //

    //*******************************************************************************************

    //

    public static class myFrame {

        JFrame mainFrame = new JFrame("Алгоритм Форда - Беллмана");
        String labelstr = new String("1");
        JTextArea textAr = new JTextArea();
        

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable();
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();		//Штука на которой рисуем
        JToolBar upperPanel = new JToolBar();
        JButton b_nextStep = new JButton("Старт");
        JButton button1 = new JButton("Загрузка из файла");
        JButton button2 = new JButton("Ввести граф");
        JButton button3 = new JButton("Справка");
        JButton button4 = new JButton("Сбросить");
        
        Border bord =  BorderFactory.createLineBorder(Color.black, 1);
        public  void createFrame(){
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.setVisible(true);
            addMainPanels();

            mainFrame.setSize(1100, 950);
            
            /*Параметры BufferedImage*/
            //int hgt = rightPanel.getHeight();		//высота rightPanel
            //int wdt = rightPanel.getWidth();		//ширина rightPanel
            int hgt = 400;
            int wdt = 400;
            img = new BufferedImage(wdt,hgt, BufferedImage.TYPE_INT_RGB);
            /*Иницилизация графики*/
            graphics = rightPanel.getGraphics();
        }
        
        public class DRAW_GRAPH implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	 return;
            }
            public void MouseClick (MouseEvent e)
            {
            	int x,y;
            	x = e.getX();
            	y = e.getY();
            	g.addVert(x, y);
            	graphics.drawOval(x, y, WIDTH, HEIGHT);
            }
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
            }
        }
        //Action listener to start and step
        public class ButtonAction implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	String name = b_nextStep.getText();
            	if (name.equals("Старт"))
            		g.Ford_Bellman(Integer.parseInt(JOptionPane.showInputDialog("Введите исходную вершину")));
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

                //table.add(_dl[i], null);
                if ( g.step()){
                	
                    b_nextStep.setText("Конец алгоритма");
                    b_nextStep.setEnabled(false);
                }
                else{
                    b_nextStep.setText("Следующий шаг");
                }
                labelstr = Integer.toString(++i);
               
            }
        }
        //Action listener to read from file
        public class ButtonRFF implements ActionListener {
            public void actionPerformed(ActionEvent e) {
            	try{
            		g = new Graph("Z:\\3304\\вихляев_ооп\\практика\\MyGUI\\src\\myFrame\\in.txt");
            		JOptionPane.showMessageDialog(null, "Данные загружены.", "Загрузка", JOptionPane.INFORMATION_MESSAGE);
                    
                    model.addColumn("Шаг");
                    for(int i= 0;i<g.getN();i++){
                    	model.addColumn(""+(i+1));
                    }
                    textAr.setFont(new Font("ARIAL", 0, 14));
                    textAr.insert("Список ребер:\n", textAr.getSelectionEnd());
                    textAr.insert(g.getEdge(), textAr.getSelectionEnd());
                   // table = new JTable(model);

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
                String strU = new String();
                String strV = new String("0");
                strV = JOptionPane.showInputDialog("Введите количество вершин");
                while(Integer.parseInt(strV) >50){
                	JOptionPane.showMessageDialog(null, "Большое количество вершин. Введите количество вершин меньше 50", "Ввод", JOptionPane.WARNING_MESSAGE);
                	strV = JOptionPane.showInputDialog("Введите количество вершин");
                };
                g = new Graph(Integer.parseInt(strV));
                if (!strV.equals("")&&!strV.equals("0")) {
                    strU = JOptionPane.showInputDialog("Введите количество ребер");
                }
                if (!strU.equals("")) {
                    String[] ribs = new String[Integer.valueOf(strU)];
                    for (int i = 0; i < Integer.valueOf(strU); i++) {
                        ribs[i] = JOptionPane.showInputDialog("Введите ребро в виде [u, v, w], где \nu->v, w[u,v]");
                    }
                    int x, y, z;
                    String tmp = "";
            		String val = "";
            		for (int i = 0; i < Integer.parseInt(strU); i++) //считывание списка ребер
            		{
            			tmp = ribs[i];
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
            			
            			g.addEdge(x, y, z);	//добавление ребра
            		}
                }
                model.addColumn("Шаг");
                for(int i= 0;i<g.getN();i++){
                	model.addColumn(""+(i+1));
                }
                textAr.setFont(new Font("ARIAL", 0, 14));
                textAr.insert("Список ребер:\n", textAr.getSelectionEnd());
                textAr.insert(g.getEdge(), textAr.getSelectionEnd());
                JOptionPane.showMessageDialog(null, "Ввод окончен.", "Ввод", JOptionPane.INFORMATION_MESSAGE);

            }
        }
        //Action listener to help
        public class ButtonHELP implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                String HELP = "Кнопка \"Загрузка из файла\" загружает введеные заранее данные;\nКнопка \"Ввести граф\"предоставляет возможность Вам вручную ввести данные;\nКнопка \"Старт\" начинает выполнение алгоритма.";
                JOptionPane.showMessageDialog(null, HELP, "Справка", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        public void addMainPanels(){
            leftPanel.setBackground(Color.white);

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
        
            
            JScrollPane scroll_text = new JScrollPane(textAr,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            
			JScrollPane scroll_table = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
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
        
    }

    //

    //******************************************************************************************

    //

}
class Graph
{
	static int INF = 1400;
	static int MAX = 50;
	int n; 			//число вершин
	int m; 			//число ребер
	int k, u, v; 	//счетчика для алгоритма ФБ.
	int dl[]; 		//массив из временных пометок
	int consEdge;   //номер рассматриваемого ребра при алгоритме ФБ
	int s; 			//исходная вершина
	boolean any;	//для алгоритма ФБ
	Node e; 		//ссылка на начало списка ребер
	Point p[];	//массив из координат вершин
	
	class Node 			//класс - список ребер.
	{
		int a, b, w; 	//a->b, weight = w
		Node next; 		//указатель на следующий эл. списка
		
		public Node(int u, int v, int weight)  //создание ребра u->v с весом weight
		{
			a = u;
			b = v;
			w = weight;
			next = null;
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
	class Point 		//класс - точка
	{
		int x,y; //координаты
		public Point(int X, int Y)
		{
			x = X;
			y = Y;
		}
		public Point()
		{
			x = y = 0;
		}
		public void set(int newX, int newY)
		{
			x = newX;
			y = newY;
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
		p = new Point[MAX];
		e = null;
	}
	public Graph(int number) //конструктор
	{
		n = number;
		m = 0;
		dl = new int[MAX];
		p = new Point[MAX];
		e = null;
	}
	
	public Graph(String filename)
	throws IOException
	{
		int x, y, z; //для считывания из файла
		BufferedReader fin = new BufferedReader (new FileReader(filename));
		n = Integer.parseInt( fin.readLine() ); //считали число вершин
		int edges = Integer.parseInt( fin.readLine() ); //считали число ребер
		dl = new int[MAX];	//создали массив пометок
		p = new Point[MAX];	//массив из координат вершин
		e = null; 		//список ребер
		
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
		addVert();
		p[n].set(x,y);		
	}
	public void addEdge(int u, int v, int w) 	//добавление ребра в граф
	{
		u--; v--; 		//внешняя нумерация начинается с 1
		if (e == null)  //если список пуст
		{ 	
			e = new Node(u,v,w);
			m++;
			return;
		}
		
		Node newNode = e;  //ссылку на список ставим в начало

		//идем до конца списка
		while (newNode.next != null)
			newNode = newNode.next;

		newNode.next = new Node(u,v,w); //создаем запись
		m++;
		return;
	}

	public void draw_dl() 			//вывод пометок на консоль
	{
		for (int i = 0; i < n; i++)
			if (dl[i] < INF)
				System.out.print(dl[i] + "  ");
			else
				System.out.print(" inf ");
		//System.out.println();
	}
	public void drawGraph() 		//вывод ребер на консоль
	{
		Node ptr = e;
		while (ptr != null)
		{
			System.out.println("[ " + ptr.a+1 + ", " + ptr.b+1 + " ], вес=" + ptr.w);
			ptr = ptr.next;
		}
	}
	public int[] get_table() 		//возвращает ссылку на массив пометок
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
	public String getEdges()
	{
		String res = "[ " + (e.getNode(consEdge).a+1) + ", " + (e.getNode(consEdge).b+1) + " ]";
		return res;
	}
	
	public boolean step() 		//шаг алгоритма ф-б.
	{
		consEdge = v; //сохранили номер рассматриваемого ребра
		if (any)
		{
			if (v < m) 
			{
				if (dl[ e.getNode(v).a] < INF )
				{
					//consEdge = v; //сохранили номер рассматриваемого ребра
					
					dl[ e.getNode(v).b ] =  
					min( dl[ e.getNode(v).b ], (dl [ e.getNode(v).a ] + e.getNode(v).w)); // релаксация
					
					any = true;
				}
				v++;
				if (v >= m)
				{
					k++;
					any = false;
					v = 0;
				}
			}
			else
			{
				v++;
			}
		}
		else
		{
			return true; // алгоритм закончен
		} 
		return false; // алгоритм не закончен
	}
	public void Ford_Bellman(int source)
	{
		any = true;
		s = --source;
		k = 0;
		v = 0;
		for (int i = 0; i < n; i++)
			dl[i] = INF;
		
		for (int i = 0; i < m; i++)
			if (e.getNode(i).a == s) dl[ e.getNode(i).b ] = e.getNode(i).w;

		dl[source] = 0;
	}
	
	public int min(int a, int b)
	{
		if (a < b) return a;
		//any = true;
		return b; 
	}
}

