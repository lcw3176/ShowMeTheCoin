package com.joebrooks.showmethecoin.auto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auto")
public class AutoController {

    private final AutoService autoService;

    @GetMapping
    public String showAdminForm(){
        return "auto";
    }


    @PostMapping
    public ResponseEntity executeAutomation(@RequestBody CommandRequest request){
        autoService.execute(request);

        return ResponseEntity.ok().build();
    }
}
