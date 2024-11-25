package com.itwill.contact.ver05.view;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.itwill.contact.ver05.controller.ContactDao;
import com.itwill.contact.ver05.model.Contact;

import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class ContactMain05 {
    // 테이블 컬럼 이름
    private static final String[] COLUMN_NAMES = { "이름", "전화번호" };

    private JFrame frame;
    private JPanel buttonPanel;
    private JButton btnSave;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel model;

    private ContactDao dao;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ContactMain05 window = new ContactMain05();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ContactMain05() {
        this.dao = ContactDao.INSTANCE;
        
        initialize();
        
        loadContactData();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 468, 468);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("연락처 v0.5");
        
        buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        
        btnSave = new JButton("추가");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContactCreateFrame.showContactCreateFrame(frame, ContactMain05.this);
            }
        });
        btnSave.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(btnSave);
        
        btnSearch = new JButton("검색");
        btnSearch.addActionListener(e ->
                ContactSearchFrame.showContactSearchFrame(frame));
        btnSearch.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(btnSearch);
        
        btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(e -> updateContact());
        btnUpdate.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(btnUpdate);
        
        btnDelete = new JButton("삭제");
        btnDelete.addActionListener(e -> deleteContact());
        btnDelete.setFont(new Font("D2Coding", Font.PLAIN, 24));
        buttonPanel.add(btnDelete);
        
        scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable();
        
        model = new DefaultTableModel(null, COLUMN_NAMES);
        table.setModel(model);
        
        scrollPane.setViewportView(table);
    }
    
    private void updateContact() {
        // 테이블에서 선택된 행의 인덱스를 찾음.
        int index = table.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(
                    frame, 
                    "업데이트할 행을 먼저 선택하세요.", 
                    "경고", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 선택한 연락처 인덱스를 업데이트 프레임에게 전달하면서 화면에 띄움.
        ContactUpdateFrame.showContactUpdateFrame(frame, ContactMain05.this, index);
    }
    
    private void deleteContact() {
        // 테이블에서 선택된 행의 인덱스를 찾음.
        int index = table.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(
                    frame, 
                    "삭제할 행을 먼저 선택하세요.", 
                    "경고", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 사용자에게 삭제할 것인 지 확인.
        int confirm = JOptionPane.showConfirmDialog(
                frame, 
                "정말 삭제할까요?", 
                "삭제 확인", 
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // DAO 메서드를 호출해서 연락처를 삭제하고, 파일에 저장.
            int result = dao.delete(index);
            if (result == 1) {
                // 테이블 갱신.
                resetTable();
                JOptionPane.showMessageDialog(frame, "연락처 삭제 성공!");
            } else {
                // TODO 삭제 실패 메시지 다이얼로그
            }
            
        }
        
    }

    private void loadContactData() {
        // DAO의 메서드를 호출해서 파일에 저장된 연락처 데이터를 읽어옴.
        List<Contact> list = dao.read();
        
        // 리스트의 연락처(이름, 전화번호)를 테이블 모델의 행(row)으로 추가.
        for (Contact c : list) {
            Object[] row = { c.getName(), c.getPhone() };
            model.addRow(row);
        }
    }
    
    private void resetTable() {
        model = new DefaultTableModel(null, COLUMN_NAMES);
        loadContactData();
        table.setModel(model);
    }
    
    // ContactCreateFrame에서 새로운 연락처를 파일에 저장한 경우 호출할 메서드. 
    public void notifyContactCreated() {
        // 테이블을 처음부터 다시 새로 그림.
        resetTable();
        
        // 사용자에게 메시지 다이얼로그를 보여줌.
        JOptionPane.showMessageDialog(frame, "연락처 저장 성공!");
    }
    
    // ContactUpdateFrame에서 연락처 업데이트 성공했을 때 호출할 메서드.
    public void notifyContactUpdated() {
        resetTable();
        JOptionPane.showMessageDialog(frame, "연락처 업데이트 성공!");
    }
    
}
