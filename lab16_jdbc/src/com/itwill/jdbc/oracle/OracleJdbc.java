package com.itwill.jdbc.oracle;

public interface OracleJdbc {
	//상수 선언 - public static final은 생략 가능
	
	//오라클 데이터베이스 서버에 접속하기 위한 JDBC 라이브러리 정보와 서버 주소/포트/SID
	String URL = "jdbc:oracle:thin:@192.168.20.21:1521:xe";
	
	//접속 계정
	String USER = "scott";
	
	//접속 계정 비밀번호
	String PASSWORD = "tiger";
}
