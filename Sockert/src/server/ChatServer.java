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
        super("채팅 서버");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       
        ta = new JTextArea();
        pane = new JScrollPane(ta);
        add(pane);
        ta.setText("채팅 서버 시작됨!");
       
        setSize(400, 300);
        setVisible(true);
       
        //네트워크 코드
        try{
            list = new ArrayList<ChatThread>();
            ss = new ServerSocket(9999);
            while(true){
               
                s = ss.accept(); //접속되어온소켓을 소켓참조변수 s에 담는다.
                ChatThread t = new ChatThread();
                list.add(t); //멀티 쓰레드를 위해
                t.start();
               
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       
       
       
    }//생성자
    class ChatThread extends Thread{
       
        BufferedReader br; //한글OK , 한줄씩 입력 OK
        PrintWriter pw; //한글 OK, 한줄씩 출력 OK
        String nickName;
        public ChatThread(){
            try{
                //입력
                InputStream is = s.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
               
                //출력
                OutputStream os = s.getOutputStream();
                pw = new PrintWriter(os,true);
                               
            }catch(Exception e){
                e.printStackTrace();
            }
           
        }//내부 클래스의 생성자
        public void send(String str) { //채팅내용 전송
            pw.println(str);
        }
        @Override
        public void run(){
            try {
                pw.println("닉네님을 입력하세요.");
                nickName = br.readLine();
                broadcast(nickName+" 님이 입장했습니다.!");
                while(true){
                    String str = br.readLine(); //채팅 내용을 받아서 브로드캐스팅
                    broadcast("["+nickName+"] "+str); //[홍길동] 안녕
                    ta.append("[" +nickName+"]"+str+"\n");
                }//while
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//inner class ChatThread end
    //외부 클래스의 메소드
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
