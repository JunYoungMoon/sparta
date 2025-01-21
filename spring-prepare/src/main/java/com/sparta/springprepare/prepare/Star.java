package com.sparta.springprepare.prepare;

import lombok.Getter;

//api를 내보낼때 jackson을 사용하는데 이떄 get을 사용해야 직렬화를 할수 있음
@Getter
public class Star {
    String name;
    int age;

    public Star(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Star() {}
}