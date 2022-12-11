package com.joebrooks.showmethecoin.global.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeFormatter {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public Date convert(String target){
        try{
            return format.parse(target);
        } catch (Exception e){
            throw new RuntimeException("타임 포맷 변환 에러");
        }
    }

    public Date parse(String target){
        try{
            return parseFormat.parse(target);
        } catch (Exception e){
            throw new RuntimeException("타임 포맷 변환 에러");
        }
    }

    public String convert(Date target){
        try{
            return format.format(target);
        } catch (Exception e){
            throw new RuntimeException("타임 포맷 변환 에러");
        }
    }
}
