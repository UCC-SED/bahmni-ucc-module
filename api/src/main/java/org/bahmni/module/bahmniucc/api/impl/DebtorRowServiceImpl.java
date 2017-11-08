package org.bahmni.module.bahmniucc.api.impl;

import org.bahmni.module.bahmniucc.api.DebtorRowService;
import org.bahmni.module.bahmniucc.db.DebtorRowDAO;
import org.bahmni.module.bahmniucc.model.DebtorRow;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ucc-ian on 24/Aug/2017.
 */
public class DebtorRowServiceImpl  extends BaseOpenmrsService implements DebtorRowService {

    private DebtorRowDAO debtorRowDAO;

    public void setDebtorRowDAO(DebtorRowDAO debtorRowDAO) {
        this.debtorRowDAO = debtorRowDAO;
    }

    @Override
    @Transactional(readOnly = false)
    public void saveDebtorRow(List<DebtorRow> results) {
        debtorRowDAO.saveDebtorRow(results);
    }

    @Override
    @Transactional(readOnly = false)
    public void clearAllResults() {
        debtorRowDAO.clearAllResults();
    }


}
