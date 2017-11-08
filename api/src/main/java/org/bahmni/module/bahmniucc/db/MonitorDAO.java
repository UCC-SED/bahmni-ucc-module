package org.bahmni.module.bahmniucc.db;

import org.bahmni.module.bahmniucc.model.DebtorRow;

import java.util.List;

/**
 * Created by ucc-ian on 09/Oct/2017.
 */
public interface MonitorDAO {


    public String readQueries(List<String> queries);

    /**
     * Clear all the results
     */
    public String readQuery();



}
