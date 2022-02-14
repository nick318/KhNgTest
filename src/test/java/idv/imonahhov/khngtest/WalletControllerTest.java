package idv.imonahhov.khngtest;

import idv.imonahhov.khngtest.entities.Currency;
import idv.imonahhov.khngtest.entities.Wallet;
import idv.imonahhov.khngtest.repositories.CurrencyRepository;
import idv.imonahhov.khngtest.repositories.TransActionRepository;
import idv.imonahhov.khngtest.repositories.WalletRepository;
import idv.imonahhov.khngtest.requests.TransActionRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

class WalletControllerTest {
    @Test
    void shouldTransferFromOneAccountToAnother() {
        //given
        CurrencyRepository currencyRepository = Mockito.mock(CurrencyRepository.class);
        TransActionRepository transActionRepository = Mockito.mock(TransActionRepository.class);
        WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
        TokenMap tokenMap = new TokenMap();
        WalletController walletController = new WalletController(currencyRepository, transActionRepository, walletRepository, tokenMap);

        TransActionRequest transActionRequest = new TransActionRequest();
        transActionRequest.setAmount(BigDecimal.valueOf(10));
        transActionRequest.setCurrency("EUR");
        transActionRequest.setSource(1L);
        transActionRequest.setTarget(2L);
        transActionRequest.setToken(1L);

        Wallet source = new Wallet();
        source.setId(1L);
        source.setBalance(new HashMap<Currency, BigDecimal>() {{
            Currency currencyEur = getCurrencyEur();
            put(currencyEur, BigDecimal.valueOf(10));
        }});

        Wallet target = new Wallet();
        target.setId(2L);
        target.setBalance(new HashMap<Currency, BigDecimal>() {{
            Currency currencyEur = getCurrencyEur();
            put(currencyEur, BigDecimal.ZERO);
        }});

        ArgumentCaptor<Wallet> argument = ArgumentCaptor.forClass(Wallet.class);
        Mockito.when(currencyRepository.findCurrencyBySymbol(Mockito.anyString())).thenReturn(getCurrencyEur());

        //when
        walletController.walletTransaction(transActionRequest, source, target);

        //then
        Mockito.verify(walletRepository, Mockito.times(2)).save(argument.capture());
        Assertions.assertThat(
                new ArrayList<>(argument.getValue()
                        .getBalance()
                        .values())
                        .get(0)
        ).isEqualTo(BigDecimal.valueOf(10));
    }

    private Currency getCurrencyEur() {
        Currency currency = new Currency();
        currency.setId(1);
        currency.setName("EUR");
        currency.setSymbol("EUR");
        currency.setRate("1");
        return currency;
    }
}