package com.example.bank.controller;

import com.example.bank.service.CashBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private CashBoxService cashBoxService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/")
    public String getMainPage(Model model) {
        cashBoxService.getCashBoxOfCurrentUser(model);
        return "index";
    }
}