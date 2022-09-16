package com.example.bank.controller;

import com.example.bank.dal.dto.CashTransferDto;
import com.example.bank.model.CashTransferFilterModel;
import com.example.bank.model.WithdrawalModel;
import com.example.bank.service.CashTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CashTransferController {

    @Autowired
    private CashTransferService cashTransferService;

    @GetMapping("/cashTransfer")
    public String getCashTransferPage() {
        return "transfer";
    }

    @PostMapping("/cashTransfer/create")
    public String createCashTransfer(@ModelAttribute("cashTransfer") CashTransferDto cashTransferDto, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("message", cashTransferService.createCashTransfer(cashTransferDto));
        return "redirect:/";
    }

    @GetMapping("/cashTransfer/list")
    public String getCashTransferListPage(Model model) {
        cashTransferService.getModelCashTransferList(1, 5, new CashTransferFilterModel(), model);
        return "transfer-list";
    }

    @PostMapping("/cashTransfer/list/filter")
    public String getCashTransferListPageByFilters(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @ModelAttribute CashTransferFilterModel cashTransferFilterModel,
                                          Model model) {
        cashTransferService.getModelCashTransferList(page, size, cashTransferFilterModel, model);
        return "transfer-list";
    }

    @GetMapping("/cashTransfer/list/filter")
    public String getCashTransferListPageByFilters(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   Model model) {
        cashTransferService.getModelCashTransferList(page, size, new CashTransferFilterModel(), model);
        return "transfer-list";
    }

    @GetMapping("/withdrawal")
    public String getWithdrawalPage() {
        return "withdrawal";
    }

    @PostMapping("/withdrawal")
    public String withdrawal(@ModelAttribute WithdrawalModel withdrawalModel, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("message", cashTransferService.withdrawal(withdrawalModel));
        return "redirect:/";
    }
}
