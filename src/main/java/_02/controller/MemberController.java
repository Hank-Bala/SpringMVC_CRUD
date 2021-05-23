package _02.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import _02.service.CategoryService;
import _02.service.HobbyService;
import _02.service.MemberService;

@Controller
@RequestMapping("/_02_member")
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	HobbyService hobbyService;
	
	@Autowired
	CategoryService categoryService;
	
	// 功能：顯示所有會員資料
	@GetMapping("/showAllMembers")
	public String list(Model model) {
		model.addAttribute("members", memberService.getAllMembers());
		return "_02_member/showAllMembers";
	}

	// 功能：依照前端送來的Member Id 刪除一筆紀錄
	@DeleteMapping("/mem/{id}")
	public String delete(@PathVariable("id") Integer id) {
		memberService.delete(id);
		return "redirect:/_02_member/showAllMembers";
	}
	
	// 功能：定義前端傳送之日期的格式以便Spring MVC將其轉換java.util.Date物件
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}
