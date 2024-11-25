package com.itwill.contact.ver05.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.itwill.contact.ver05.controller.ContactDao;
import com.itwill.contact.ver05.model.Contact;

public class ContactSearchFrame extends JFrame {
    
    private static final String[] COLUMN_NAMES = { "이름", "전화번호", "이메일" };
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel buttonPanel;
    private JTextField textKeyword;
    private JButton btnSearch;
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel model;
    
    private ContactDao dao;
    private Component parentComponent;

    /**
     * Launch the application.
     */
    public static void showContactSearchFrame(Component parentComponent) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ContactSearchFrame frame = new ContactSearchFrame(parentComponent);
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
    private ContactSearchFrame(Component parentComponent) {
        this.dao = ContactDao.INSTANCE;
        this.parentComponent = parentComponent;
        
        initialize();
    }
    
    private void initialize() {
        setTitle("연락처 검색");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        int x = 0;
        int y = 0;
        if (parentComponent != null) {
            x = parentComponent.getX() + parentComponent.getWidth();
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
        
        buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.NORTH);
        
        textKeyword = new JTextField();
        textKeyword.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(textKeyword);
        textKeyword.setColumns(20);
        
        btnSearch = new JButton("검색");
        btnSearch.addActionListener(e -> searchContact());
        btnSearch.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(btnSearch);
        
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable();
        
        model = new DefaultTableModel(null, COLUMN_NAMES);
        table.setModel(model);
        
        scrollPane.setViewportView(table);
    }
    
    private void searchContact() {
        String keyword = textKeyword.getText();
        if (keyword == null || keyword.equals("")) {
            JOptionPane.showMessageDialog(
                    this, 
                    "검색어를 입력하세요.", 
                    "경고", 
                    JOptionPane.WARNING_MESSAGE);
            
            textKeyword.requestFocus();
            
            return;
        }
        
        List<Contact> contacts = dao.search(keyword);
        
        model = new DefaultTableModel(null, COLUMN_NAMES);
        for (Contact c : contacts) {
            Object[] rowData = { c.getName(), c.getPhone(), c.getEmail() };
            model.addRow(rowData);
        }
        table.setModel(model);
    }

}
