package cn.chiayhon.pojo;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer age;

    private Gender gender;

    private String username;

    private String password;

    private Company company;

}
