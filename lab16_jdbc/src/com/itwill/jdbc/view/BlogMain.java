package com.itwill.jdbc.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.itwill.jdbc.controller.BlogDao;
import com.itwill.jdbc.model.Blog;
import com.itwill.jdbc.view.BlogCreateFrame.CreateNotify;
import com.itwill.jdbc.view.BlogUpdateFrame.UpdateNotify;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Font;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//MVC 아키텍쳐에서 VIEW
public class BlogMain implements CreateNotify, UpdateNotify{
	
	

	//JComboBox 아이템 이름들을 상수로 선언
	private static final String[] SEARCH_TYPE= {"제목", "내용", "제목+내용", "작성자"};
	
	//Jtable의 컬럼이름들을 상수로 선언
	private static final String[] COLUMN_NAMES= {"번호", "제목", "작성자", "작성시간"};
	
	private JFrame frame;
	private JPanel searchPanel;
	private JComboBox<String> comboBox;
	private JTextField textSearchKeyword;
	private JButton btnSearch;
	private JTable table;
	private JPanel buttonPanel;
	private JScrollPane scrollPane;
	private JButton btnReadAll;
	private JButton btnCreate;
	private JButton btnDetails;
	private JButton btnDelete;
	private DefaultTableModel model;
	
	//mvc 아키텍쳐에서 컨트롤러 객체.
	private BlogDao blogdao; 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					BlogMain window = new BlogMain();
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
	public BlogMain() {
		blogdao = BlogDao.INSTANCE;
		initialize();
		initializeTable();
	}
	
	private void initializeTable() {
		//controller인 dao의 메서드를 호출해서 db에 저장된 데이터를 읽어온다.
		List<Blog> list = blogdao.read();
		resetTableModel(list);
	}
	
	private void resetTableModel(List<Blog> list) {
		model = new DefaultTableModel(null, COLUMN_NAMES);
		for(Blog b : list) {
			Object[] rowData = {
					b.getId(),b.getTitle(),b.getAuthor(),b.getCreatedTime()
			};
			model.addRow(rowData);
		}
		table.setModel(model);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("블로그 앱 v1.0");
		frame.setBounds(100, 100, 468, 511);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		searchPanel = new JPanel();
		frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
		
		comboBox = new JComboBox<>();
		
		final DefaultComboBoxModel<String> comboboxModel=new DefaultComboBoxModel<>(SEARCH_TYPE);
		comboBox.setModel(comboboxModel);
		
		comboBox.setFont(new Font("굴림", Font.PLAIN, 14));
		searchPanel.add(comboBox);
		
		textSearchKeyword = new JTextField();
		textSearchKeyword.setFont(new Font("굴림", Font.PLAIN, 14));
		searchPanel.add(textSearchKeyword);
		textSearchKeyword.setColumns(20);
		
		btnSearch = new JButton("검색");
		//TODO
		btnSearch.addActionListener(e->searchByKeyword());
		btnSearch.setFont(new Font("굴림", Font.PLAIN, 14));
		searchPanel.add(btnSearch);
		
		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		//JTable 컬럼 이름 설정.
		table = new JTable();
		scrollPane.setViewportView(table);
		model = new DefaultTableModel(null,COLUMN_NAMES);
		table.setModel(model);
		
		
		buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		btnReadAll = new JButton("목록보기");
		//TODO
		btnReadAll.addActionListener(e->initializeTable());
		buttonPanel.add(btnReadAll);
		
		btnCreate = new JButton("새 블로그 작성");
		btnCreate.addActionListener(e->BlogCreateFrame.showBlogCreateFrame(frame,BlogMain.this));
		buttonPanel.add(btnCreate);
		
		btnDetails = new JButton("상세보기");
		btnDetails.addActionListener(e->updateBlog());
		buttonPanel.add(btnDetails);
		
		btnDelete = new JButton("삭제");
		btnDelete.addActionListener(e->deleteBlog());
		buttonPanel.add(btnDelete);
	}
	
	
	private void searchByKeyword() {
		//검색종류(제목, 내용, 제목+내용, 작성자)를 찾음
		int index=comboBox.getSelectedIndex();
		
		String keyword=textSearchKeyword.getText();
		if(keyword.equals("")) {
			JOptionPane.showMessageDialog(frame, "키워드를 입력하세요","error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		List<Blog> results = blogdao.read(index, keyword);
		
		resetTableModel(results);
	}

	private void updateBlog() {
		//JTable에서 선택된 행의 인덱스를 찾음.
		int index=table.getSelectedRow();
		
		if(index==-1) {
			JOptionPane.showMessageDialog(frame, "선택된 행이 없습니다.","error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		Integer id=(Integer) model.getValueAt(index, 0);
		BlogUpdateFrame.showBlogUpdateFrame(frame,BlogMain.this,id);
		
	}

	private void deleteBlog() {
		//JTable에서 선택된 행을 찾고, 선택된 행의 블로그 번호(ID)로 삭제.
		int index=table.getSelectedRow();
		if(index==-1) {
			JOptionPane.showMessageDialog(frame, "선택된 행이 없습니다.","error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int confirm=JOptionPane.showConfirmDialog(frame, "정말 삭제할까요?","삭제 확인",JOptionPane.YES_NO_OPTION);
		if(confirm==JOptionPane.YES_OPTION) {
			Integer id =(Integer) model.getValueAt(index, 0);	
//			List<Blog> list = blogdao.read();
//			int id = list.get(index).getId();
			blogdao.deleteBlogById(id);
			initializeTable();
		}	
		else {
			return;
		}
	}
	
	//새 블로그 작성 성공했을 때 (DB 테이블에 INSERT가 되었을 때)BlogCreatedFrame이 호출할 메서드.
	@Override
	public void notifyBlogCreated() {
		initializeTable();
		JOptionPane.showMessageDialog(frame, "새로운 블로그를 생성했습니다.");
	}
	
	//블로그 제목/내용 업데이트 성공했을 때, BlogUpdateFrame이 호출할 메서드.
	@Override
	public void notifyUpdateSuccess() {
		initializeTable();
		JOptionPane.showMessageDialog(frame, "업데이트 성공!");
	}
	
	

}
