package _01.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import _01.dao.CustomerDao;
import _01.model.CustomerBean;
import _01.service.CustomerService;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

	CustomerDao custDao;
	
	@Autowired
	public void setCustDao(CustomerDao custDao) {
		this.custDao = custDao;
	}

	@Override
	public CustomerBean getCustomerById(int id) {
	
		return custDao.getCustomerById(id);
	}

	@Override
	public List<CustomerBean> getCustomers() {
		return custDao.getCustomers();
	}

	@Override
	public Object save(CustomerBean bean) {
		return custDao.save(bean);
	}

	@Override
	public void updateCustomer(CustomerBean bean) {
		custDao.updateCustomer(bean);
	}

	@Override
	public void deleteCustomerByPrimaryKey(int key) {
		custDao.deleteCustomerByPrimaryKey(key);
	}
}
