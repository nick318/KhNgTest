package idv.imonahhov.khngtest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private
    Long id;
    @ElementCollection
    private
    Map<Currency, BigDecimal> balance;
    @OneToOne
    private
    User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Currency, BigDecimal> getBalance() {
        return balance;
    }

    public void setBalance(Map<Currency, BigDecimal> balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
