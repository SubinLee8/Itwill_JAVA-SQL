package com.itwill.jdbc.view;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.itwill.jdbc.controller.BlogDao;
import com.itwill.jdbc.model.Blog;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BlogUpdateFrame extends JFrame {
	
	public interface UpdateNotify{
		void notifyUpdateSuccess();
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Component parentComponent;
	private JTextField textTitle;
	private JLabel lblTitle;
	private JTextField textField;
	private JLabel lblAuthor;
	private JTextField textAuthor;
	private JLabel lblCreatedTime;
	private JTextField textCreatedTime;
	private JLabel lblModifiedTime;
	private JTextField modifiedTime;
	private JLabel lblId;
	private JTextField textId;
	private JPanel buttonPanel;
	private JButton btnUpdate;
	private JButton btnCancel;
	private JScrollPane scrollPane;
	private JLabel lbContent;
	private UpdateNotify app;
	private int id;
	private BlogDao dao=BlogDao.INSTANCE;

	/**
	 * Launch the application.
	 */
	public static void showBlogUpdateFrame(Component parentComponent,UpdateNotify app, int id) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BlogUpdateFrame frame = new BlogUpdateFrame(parentComponent, app, id);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	BlogUpdateFrame(Component parentFrame, UpdateNotify app,int id){
		this.parentComponent=parentFrame;
		this.app=app;
		this.id=id;
		initialize();
		loadBlogDetails();
	}
	

	private void loadBlogDetails() {
		Blog blog = dao.read(id);
		textId.setText(blog.getId().toString());
	    textTitle.setText(blog.getTitle());
		textAuthor.setText(blog.getAuthor());
		if(blog.getCreatedTime()!=null)textCreatedTime.setText(blog.getCreatedTime().toString());
		textField.setText(blog.getContent());
		if(blog.getModifiedTime()!=null) modifiedTime.setText(blog.getModifiedTime().toString());
	}

	/**
	 * Create the frame.
	 */
	public void initialize() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parentComponent);
		setBounds(100, 100, 450, 626);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		lblTitle = new JLabel("제목");
		lblTitle.setBounds(12, 80, 57, 15);
		panel.add(lblTitle);
		
		textTitle = new JTextField();
		textTitle.setBounds(70, 74, 238, 28);
		panel.add(textTitle);
		textTitle.setColumns(10);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(70, 131, 342, 175);
		panel.add(scrollPane);
		
		textField = new JTextField();
		scrollPane.setViewportView(textField);
		textField.setColumns(10);
		
		lbContent = new JLabel("내용");
		lbContent.setBounds(12, 131, 57, 15);
		panel.add(lbContent);
		
		lblAuthor = new JLabel("작성자");
		lblAuthor.setBounds(12, 341, 57, 15);
		panel.add(lblAuthor);
		
		textAuthor = new JTextField();
		textAuthor.setColumns(10);
		textAuthor.setBounds(70, 338, 238, 28);
		//수정을 못하게 막는다.
		textAuthor.setEditable(false);
		panel.add(textAuthor);
		
		lblCreatedTime = new JLabel("작성시간");
		lblCreatedTime.setBounds(12, 413, 57, 15);
		panel.add(lblCreatedTime);
		
		textCreatedTime = new JTextField();
		textCreatedTime.setColumns(10);
		textCreatedTime.setBounds(70, 410, 238, 28);
		//수정을 못하게 막는다.
		textCreatedTime.setEditable(false);
		panel.add(textCreatedTime);
		
		lblModifiedTime = new JLabel("수정시간");
		lblModifiedTime.setBounds(12, 478, 57, 15);
		panel.add(lblModifiedTime);
		
		modifiedTime = new JTextField();
		modifiedTime.setColumns(10);
		modifiedTime.setBounds(70, 475, 238, 28);
		//수정을 못하게 막는다.
		modifiedTime.setEditable(false);
		panel.add(modifiedTime);
		
		lblId = new JLabel("번호");
		lblId.setBounds(12, 40, 57, 15);
		panel.add(lblId);
		
		textId = new JTextField();
		textId.setColumns(10);
		textId.setBounds(70, 36, 238, 28);
		//수정을 못하게 막는다.
		textId.setEditable(false);
		panel.add(textId);
		
		buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		btnUpdate = new JButton("업데이트");
		btnUpdate.addActionListener(e->updateDetails());
		buttonPanel.add(btnUpdate);
		
		btnCancel = new JButton("취소");
		btnCancel.addActionListener(e->dispose());
		buttonPanel.add(btnCancel);
	}

	private void updateDetails() {
		//제목, 내용을 수정받아 dao의 update(Blog blog)메소드를 이용한다.
		String title=textTitle.getText();
		String content=textField.getText();
		if(title.equals("")||content.equals("")) {
			JOptionPane.showMessageDialog(parentComponent, "제목과 내용은 모두 입력해야합니다.");
			return;
		}
		Blog updated_blog=Blog.builder().title(title).content(content).id(id).build();
		int result=dao.update(updated_blog);
		if(result==1) {
			dispose();
			app.notifyUpdateSuccess();
		}
		else JOptionPane.showMessageDialog(parentComponent, "업데이트에 실패했습니다", "error", JOptionPane.ERROR_MESSAGE);
		
	}
}
