package cn.chiayhon.pojo;

import lombok.Data;

@Data
public class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Byte sex;
}
