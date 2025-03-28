package com.secor.apicompositor;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class UserView implements Serializable {

    private String fullname;
    private String email;

}
