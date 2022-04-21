package model.services;

import model.dao.DaoFactory;
import model.dao.SalespersonDao;
import model.entities.Salesperson;

import java.util.List;

public class SalespersonService {

    SalespersonDao salespersonDao = DaoFactory.createSalespersonDao();

    public List<Salesperson> findAll() {

        return salespersonDao.findAll();
    }

    public void saveOrUpdate(Salesperson salesperson) {

        if (salesperson.getId() == null) {

            salespersonDao.insert(salesperson);
        } else {

            salespersonDao.update(salesperson);
        }
    }

    public void remove(Salesperson salesperson) {

        salespersonDao.deleteById(salesperson.getId());
    }
}
