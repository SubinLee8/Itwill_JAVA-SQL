package com.itwill.contact.ver05.controller;

import java.util.ArrayList;
import java.util.List;

import com.itwill.contact.ver05.model.Contact;

// import static 문장 - 클래스(인터페이스, enum)에서 static으로 선언된 필드/메서드 이름을 import.
import static com.itwill.contact.ver05.util.FileUtil.*;

// MVC 아키텍쳐에서 Controller 역할을 담당하는 객체. DAO(Data Access Object).
// 연락처 저장, 검색(목록, 인덱스), 업데이트, 삭제 기능을 제공.
public enum ContactDao {
    INSTANCE; // ContactDao 타입의 싱글턴 객체 선언.

    // 필드
    private List<Contact> contacts; // 연락처(들)의 리스트 - 파일에서 읽어 올 데이터. 파일에 쓸 데이터.
    
    // 생성자 - enum 타입의 생성자는 private만 가능. private 수식어는 생략 가능.
    ContactDao() {
        // 데이터 폴더 초기화: 폴더가 없는 경우에 폴더를 새로 만듦.
        initializeDataDir();
        
        // 연락처 데이터 초기화: 데이터 파일에서 리스트를 읽어 오거나, 또는 빈 리스트를 생성. 
        contacts = initializeData();
    }
    
    // 메서드
    /**
     * 연락처 데이터를 리스트에 추가하고, 변경된 리스트를 파일에 씀.
     * @param contact - 이름, 전화번호, 이메일 정보를 저장하고 있는 연락처 데이터.
     * @return 성공하면 1, 실패하면 0.
     */
    public int create(Contact contact) {
        contacts.add(contact); // (메모리) 리스트에 연락처 데이터를 추가.
        writeDataToFile(contacts); // (메모리) 리스트를 파일에 씀.
        
        return 1;
    }
    
    /**
     * 연락처 목록 검색.
     * 
     * @return 연락처(들)의 리스트(List<Contact>).
     */
    public List<Contact> read() {
        return contacts;
    }
    
    /**
     * 특정 인덱스 위치에 있는 연락처 정보를 리턴.
     * 
     * @param index - 리스트에서 연락처를 찾기 위한 인덱스. 0 이상의 정수.
     * @return index가 리스트의 인덱스 범위 안에 있으면(0 <= index < size()) 
     * 리스트에서 해당 index 위치의 연락처 객체, 그렇지 않으면 null.
     */
    public Contact read(int index) {
        if (isValidIndex(index)) {
            return contacts.get(index);
        } else {
            return null;
        }
    }
    
    /**
     * 아규먼트 index가 연락처 리스트의 인덱스 범위 안에 있는 지 여부를 리턴.
     * @param index - 연락처 리스트의 유효한 인덱스인 지를 검사하기 위한 정수.
     * @return (0 <= index < size())이면 true, 그렇지 않으면 false.
     */
    public boolean isValidIndex(int index) {
        return (index >= 0) && (index < contacts.size());
    }
    
    /**
     * 해당 인덱스 위치의 연락처를 새로운 정보로 업데이트. 업데이트한 리스트를 파일에 씀.
     * @param index - 업데이트할 연락처의 인덱스.
     * @param contact - 업데이트할 연락처의 새로운 정보(이름, 전화번호, 이메일).
     * @return 성공하면 1, 실패하면 0.
     */
    public int update(int index, Contact contact) {
        if (!isValidIndex(index)) {
            return 0;
        }
        
        // 특정 index 위치의 연락처 정보를 수정.
//        contacts.set(index, contact);
        Contact previous = contacts.get(index);
        previous.setName(contact.getName());
        previous.setPhone(contact.getPhone());
        previous.setEmail(contact.getEmail());
        
        writeDataToFile(contacts); // 업데이트된 리스트를 파일에 씀.
        
        return 1;
    }
    
    /**
     * 연락처 리스트에서 특정 위치의 연락처를 삭제. 변경된 리스트를 파일에 씀.
     * 
     * @param index - 삭제할 연락처의 인덱스.
     * @return 성공하면 1, 그렇지 않으면 0.
     */
    public int delete(int index) {
        if (!isValidIndex(index)) {
            return 0;
        }
        
        contacts.remove(index); // 리스트에서 특정 index 위치의 원소를 삭제.
        writeDataToFile(contacts); // 변경된 리스트를 파일에 씀.
        
        return 1;
    }
    
    /**
     * 연락처 검색.
     * 
     * @param keyword - 검색어(이름, 전화번호, 이메일의 일부).
     * @return 검색어가 이름 또는 전화번호 또는 이메일에 포함된 연락처들의 리스트.
     * 검색어가 포함된 연락처를 찾을 수 없으면 빈 리스트를 리턴.
     */
    public List<Contact> search(String keyword) {
        List<Contact> result = new ArrayList<>();
        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(keyword.toLowerCase()) || 
                    c.getPhone().toLowerCase().contains(keyword.toLowerCase()) ||
                    c.getEmail().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(c);
            }
        }
        
        return result;
    }
    
}
