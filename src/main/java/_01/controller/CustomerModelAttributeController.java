package _01.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import _01.model.CustomerBean;
import _01.service.CustomerService;
import _01.validate.CustomerValidator;

@Controller
@RequestMapping("/_01_customer")
public class CustomerModelAttributeController {

	@Autowired
	ServletContext context;

	@Autowired
	CustomerService service;
	
	@GetMapping("/modifyCustomer/{id}")
	public String editCustomerForm(Model model, @PathVariable Integer id) {
		CustomerBean bean = service.getCustomerById(id);
		bean.setPassword1(bean.getPassword());
		model.addAttribute("customerBean", bean);
		return "_01_customer/EditCustomerForm";
	}

	@GetMapping("/insertCustomer")
	public String showCustomerForm(Model model) {
		return "_01_customer/InsertCustomerForm";
	}
	// 
	@PostMapping("/insertCustomer")
	public String insertCustomerData(
		@ModelAttribute CustomerBean bean 
		, BindingResult bindingResult 
		) {
		new CustomerValidator().validate(bean, bindingResult);
		System.out.println("新增會員: " + bean);
		    
		if (bindingResult.hasErrors()) {
			System.out.println("======================");
			List<ObjectError> list = bindingResult.getAllErrors();
			for(ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			System.out.println("======================");
			return "_01_customer/InsertCustomerForm";
		}

		if (bean.getCustomerId() != null ) {
			service.updateCustomer(bean);	
		} 
		bean.setRegisterTime(new Timestamp(System.currentTimeMillis()));
		service.save(bean);
		return "redirect:customers";
	}
	

	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		// java.util.Date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		dateFormat.setLenient(false);
		CustomDateEditor ce = new CustomDateEditor(dateFormat, true); 
		binder.registerCustomEditor(Date.class, ce);
		// java.sql.Date		
		DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat2.setLenient(false);
		CustomDateEditor ce2 = new CustomDateEditor(dateFormat2, true); 
		binder.registerCustomEditor(java.sql.Date.class, ce2);
	}
//	@PutMapping(value="/modifyCustomer/{id2}", consumes = "application/x-www-form-urlencoded")
	@PostMapping(value="/modifyCustomer/{id2}", consumes = "application/x-www-form-urlencoded")
	public String modifyCustomerData2(
			@ModelAttribute("customerBean") CustomerBean bean 
			, BindingResult bindingResult 
			) {
			new CustomerValidator().validate(bean, bindingResult);
			    
			if (bindingResult.hasErrors()) {
				List<ObjectError> list = bindingResult.getAllErrors();
				for(ObjectError error : list) {
					System.out.println(error);
				}
				System.out.println("當表單資料有誤時，bean==>" + bean);
				// 如果使用@PutMapping來修飾本方法，會於修改後之資料無法通過驗證時，
				// 執行下列敘述之對應的JSP時發生例外：
				// HTTP Status 405 – Method Not Allowed
				// JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS
				// 只好將@PutMapping 改為 @PostMapping
				return "_01_customer/EditCustomerForm";

			}
			service.updateCustomer(bean);	
			return "redirect:/_01_customer/customers";
		}
	@ModelAttribute
	public CustomerBean getCustomerData(@RequestParam(value="customerId", required = false) Integer id) {
		CustomerBean cbean = null;
		if (id != null) {
			System.out.println("----------------edit Customer ---------------------");
			cbean = service.getCustomerById(id);
			System.out.println("在@ModelAttribute修飾的方法 getCustomerBean()中，讀到物件:" + cbean);
		} else {
			System.out.println("----------------insert Customer ---------------------");
			cbean = new CustomerBean();
			System.out.println("在@ModelAttribute修飾的方法 getCustomerBean()中，無法讀取物件:" + cbean);
		}
		return cbean;
	}
	
	
}
