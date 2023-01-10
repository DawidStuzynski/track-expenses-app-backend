package pl.byczazagroda.trackexpensesappbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.service.FinancialTransactionService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService financialTransactionService;


    @GetMapping()
    ResponseEntity<List<FinancialTransactionDTO>> getFinancialTransactionsByWalletId(@RequestParam @Min(1) @NotNull Long walletId) {
        List<FinancialTransactionDTO> financialTransactionDTOList = financialTransactionService.getFinancialTransactionsByWalletId(walletId);
        return new ResponseEntity<>(financialTransactionDTOList, HttpStatus.OK);
    }
}
