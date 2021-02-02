package batch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
public class SavingsAccount {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal balance;

    public SavingsAccount() {
    }

    public BigDecimal balance(){
        return balance;
    }

    public SavingsAccount(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavingsAccount that = (SavingsAccount) o;
        return Objects.equals(id, that.id) && (balance.compareTo(((SavingsAccount) o).balance) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance.doubleValue());
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}
