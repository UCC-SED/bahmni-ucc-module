package org.bahmni.module.bahmniucc.db;

import org.bahmni.module.bahmniucc.model.DebtorRow;

import java.util.List;

/**
 * Created by ucc-ian on 22/Aug/2017.
 */
public interface DebtorRowDAO {

    public void saveDebtorRow(List<DebtorRow> results);

    /**
     * Clear all the results
     */
    public void clearAllResults();

    public String readQuery();


    public void insertHack(String visitType);

    public void clearHack();

    public String readHack();


}
