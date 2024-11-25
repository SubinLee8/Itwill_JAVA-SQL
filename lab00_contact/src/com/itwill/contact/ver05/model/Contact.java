package com.itwill.contact.ver05.model;

import java.io.Serial;
import java.io.Serializable;

// MVC 아키텍쳐에서 Model 역할 담당. VO(Value Object). DTO(Data Transfer Object).
// 객체를 파일에서 읽고 쓰기 위해서(직렬화/역직렬화하기 위해서) Serializable 인터페이스를 구현함.
public class Contact implements Serializable {
    @Serial
    private static final long serialVersionUID = -5846053527553368684L;
    
    private Long id; // 데이터베이스 테이블에서 고유키(primary key)로 사용할 예정.
    private String name;
    private String phone;
    private String email;
    
    public Contact() {}

    public Contact(Long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + "]";
    }

}
