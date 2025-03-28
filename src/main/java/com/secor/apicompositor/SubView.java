package com.secor.apicompositor;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class SubView  implements Serializable {

    private String subid;
    private Date startdate;
    private String paymentid;
    private List<UserView> usersViews = new ArrayList<>();

}
