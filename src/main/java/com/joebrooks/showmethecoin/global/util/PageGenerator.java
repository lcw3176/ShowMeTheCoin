package com.joebrooks.showmethecoin.global.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PageGenerator {

    public boolean isValidate(int requestPage, long totalElementSize){
        long maxPage = totalElementSize / 10 + 1;

        return maxPage >= requestPage;
    }

    public int getStartPage(int nowPage){
        return Math.max((nowPage - 1) / 10 * 10 + 1, 1);
    }

    public int getLastPage(int nowPage, long totalElementSize){
        if(nowPage % 10 == 0){
            nowPage -= 1;
        }

        return (int) Math.min((nowPage / 10 + 1) * 10, ((totalElementSize - 1) / 10) + 1);
    }

    public int getPreviousPage(int startPage){
        return startPage - 1 <= 0 ? startPage : startPage - 10;
    }

    public int getNextPage(int lastPage, long totalElementSize){
        if((lastPage + 1) * 10 < totalElementSize
                || ((lastPage + 1) * 10 > totalElementSize
                && lastPage * 10 < totalElementSize)) {
            return lastPage + 1;
        }

        return lastPage;
    }


}