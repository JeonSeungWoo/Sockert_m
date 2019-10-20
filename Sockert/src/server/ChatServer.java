package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ChatServer extends JFrame {

    JTextArea ta;
    JScrollPane pane;
    ServerSocket ss;
    Socket s;
    ArrayList<ChatThread> list;
   
    public ChatServer(){
        super("ä�� ����");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       
        ta = new JTextArea();
        pane = new JScrollPane(ta);
        add(pane);
        ta.setText("ä�� ���� ���۵�!");
       
        setSize(400, 300);
        setVisible(true);
       
        //��Ʈ��ũ �ڵ�
        try{
            list = new ArrayList<ChatThread>();
            ss = new ServerSocket(9999);
            while(true){
               
                s = ss.accept(); //���ӵǾ�¼����� ������������ s�� ��´�.
                ChatThread t = new ChatThread();
                list.add(t); //��Ƽ �����带 ����
                t.start();
               
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       
       
       
    }//������
    class ChatThread extends Thread{
       
        BufferedReader br; //�ѱ�OK , ���پ� �Է� OK
        PrintWriter pw; //�ѱ� OK, ���پ� ��� OK
        String nickName;
        public ChatThread(){
            try{
                //�Է�
                InputStream is = s.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
               
                //���
                OutputStream os = s.getOutputStream();
                pw = new PrintWriter(os,true);
                               
            }catch(Exception e){
                e.printStackTrace();
            }
           
        }//���� Ŭ������ ������
        public void send(String str) { //ä�ó��� ����
            pw.println(str);
        }
        @Override
        public void run(){
            try {
                pw.println("�г״��� �Է��ϼ���.");
                nickName = br.readLine();
                broadcast(nickName+" ���� �����߽��ϴ�.!");
                while(true){
                    String str = br.readLine(); //ä�� ������ �޾Ƽ� ��ε�ĳ����
                    broadcast("["+nickName+"] "+str); //[ȫ�浿] �ȳ�
                    ta.append("[" +nickName+"]"+str+"\n");
                }//while
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//inner class ChatThread end
    //�ܺ� Ŭ������ �޼ҵ�
    public void broadcast(String str){
        for (int i = 0; i < list.size(); i++) {
            ChatThread t = (ChatThread)list.get(i);
            t.send(str);
        }//for
       
    }
    public static void main(String[] args) {
        new ChatServer();
    }//main

}
