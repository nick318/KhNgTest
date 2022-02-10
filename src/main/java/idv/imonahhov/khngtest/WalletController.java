package idv.imonahhov.khngtest;

import idv.imonahhov.khngtest.entities.Currency;
import idv.imonahhov.khngtest.entities.TransAction;
import idv.imonahhov.khngtest.entities.Wallet;
import idv.imonahhov.khngtest.repositories.CurrencyRepository;
import idv.imonahhov.khngtest.repositories.TransActionRepository;
import idv.imonahhov.khngtest.repositories.WalletRepository;
import idv.imonahhov.khngtest.requests.TransActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@RestController
public class WalletController {

    private CurrencyRepository currencyRepository;
    private TransActionRepository transActionRepository;
    private WalletRepository walletRepository;
    private TokenMap tokenMap;

    @Autowired
    public WalletController(CurrencyRepository currencyRepository, TransActionRepository transActionRepository, WalletRepository walletRepository, TokenMap tokenMap) {
        this.currencyRepository = currencyRepository;
        this.transActionRepository = transActionRepository;
        this.walletRepository = walletRepository;
        this.tokenMap = tokenMap;
    }

    @PostMapping("/transaction")
    public ResponseEntity createDirectory(@RequestBody TransActionRequest request){
        Wallet source = walletRepository.findById(request.getSource()).get();
        Wallet target = walletRepository.findById(request.getTarget()).get();
        Currency currency = currencyRepository.findCurrencyBySymbol(request.getCurrency());
        if(source == null){
            return ResponseEntity.notFound().build();
        }
        if(source == target){
            return ResponseEntity.notFound().build();
        }
        if(currency == null){
            return ResponseEntity.notFound().build();
        }
        if(!tokenMap.validateToken(request.getSource(),request.getToken())){
            return ResponseEntity.badRequest().build();
        }
        if(source.getBalance().get(currency) == null){
            source.getBalance().put(currency, BigDecimal.ZERO);
        }
        if(target.getBalance().get(currency) == null){
            target.getBalance().put(currency, BigDecimal.ZERO);
        }
        walletRepository.save(source);
        walletRepository.save(target);
        if(source.getBalance().get(currency).subtract(request.getAmount()).doubleValue() < 0){
            return ResponseEntity.badRequest().build();
        }
        walletTransaction(request,source,target);
        return ResponseEntity.ok().body("created");
    }
    @Transactional(rollbackOn = Exception.class)
    public void walletTransaction(TransActionRequest request, Wallet source, Wallet target){
        Currency currency = currencyRepository.findCurrencyBySymbol(request.getCurrency());
        TransAction transAction = new TransAction();
        transAction.setSource(source);
        transAction.setTarget(target);
        transAction.setAmount(request.getAmount());
        transAction.setCurrency(currency);
        transAction.setTime(System.currentTimeMillis());

        source.getBalance().get(currency).subtract(request.getAmount());
        target.getBalance().get(currency).add(request.getAmount());

        transActionRepository.save(transAction);
        walletRepository.save(source);
        walletRepository.save(target);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity getBalance(@PathVariable Long id){
        Wallet wallet = walletRepository.findById(id).get();
        if(wallet == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(wallet);
    }

    @GetMapping("/token/{id}")
    public ResponseEntity getToken(@PathVariable Long id){
        return ResponseEntity.ok().body(tokenMap.createTokenForId(id));
    }
}
