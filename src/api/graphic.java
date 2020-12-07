package api;

import gameClient.Ex2_Client;

import javax.swing.*;
import java.awt.event.*;


public class graphic extends JFrame implements ActionListener {

    private int win_w = 500;
    private int win_h = 500;
    private String f = "file";
    JTextField userField, textLevel;
    JButton logButton, resetButton;
    JLabel _level , _id;
    int sizeText=10;
    
    public graphic() {
        initGraphic();
    }

    public void initGraphic() {

        //label id
        _id = new JLabel("Enter ID : ");
        _id.setBounds(15,25,75,25);

        // label level
        _level = new JLabel("Select a level : ");
        _level.setBounds(15,50,100,25);

        // TEXT FILED
        userField =new JTextField(sizeText);
        userField.setBounds(110,25,100,25);
        //textField.setFont(new Font(null,Font.ITALIC , 25));

        // textLevel
        textLevel = new JTextField(sizeText);
        textLevel.setBounds(110,50,100,25);


        // LOG BUTTON
        logButton = new JButton("Login");
        logButton.setBounds(100,100,50,50);
        logButton.setFocusable(false);
        logButton.addActionListener(this);

        // reset button
        resetButton = new JButton("Reset");
        resetButton.setBounds(150,100,50,50);
        resetButton.setFocusable(false);
        resetButton.addActionListener(this);



        //add to frame

        this.add(_id);
        this.add(_level);
        this.add(userField);
        this.add(textLevel);
        this.add(logButton);
        this.add(resetButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(450, 250, 350, 200);
        this.setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String[] a = new String[0];
        if(e.getSource()== resetButton){
            userField.setText("");
            textLevel.setText("");
        }

        if (e.getSource()== logButton){

            String level = textLevel.getText();
            Ex2_Client.main(a);
//            JFrame p = new JFrame();
//            p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            p.setLayout(null);
//            p.setBounds(450, 250, 350, 200);
//            p.setVisible(true);
//            String user = userField.getText();

        }
    }
}
