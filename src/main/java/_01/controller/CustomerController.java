package _01.controller;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import _01.model.CustomerBean;
import _01.service.CustomerService;

@Controller
@RequestMapping("/_01_customer")
public class CustomerController {

	@Autowired
	ServletContext context;

	@Autowired
	CustomerService service;
	
	@GetMapping("/customers")
	public String getCustomers(Model model) {
		List<CustomerBean> beans = service.getCustomers();
		model.addAttribute(beans);      
		// 若屬性物件為CustomerBean型別的物件，則預設的識別字串 ==> customerBean
		// 若屬性物件為List<CustomerBean>型別的物件，則預設的識別字串 ==> customerBeanList
		return "_01_customer/ShowCustomers";
	}
	


	
	@DeleteMapping(value="/modifyCustomer/{id}")
	public String deleteCustomerData(@PathVariable Integer id) {
		service.deleteCustomerByPrimaryKey(id);	
		return "redirect:/_01_customer/customers";
	}
	
	
	@RequestMapping("/index")
	public String home() {
		return "_01_customer/index";
	}
}
