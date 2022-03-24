package cn.chiayhon.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
}
