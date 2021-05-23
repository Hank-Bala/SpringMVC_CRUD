package _01.service;

import java.util.List;

import _01.model.CustomerBean;

public interface CustomerService {
	CustomerBean getCustomerById(int id);

	List<CustomerBean> getCustomers();

	Object save(CustomerBean bean);
	
	void updateCustomer(CustomerBean bean); 

	void deleteCustomerByPrimaryKey(int key);
	
}
