package idv.imonahhov.khngtest.repositories;

import idv.imonahhov.khngtest.entities.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency,Integer> {
    public Currency findCurrencyBySymbol(String symbol);
}
