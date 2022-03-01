package com.joebrooks.showmethecoin.recommend;

import com.joebrooks.showmethecoin.common.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class ReCommendController {

    @GetMapping
    public String showMainForm(Model model){
        List<RecommendResponse> lst = new LinkedList<>();

        for(CoinType i : CoinType.values()){
            RecommendResponse response = new RecommendResponse();
            response.setCode(i.name().toLowerCase());
            response.setKoreanName(i.getKoreanName());

            lst.add(response);
        }
        model.addAttribute("coinList", lst);
        return "recommend";
    }
}
