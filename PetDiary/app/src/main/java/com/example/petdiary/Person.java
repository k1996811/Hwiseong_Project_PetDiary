package com.example.petdiary;

public class Person {

    String nickname;

    public Person(){

    }
    public Person(String nickname){

        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
