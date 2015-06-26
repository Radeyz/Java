package myFrame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class mainClass {

    public static void main(String[] args) {
        myFrame jf = new myFrame();
        jf.createFrame();
    }
    //

    //*******************************************************************************************

    //

     public static class myFrame {

        JFrame mainFrame = new JFrame("algorithm ford-bellman");
        String labelstr = new String("1");
        JTextArea textAr = new JTextArea();
        JTable table = new JTable(100,10);
        JPanel leftPanel = new JPanel(){
        	public void paintComponent(Graphics g){
        		Graphics2D gr = (Graphics2D)g;
        		gr.drawOval(100, 100, 100, 150);
        		gr.drawRect(100, 100, 100, 150);
        	}
        } ;
        JPanel rightPanel = new JPanel();
        JPanel upperPanel = new JPanel();
        JButton b_nextStep = new JButton("Старт");

        Border bord =  BorderFactory.createLineBorder(Color.black, 1);
        public  void createFrame(){
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.setVisible(true);
            addMainPanels();

            mainFrame.setSize(1100, 950);
            

        }/*
        *Обработчик нажатия
        */
        public class ButtonClick implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                int i = 0;

                textAr.replaceRange("", 0, textAr.getSelectionEnd());
                textAr.insert("Шаг " + labelstr + "\n", textAr.getSelectionEnd());
                textAr.insert("Список ребер:\n", textAr.getSelectionEnd());
                i = Integer.valueOf(labelstr)+1;
                labelstr = Integer.toString(i);
                b_nextStep.setText("Следующий шаг");
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
        	upperPanel.setLayout(new FlowLayout());
        	
        	JButton button1 = new JButton("Справка");
        	JButton button2 = new JButton("Загрузить из файла");
        	JButton button3 = new JButton("резерв1");
        	JButton button4 = new JButton("резерв2");
        	        	
        	upperPanel.add(button1);
        	upperPanel.add(button2);
        	upperPanel.add(button3);
        	upperPanel.add(button4);
        }
        public void addRightPanels(){

            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

            JScrollPane scroll_text = new JScrollPane(textAr,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            JScrollPane scroll_table = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scroll_table.setBorder(bord);
            scroll_text.setBorder(bord);
            scroll_table.setSize(new Dimension(rightPanel.getWidth(), 0));
            
            TableColumnModel colMod = table.getColumnModel();
            TableColumn col = colMod.getColumn(0);
            col.setHeaderValue("Шаг");
            table.getTableHeader().updateUI();

            ActionListener AL = new ButtonClick();
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
