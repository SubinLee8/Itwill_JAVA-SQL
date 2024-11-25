package com.itwill.jdbc.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.itwill.jdbc.controller.BlogDao;
import com.itwill.jdbc.model.Blog;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BlogCreateFrame extends JFrame {
	
	
	public interface CreateNotify {
		void notifyBlogCreated();
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel mainPanel;
	private JPanel panel;
	private JTextField textFTITLE;
	private JLabel lblContent;
	private JScrollPane scrollPane;
	private JTextArea textContent;
	private JLabel lblAuthor;
	private JTextField textAuthor;
	private JButton btnCancel;
	private JButton btnSave;
	private Component parentComponent;
	private BlogDao blogDao;
	private CreateNotify app;

	/**
	 * Launch the application.
	 */
	public static void showBlogCreateFrame(Component parentComponent, CreateNotify app) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					BlogCreateFrame frame = new BlogCreateFrame(parentComponent, app);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * UI 컴포넌트들을 초기화.
	 */
	private BlogCreateFrame(Component parentComponen, CreateNotify app) {
		this.blogDao=BlogDao.INSTANCE;
		this.app=app;
		this.parentComponent=parentComponent;
		initialize();
	}

	/**
	 * Create the frame.
	 */
	private void initialize() {
		//JFrame의 타이틀 설정.
		setTitle("새 블로그 작성");
		
		//프레임을 닫을 때 전체 창이 닫히지 않게 한다.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		int x=100;
		int y=100;
		
		if(parentComponent!=null) {
			x=parentComponent.getX()+parentComponent.getWidth();
			y=parentComponent.getY();
		}
		setBounds(x, y, 464, 613);
		//setLocationRelativeTo(parentComponent); <-- 부모 창 위에 뜨거나 부모의 값이 null이면 화면 정중앙에 뜨도록 한다.
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JLabel lblTitle = new JLabel("제목");
		lblTitle.setBounds(12, 48, 414, 30);
		mainPanel.add(lblTitle);
		
		textFTITLE = new JTextField();
		textFTITLE.setBounds(12, 88, 414, 40);
		mainPanel.add(textFTITLE);
		textFTITLE.setColumns(10);
		
		lblContent = new JLabel("내용");
		lblContent.setBounds(12, 155, 414, 30);
		mainPanel.add(lblContent);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 195, 414, 141);
		mainPanel.add(scrollPane);
		
		textContent = new JTextArea();
		scrollPane.setViewportView(textContent);
		
		lblAuthor = new JLabel("작성자");
		lblAuthor.setBounds(12, 365, 414, 30);
		mainPanel.add(lblAuthor);
		
		textAuthor = new JTextField();
		textAuthor.setColumns(10);
		textAuthor.setBounds(12, 396, 414, 40);
		mainPanel.add(textAuthor);
		
		panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		btnSave = new JButton("저장");
		btnSave.addActionListener(e->createNewBlog());
		panel.add(btnSave);
		
		btnCancel = new JButton("취소");
		btnCancel.addActionListener(e->dispose());
		panel.add(btnCancel);
	}
	
	private void createNewBlog() {
		//제목, 내용, 작성자에 입력된 문자열을 읽고, DAO의 메서드를 이용해 DB에 INSERT
		String title = textFTITLE.getText();
		String content = textContent.getText();
		String author = textAuthor.getText();
		
		//todo: 제목,내용,작성자가 비어 있으면 사용자에게 경고를 주고, 메서드를 종료.
		if(title.equals("") || content.equals("") || author.equals("")) {
			JOptionPane.showMessageDialog(BlogCreateFrame.this, "전부 입력하지 않았습니다.","error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		Blog blog = Blog.builder().title(title).content(content).author(author).build();
		int result = blogDao.create(blog);
		if (result ==1 ) {
			app.notifyBlogCreated();
			dispose();
		}
		else {
			JOptionPane.showMessageDialog(BlogCreateFrame.this, "블로그 생성을 실패했습니다","error",JOptionPane.ERROR_MESSAGE);
		}
		
		//todo 메인창에게 insert성공했다고 알려줌.
		
		
	}

}
