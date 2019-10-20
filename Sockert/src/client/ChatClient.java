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

    JTextArea ta;
    JScrollPane pane;
    JTextField tf;
    Socket s;
    BufferedReader br;
    PrintWriter pw;
   
    public ChatClient(){
        setTitle("ä�� Ŭ���̾�Ʈ v1.0.1");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); //����â�� �ݱ�
       
        ta = new JTextArea();
        ta.setEditable(false);
        pane = new JScrollPane(ta);
        add(pane);
        tf = new JTextField();
        add(tf, BorderLayout.SOUTH);
       
        tf.addActionListener(this); //����ġ�� �̺�Ʈ �߻�
       
        setSize(400,300);
        setVisible(true);
       
       
        //��Ʈ��ũ �ڵ�
        try {
            s = new Socket("localhost",9999);
            //�Է�
            InputStream is = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
           
            //���
            OutputStream os = s.getOutputStream();
            pw = new PrintWriter(os, true);
           
        } catch (Exception e) {
           
        }
        //������ �ڵ�
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) { //���� �Ǵ� ��ư Ŭ���������
        String chat = tf.getText(); //tf�� ���� ä�� ������ �����´�.
        pw.println(chat); //ä�ó��� ������ ����
        tf.setText(""); //tf�� �����.
 
    }
 
    @Override
    public void run() {
        try {
            String str = br.readLine();
            ta.append(str+"\n"); //"�г����� �Է��ϼ���!" ���
            while(true){
                str= br.readLine(); //ä�� ����
                ta.append(str+"\n");
                //��ũ�� �� ������
                ta.setCaretPosition(ta.getText().length());
            }
        } catch (Exception e) {
           
        }
       
 
    }
   
    public static void main(String[] args) {
        new ChatClient();
    }
 
}
