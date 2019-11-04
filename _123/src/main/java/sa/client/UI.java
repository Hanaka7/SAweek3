package sa.client;

import net.sf.json.JSONObject;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

class UI {
    public static void main(String[] args){




        JFrame frame = new JFrame("mail sender");
        frame.setSize(550,900);
        frame.setResizable(false);

        JPanel p =new JPanel();
        p.setLayout(null);
        frame.add(p);

        JLabel lb1 = new JLabel("email:");
        p.add(lb1);
        lb1.setBounds(10,10,80,20);

        JTextArea mail = new JTextArea();
        p.add(mail);
        mail.setBounds(10,40,400,160);



        JLabel lb2 = new JLabel("payload:");
        p.add(lb2);
        lb2.setBounds(10,210,80,20);

        JTextArea payload = new JTextArea();
        p.add(payload);
        payload.setBounds(10,230,400,360);

        JTextArea msg = new JTextArea();
        p.add(msg);
        msg.setBounds(10,600,400,250);
        msg.setEditable(false);

        JButton button1 = new JButton("decide");
        p.add(button1);
        button1.setBounds(420,40,80,20);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(1);
                JSONObject json = new JSONObject();
                try{
                    json.put("url",mail.getText().toString());
                    json.put("payload",payload.getText().toString());
                    System.out.println(2);
                }catch(Exception e1){
                    e1.printStackTrace();
                }
                try{
                    URL url = new URL("http://localhost:8080/senderapi/rest/decidemail");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.getOutputStream().write(json.toString().getBytes());
                    conn.getOutputStream().flush();
                    System.out.println(3);
                    if (conn.getResponseCode() == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String responseStr = in.lines().collect(Collectors.joining("\n"));
                        JSONObject response = JSONObject.fromObject(responseStr);
                        System.out.println(response);
                        System.out.println(response.get("msg"));
                        if ("succeed".equals(response.get("msg"))) {
                            msg.setText("right");
                        } else {
                            msg.setText("wrong");
                        }
                    }
                    else{
                        System.out.println("4");
                    }
                }catch(Exception e2){}
            }
        });

        JButton button2 =new JButton("send");
        p.add(button2);
        button2.setBounds(420,230,80,20);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(1);
                JSONObject json = new JSONObject();
                try{
                    json.put("url",mail.getText().toString());
                    json.put("payload",payload.getText().toString());
                    System.out.println(2);
                }catch(Exception e1){
                    e1.printStackTrace();
                }
                try{
                    URL url = new URL("http://localhost:8080/senderapi/rest/sendmail");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.getOutputStream().write(json.toString().getBytes());
                    conn.getOutputStream().flush();
                    System.out.println(3);
                    if (conn.getResponseCode() == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String responseStr = in.lines().collect(Collectors.joining("\n"));
                        JSONObject response = JSONObject.fromObject(responseStr);
                        System.out.println(response);
                        System.out.println(response.get("msg"));
                        if ("succeed".equals(response.get("msg"))) {
                            msg.setText("send succeed");
                        } else {
                            msg.setText("send fail");
                        }
                    }
                    else{
                        System.out.println("4");
                    }
                }catch(Exception e1){}
            }
        });


        frame.setVisible(true);



    }
}
