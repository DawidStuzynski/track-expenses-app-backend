package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.validation.annotation.Validated;
import pl.byczazagroda.trackexpensesappbackend.controller.FinancialTransactionController;
import pl.byczazagroda.trackexpensesappbackend.dto.FinancialTransactionDTO;
import pl.byczazagroda.trackexpensesappbackend.exception.ApiExceptionBase;
import pl.byczazagroda.trackexpensesappbackend.mapper.FinancialTransactionModelMapper;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransaction;
import pl.byczazagroda.trackexpensesappbackend.model.FinancialTransactionType;
import pl.byczazagroda.trackexpensesappbackend.model.Wallet;
import pl.byczazagroda.trackexpensesappbackend.repository.FinancialTransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@Validated
@WebMvcTest(
        controllers = FinancialTransactionController.class,
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {FinancialTransactionRepository.class, FinancialTransactionServiceImpl.class}))
class FinancialTransactionServiceImplTest {
    public static final long ID_1L = 1L;
    public static final long ID_2L = 2L;

    public static final long ID_10L = 10L;
    public static final FinancialTransactionType TYPE = FinancialTransactionType.EXPENSE;
    public static final Instant DATE_NOW = Instant.now();

    @MockBean
    private ApiExceptionBase apiExceptionBase;

    @MockBean
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionServiceImpl financialTransactionService;

    @MockBean
    private FinancialTransactionModelMapper financialTransactionModelMapper;


    @Test
    @DisplayName("when finding with proper financial transaction id should successfully find transaction")
    void shouldSuccessfullyFindFinancialTransaction_WhenFindingWithProperTransactionIdy() {
        //given

        FinancialTransaction financialTransaction1 = new FinancialTransaction();
        financialTransaction1.setId(ID_1L);
        financialTransaction1.setFinancialTransactionType(TYPE);
        financialTransaction1.setTransactionDate(DATE_NOW);

        FinancialTransactionDTO financialTransactionDTO1 = new FinancialTransactionDTO(ID_1L, BigDecimal.ONE, "desc", TYPE, DATE_NOW);

        FinancialTransaction financialTransaction2 = new FinancialTransaction();
        financialTransaction2.setId(ID_2L);
        financialTransaction2.setFinancialTransactionType(TYPE);
        financialTransaction2.setTransactionDate(DATE_NOW);

        FinancialTransactionDTO financialTransactionDTO2 = new FinancialTransactionDTO(ID_2L, BigDecimal.ONE, "desc", TYPE, DATE_NOW);

        List<FinancialTransaction> financialTransactionsList = new ArrayList<>();
        financialTransactionsList.add(financialTransaction1);
        financialTransactionsList.add(financialTransaction2);

        Wallet wallet = new Wallet("Random wallet");
        wallet.setId(ID_1L);
        wallet.setCreationDate(DATE_NOW);

        financialTransaction1.setWallet(wallet);
        financialTransaction2.setWallet(wallet);

        wallet.setFinancialTransactionList(financialTransactionsList);


        //when
        when(financialTransactionRepository.findAllByWalletIdOrderByTransactionDateDesc(ID_1L)).thenReturn(financialTransactionsList);
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction1))
                .thenReturn(financialTransactionDTO1);
        when(financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction2))
                .thenReturn(financialTransactionDTO2);

        List<FinancialTransactionDTO> returnedFinancialTransactionDTOsList = financialTransactionService.getFinancialTransactionsByWalletId(ID_1L);

        //then
        Assertions.assertEquals(returnedFinancialTransactionDTOsList.get(0), financialTransactionDTO1);
        Assertions.assertEquals(returnedFinancialTransactionDTOsList.get(1), financialTransactionDTO2);
    }


}