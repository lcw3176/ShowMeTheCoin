package com.joebrooks.showmethecoin.user.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeResponse{
    private String rank;
    private String nickName;
    private double money;
}