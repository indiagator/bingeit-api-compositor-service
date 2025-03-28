package com.secor.apicompositor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter @Setter
public class Subscription
{
    private String subid;
    private MultiUserView users;
    private String planid;
    private Date startdate;
    private String status; //unpaid, active, inactive, suspended
}
