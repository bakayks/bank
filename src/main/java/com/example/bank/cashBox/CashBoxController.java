package com.example.bank.cashBox;

import com.example.bank.enums.Operations;
import com.example.bank.dto.CashBoxDto;
import com.example.bank.model.ReplenishModel;
import com.example.bank.services.ReplenishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cashbox")
public class CashBoxController {

    @Autowired
    private CashBoxRepository cashBoxRepository;

    @Autowired
    private ReplenishService replenishService;

    @GetMapping("/{cashBoxId}")
    private ResponseEntity<CashBoxDto> getCashBoxById(@PathVariable("cashBoxId") Integer cashBoxId){
        Optional<CashBox> cashBoxOptional = cashBoxRepository.findById(cashBoxId);
        if(cashBoxOptional.isPresent()) {
            CashBox cashBox = cashBoxOptional.get();
            CashBoxDto cashBoxDto = new CashBoxDto();
            cashBoxDto.setId(cashBox.getId());
            cashBoxDto.setName(cashBox.getName());
            return new ResponseEntity<>(cashBoxDto, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    private ResponseEntity<Map<String, Object>> getListCashBox(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "3") int size){
        List<CashBoxDto> cashBoxDtoList = new ArrayList<>();

        Pageable paging = PageRequest.of(page, size);

        Page<CashBox> pageTuts = cashBoxRepository.findAll(paging);
        for(CashBox cashBox : pageTuts.getContent()) {
            CashBoxDto cashBoxDto = new CashBoxDto();
            cashBoxDto.setName(cashBox.getName());
            cashBoxDto.setId(cashBox.getId());
            cashBoxDto.setCurrentBalanceSOM(cashBox.getCurrentBalanceSOM());
            cashBoxDto.setCurrentBalanceEURO(cashBox.getCurrentBalanceEURO());
            cashBoxDto.setCurrentBalanceUSD(cashBox.getCurrentBalanceUSD());
            cashBoxDtoList.add(cashBoxDto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("cashBoxes", cashBoxDtoList);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    private ResponseEntity<HttpStatus> createCashBox(@RequestBody CashBoxDto cashBoxDto){
        CashBox cashBox = new CashBox();
        cashBox.setName(cashBoxDto.getName());
        cashBox.setCurrentBalanceUSD(cashBoxDto.getCurrentBalanceUSD());
        cashBox.setCurrentBalanceEURO(cashBoxDto.getCurrentBalanceEURO());
        cashBox.setCurrentBalanceSOM(cashBoxDto.getCurrentBalanceSOM());

        cashBoxRepository.save(cashBox);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/subtractBalance")
    private ResponseEntity<HttpStatus> subtractBalanceByCurrency(@RequestBody ReplenishModel replenishModel){
        return replenishService.operationWithBalance(replenishModel, Operations.SUBTRACT);
    }

    @PostMapping("/additionBalance")
    private ResponseEntity<HttpStatus> additionBalanceByCurrency(@RequestBody ReplenishModel replenishModel){
        return replenishService.operationWithBalance(replenishModel, Operations.ADDITION);
    }
}
