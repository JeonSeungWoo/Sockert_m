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
        setTitle("채팅 클라이언트");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //현재창만 닫기
       
        jta = new JTextArea();
        jta.setEditable(false);
        jpane = new JScrollPane(jta);
        add(jpane);
        jtf = new JTextField();
        add(jtf, BorderLayout.SOUTH);
       
        jtf.addActionListener(this); //엔터치면 이벤트 발생
       
        setSize(400,300);
        setVisible(true);
       
       
        //네트워크 코드
        try {
        	socket = new Socket("localhost",9999);
            //입력
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            bufReader = new BufferedReader(isr);
           
            //출력
            OutputStream os = socket.getOutputStream();
            printWriter = new PrintWriter(os, true);
           
        } catch (Exception e) {
           
        }
        //쓰레드 코드
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) { //엔터 또는 버튼 클릭했을경우
        String chat = jtf.getText(); //tf로 부터 채팅 내용을 가져온다.
        printWriter.println(chat); //채팅내용 서버로 전송
        jtf.setText(""); //tf를 지운다.
 
    }
 
    @Override
    public void run() {
        try {
            String str = bufReader.readLine();
            jta.append(str+"\n"); //"닉네임을 입력하세요!" 출력
            while(true){
                str= bufReader.readLine(); //채팅 내용
                jta.append(str+"\n");
                //스크롤 맨 밑으로
                jta.setCaretPosition(jta.getText().length());
            }
        } catch (Exception e) {
           
        }
       
 
    }
   
    public static void main(String[] args) {
        new ChatClient();
    }
 
}
