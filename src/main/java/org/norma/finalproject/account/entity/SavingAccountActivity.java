package org.norma.finalproject.account.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.common.entity.AccountActivity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class SavingAccountActivity extends AccountActivity {

    @ManyToOne
    private SavingAccount savingAccount;

}
