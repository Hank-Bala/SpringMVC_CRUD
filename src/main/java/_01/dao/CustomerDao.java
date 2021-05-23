package _01.dao;

import java.util.List;

import _01.model.CustomerBean;

public interface CustomerDao {
	CustomerBean getCustomerById(int id);
	
	List<CustomerBean> getCustomers();

	Object save(CustomerBean bean);
	
	void updateCustomer(CustomerBean bean); 

	void deleteCustomerByPrimaryKey(int key);
}
