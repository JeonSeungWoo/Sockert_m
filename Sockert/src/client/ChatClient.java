package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChatClient extends JFrame implements Runnable, ActionListener{

    JTextArea jta;
    JScrollPane jpane;
    JTextField jtf;
    Socket socket;
    BufferedReader bufReader;
    PrintWriter printWriter;

    public ChatClient(){
        setTitle("ä�� Ŭ���̾�Ʈ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //����â�� �ݱ�
       
        jta = new JTextArea();
        jta.setEditable(false);
        jpane = new JScrollPane(jta);
        add(jpane);
        jtf = new JTextField();
        add(jtf, BorderLayout.SOUTH);
       
        jtf.addActionListener(this); //����ġ�� �̺�Ʈ �߻�
       
        setSize(400,300);
        setVisible(true);
       
       
        //��Ʈ��ũ �ڵ�
        try {
        	socket = new Socket("localhost",9999);
            //�Է�
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            bufReader = new BufferedReader(isr);
           
            //���
            OutputStream os = socket.getOutputStream();
            printWriter = new PrintWriter(os, true);
           
        } catch (Exception e) {
           
        }
        //������ �ڵ�
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) { //���� �Ǵ� ��ư Ŭ���������
        String chat = jtf.getText(); //tf�� ���� ä�� ������ �����´�.
        printWriter.println(chat); //ä�ó��� ������ ����
        jtf.setText(""); //tf�� �����.
 
    }
 
    @Override
    public void run() {
        try {
            String str = bufReader.readLine();
            jta.append(str+"\n"); //"�г����� �Է��ϼ���!" ���
            while(true){
                str= bufReader.readLine(); //ä�� ����
                jta.append(str+"\n");
                //��ũ�� �� ������
                jta.setCaretPosition(jta.getText().length());
            }
        } catch (Exception e) {
           
        }
       
 
    }
   
    public static void main(String[] args) {
        new ChatClient();
    }
 
}
