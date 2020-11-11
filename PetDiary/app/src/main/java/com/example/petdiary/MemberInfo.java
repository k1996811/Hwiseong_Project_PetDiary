package com.example.petdiary;

public class MemberInfo {

    private String email;
    private String nickName;
    private String password;

    public MemberInfo(String email, String password, String nickName){
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName(){
        return this.nickName;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }

}
