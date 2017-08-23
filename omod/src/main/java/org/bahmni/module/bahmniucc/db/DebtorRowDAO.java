package org.bahmni.module.bahmniucc.db;

import org.bahmni.module.bahmniucc.model.Debtor;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public interface DebtorRowDAO {

    public void saveRow(Debtor debtor);

    public void deleteRows();

}
