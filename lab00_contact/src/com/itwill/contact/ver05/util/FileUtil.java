package com.itwill.contact.ver05.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itwill.contact.ver05.model.Contact;

// 도우미 클래스(helper class): 파일 관련(읽기, 쓰기, 폴더 생성) 기능을 제공하는 클래스.
// - 객체를 생성하지 못하도록 작성. 모든 메서드들을 public static으로 작성.
public class FileUtil {
    public static final String DATA_DIR = "data"; // 데이터 파일을 저장할 폴더 이름.
    public static final String DATA_FILE = "contacts.dat"; // 데이터 파일 이름.
    
    // private 생성자 -> 다른 클래스에서는 객체를 생성할 수 없음.
    private FileUtil() {}
    
    /**
     * 연락처(Contact)의 리스트(List<E>)가 저장된 데이터 파일을 읽고, 그 결과를 리턴.
     * 
     * @return List<Contact> 타입 객체.
     */
    public static List<Contact> readDataFromFile() {
        List<Contact> list = null;
        
        File file = new File(DATA_DIR, DATA_FILE);
        try ( // 리소스 생성
                FileInputStream in = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(in);
                ObjectInputStream ois = new ObjectInputStream(bis);
        ) {
            list = (List<Contact>) ois.readObject();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * 연락처(Contact)의 리스트(List<E>)를 파일에 쓰는 메서드.
     * 
     * @param list - 파일에 쓸 데이터. 연락처들의 리스트(List<Contact>).
     */
    public static void writeDataToFile(List<Contact> list) {
        File file = new File(DATA_DIR, DATA_FILE);
        try ( // 리소스 생성
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(list);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 연락처 데이터 파일을 저장하는 폴더가 만들어져 있지 않으면 폴더를 새로 생성하고 
     * File 타입의 객체를 리턴,
     * 데이터 폴더가 이미 존재하면, 그 폴더의 File 객체를 리턴.
     * 
     * @return File 타입 객체 리턴. 폴더 생성 실패인 경우에는 null.
     */
    public static File initializeDataDir() {
        File dir = new File(DATA_DIR);
        if (dir.exists()) {
            System.out.println("데이터 폴더가 이미 있습니다.");
        } else {
            boolean result = dir.mkdir();
            if (result) {
                System.out.println("데이터 폴더를 새로 만듭니다.");
            } else {
                System.out.println("데이터 폴더 생성 실패!!!");
                dir = null;
            }
        }
        
        return dir;
    }
    
    /**
     * 연락처 파일(data\contacts.dat)이 있으면, 파일의 내용을 읽어서 리턴.
     * 연락처 데이터 파일이 없으면, 빈 리스트(원소가 없는 리스트)를 리턴.
     *  
     * @return List<Contact>.
     */
    public static List<Contact> initializeData() {
        List<Contact> list = new ArrayList<>();
        
        File file = new File(DATA_DIR, DATA_FILE);
        if (file.exists()) {
            list = readDataFromFile();
        }
        
        return list;
    }
    
}
