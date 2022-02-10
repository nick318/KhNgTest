package idv.imonahhov.khngtest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class TransAction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonIgnore
    private
    Long id;
    @OneToOne
    private
    Wallet source;
    @OneToOne
    private
    Wallet target;
    private BigDecimal amount;
    @OneToOne
    private
    Currency currency;
    private Long time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getSource() {
        return source;
    }

    public void setSource(Wallet source) {
        this.source = source;
    }

    public Wallet getTarget() {
        return target;
    }

    public void setTarget(Wallet target) {
        this.target = target;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
