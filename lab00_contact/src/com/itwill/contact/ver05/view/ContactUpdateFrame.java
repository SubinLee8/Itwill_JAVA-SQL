package com.itwill.contact.ver05.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.itwill.contact.ver05.controller.ContactDao;
import com.itwill.contact.ver05.model.Contact;

public class ContactUpdateFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JLabel lblName;
    private JTextField textName;
    private JLabel lblPhone;
    private JTextField textPhone;
    private JLabel lblEmail;
    private JTextField textEmail;
    private JButton btnUpdate;
    private JButton btnCancel;

    private ContactDao dao;
    private Component parentComponent;
    private ContactMain05 mainApp;
    private int index;
    
    /**
     * Launch the application.
     */
    public static void showContactUpdateFrame(
            Component parentComponent, 
            ContactMain05 mainApp, 
            int index) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ContactUpdateFrame frame = new ContactUpdateFrame(parentComponent, mainApp, index);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    private ContactUpdateFrame(Component parentComponent, ContactMain05 mainApp, int index) {
        this.dao = ContactDao.INSTANCE;
        this.parentComponent = parentComponent;
        this.mainApp = mainApp;
        this.index = index;
        
        initialize();
        
        loadContactInfo();
    }
    
    private void initialize() {
        setTitle("연락처 업데이트");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        int x = 0;
        int y = 0;
        if (parentComponent != null) {
            x = parentComponent.getX();
            y = parentComponent.getY();
        }
        setBounds(x, y, 450, 300);
        
        if (parentComponent == null) {
            setLocationRelativeTo(null); // 화면의 한가운데에 JFrame을 보여줌.
        }
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        mainPanel = new JPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(null);
        
        lblName = new JLabel("이름");
        lblName.setFont(new Font("D2Coding", Font.PLAIN, 24));
        lblName.setBounds(12, 10, 120, 40);
        mainPanel.add(lblName);
        
        textName = new JTextField();
        textName.setFont(new Font("D2Coding", Font.PLAIN, 24));
        textName.setBounds(144, 10, 268, 40);
        mainPanel.add(textName);
        textName.setColumns(10);
        
        lblPhone = new JLabel("전화번호");
        lblPhone.setFont(new Font("D2Coding", Font.PLAIN, 24));
        lblPhone.setBounds(12, 60, 120, 40);
        mainPanel.add(lblPhone);
        
        textPhone = new JTextField();
        textPhone.setFont(new Font("D2Coding", Font.PLAIN, 24));
        textPhone.setColumns(10);
        textPhone.setBounds(144, 60, 268, 40);
        mainPanel.add(textPhone);
        
        lblEmail = new JLabel("이메일");
        lblEmail.setFont(new Font("D2Coding", Font.PLAIN, 24));
        lblEmail.setBounds(12, 110, 120, 40);
        mainPanel.add(lblEmail);
        
        textEmail = new JTextField();
        textEmail.setFont(new Font("D2Coding", Font.PLAIN, 24));
        textEmail.setColumns(10);
        textEmail.setBounds(144, 110, 268, 40);
        mainPanel.add(textEmail);
        
        buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        
        btnUpdate = new JButton("업데이트");
        btnUpdate.addActionListener(e -> updateContact());
        btnUpdate.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(btnUpdate);
        
        btnCancel = new JButton("취소");
        btnCancel.addActionListener(e -> dispose());
        btnCancel.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(btnCancel);
    }
    
    private void updateContact() {
        String name = textName.getText();
        String phone = textPhone.getText();
        String email = textEmail.getText();
        Contact contact = new Contact(null, name, phone, email);
        
        int result = dao.update(index, contact);
        if (result == 1) {
            mainApp.notifyContactUpdated();
            dispose();
        } else {
            // 업데이트 실패
        }
    }
    
    private void loadContactInfo() {
        // DAO의 메서드를 호출해서 index에 해당하는 연락처 정보를 가져옴.
        Contact contact = dao.read(index);
        
        // 연락처 정보(이름, 전화번호, 이메일)을 JTextField에 씀.
        textName.setText(contact.getName());
        textPhone.setText(contact.getPhone());
        textEmail.setText(contact.getEmail());
    }
        
}
