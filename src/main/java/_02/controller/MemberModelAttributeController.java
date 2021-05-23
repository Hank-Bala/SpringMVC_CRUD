package _02.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import _02.model.Category;
import _02.model.Hobby;
import _02.model.Member;
import _02.service.CategoryService;
import _02.service.HobbyService;
import _02.service.MemberService;
import _02.validators.MemberValidator;

@Controller
@RequestMapping("/_02_member")
public class MemberModelAttributeController {
	// 當缺少圖片時傳送下列圖片。它們是相對於應用系統根目錄的路徑
	Logger logger = LoggerFactory.getLogger(MemberModelAttributeController.class);
	String noImage = "/data/images/NoImage.png";
	String noImageFemale = "/data/images/NoImage_Female.jpg";
	String noImageMale = "/data/images/NoImage_Male.png";
	@Autowired
	HobbyService hobbyService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	MemberService memberService;

	@Autowired
	ServletContext context;

	// 功能：本方法於使用者執行新增會員資料時送出空白表單讓使用者輸入資料
	// 補充說明：任何方法回傳之網頁若會用到"hobbyList", "categoryList", "genderMap"等三個屬性物件時，
	// 則該方法就應該與『以@ModelAttribute修飾，且能產生上述三個屬性物件的方法』位於同一個類別內
	@GetMapping(value = "/mem")
	public String showEmptyForm(Model model) {
		Member member = new Member();
//      若要避免測試時不斷輸入資料，可加入下列敘述		
//		member.setAccount("a10225");
//		member.setName("Lisa Lee");
//		member.setEmail("www@gmail.com");
//		member.setWeight(65.5);
//		member.setGender("F");
//		member.setBirthday(java.sql.Date.valueOf("1988-7-5"));
		model.addAttribute("member", member);
		// "_02_member/insertMember"網頁會用到"hobbyList", "categoryList", "genderMap"等三個屬性物件
		return "_02_member/insertMember";
	}
	// 功能：使用者提交(Submit)之修改過的會員資料由本方法負責檢核，若檢核無誤則呼叫Service元件更新到對應的紀錄
	// 若有任何錯誤則送回原輸入資料與對應的錯誤訊息，這些訊息會放入原修改資料的表單後送回此表單。
	// 本方法也會需要"hobbyList", "categoryList", "genderMap"等三個屬性物件
	@PostMapping("/mem/{id}")
	// BindingResult 參數必須與@ModelAttribute修飾的參數連續編寫，中間不能夾其他參數
	//
	public String modify(@ModelAttribute("member") Member member, BindingResult result, Model model,
			@PathVariable Integer id, HttpServletRequest request) {
		MemberValidator validator = new MemberValidator();
		validator.validate(member, result);
		if (result.hasErrors()) {
			// 檢核時發現錯誤
			List<ObjectError> list = result.getAllErrors();
			for (ObjectError error : list) {
				System.out.println("有錯誤：" + error);
			}
			// "_02_member/updateMember"網頁會用到"hobbyList", "categoryList", "genderMap"等三個屬性物件
			return "_02_member/updateMember";
		}
		// 檢核後若未發現任何錯誤才會執行下列敘述：		
		Hobby hobby = hobbyService.getHobby(member.getHobby().getId());
		member.setHobby(hobby);

		// 找到對應的Category物件
		Category category = categoryService.getCategory(member.getCategory().getId());
		member.setCategory(category);
		// Admission Time 不允許修改，所以『不要』寫下面兩敘述
//		Timestamp adminTime = new Timestamp(System.currentTimeMillis());
//		member.setAdmissionTime(adminTime);
		// 處理上傳的圖片
		MultipartFile picture = member.getProductImage();

		if (picture.getSize() == 0) {
			// 表示使用者並未挑選圖片
			Member original = memberService.get(id);
			member.setImage(original.getImage());
		} else {
			String originalFilename = picture.getOriginalFilename();
			if (originalFilename.length() > 0 && originalFilename.lastIndexOf(".") > -1) {
				member.setFileName(originalFilename);
			}

			// 建立Blob物件
			if (picture != null && !picture.isEmpty()) {
				try {
					byte[] b = picture.getBytes();
					Blob blob = new SerialBlob(b);
					member.setImage(blob);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
				}
			}
		}
		memberService.update(member);
		return "redirect:/_02_member/showAllMembers";
	}

	// 功能：當使用者按下『修改之超連結』準備修改會員資料時，本方法送回含有會員資料的表單讓使用者修改。
	@GetMapping(value = "/mem/{id}")
	public String showDataForm(@PathVariable("id") Integer id, Model model) {
		Member member = (Member) model.getAttribute("member");
		logger.info("@GetMapping, id=" + id + ", member=" + member.hashCode());
//		Member member = memberService.get(id);
//		model.addAttribute(member);
		return "_02_member/updateMember";
	}
	// 功能：使用者提交(Submit)新增之會員資料後由本方法負責檢核，若檢核無誤則呼叫Service元件寫入資料庫
	// 若有任何錯誤則送回原新增資料的表單，此表單將包含原輸入資料與對應的錯誤訊息
	// 本方法也會需要"hobbyList", "categoryList", "genderMap"等三個屬性物件
	
	@PostMapping(value = "/mem")
	// BindingResult 參數必須與@ModelAttribute修飾的參數連續編寫，中間不能夾其他參數
	public String add(@ModelAttribute("member") /* @Valid */ Member member, BindingResult result, Model model,
			HttpServletRequest request) {
		MemberValidator validator = new MemberValidator();
		// 呼叫Validate進行資料檢查
		validator.validate(member, result);
		if (result.hasErrors()) {
//	          下列敘述可以理解Spring MVC如何處理錯誤			
//				List<ObjectError> list = result.getAllErrors();
//				for (ObjectError error : list) {
//					System.out.println("有錯誤：" + error);
//				}
			return "_02_member/insertMember";
		}
		MultipartFile picture = member.getProductImage();
		String originalFilename = picture.getOriginalFilename();
		if (originalFilename.length() > 0 && originalFilename.lastIndexOf(".") > -1) {
			member.setFileName(originalFilename);
		}
		// 建立Blob物件，交由 Hibernate 寫入資料庫
		if (picture != null && !picture.isEmpty()) {
			try {
				byte[] b = picture.getBytes();
				Blob blob = new SerialBlob(b);
				member.setImage(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		// 必須要找出對應的Hobby物件
		Hobby hobby = hobbyService.getHobby(member.getHobby().getId());
		member.setHobby(hobby);

		// 必須要找出對應的Category物件
		Category category = categoryService.getCategory(member.getCategory().getId());
		member.setCategory(category);

		Timestamp adminTime = new Timestamp(System.currentTimeMillis());
		member.setAdmissionTime(adminTime);

		try {
			memberService.save(member);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			result.rejectValue("account", "", "帳號已存在，請重新輸入");
			return "_02_member/insertMember";
		} catch (Exception ex) {
			System.out.println(ex.getClass().getName() + ", ex.getMessage()=" + ex.getMessage());
			result.rejectValue("account", "", "請通知系統人員...");
			return "_02_member/insertMember";
		}
//			// 將上傳的檔案移到指定的資料夾, 目前註解此功能
//			try {
//				File imageFolder = new File(rootDirectory, "images");
//				if (!imageFolder.exists())
//					imageFolder.mkdirs();
//				File file = new File(imageFolder, "MemberImage_" + member.getId() + ext);
//				productImage.transferTo(file);
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
//			}
		return "redirect:/_02_member/showAllMembers";
	}
	// 功能：依照前端送來的Member Id來讀取會員資料(Member物件)，然後傳回位於該物件內的大頭貼
	@GetMapping("/picture/{id}")
	public ResponseEntity<byte[]> getPicture(@PathVariable("id") Integer id) {
		byte[] body = null;
		ResponseEntity<byte[]> re = null;
		MediaType mediaType = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());

		Member member = memberService.get(id);
		if (member == null) {
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
		String filename = member.getFileName();
		if (filename != null) {
			if (filename.toLowerCase().endsWith("jfif")) {
				mediaType = MediaType.valueOf(context.getMimeType("dummy.jpeg"));
			} else {
				mediaType = MediaType.valueOf(context.getMimeType(filename));
				headers.setContentType(mediaType);
			}
		}
		Blob blob = member.getImage();
		if (blob != null) {
			body = blobToByteArray(blob);
		} else {
			String path = null;
			if (member.getGender() == null || member.getGender().length() == 0) {
				path = noImageMale;
			} else if (member.getGender().equals("M")) {
				path = noImageMale;
			} else {
				path = noImageFemale;
				;
			}
			body = fileToByteArray(path);
		}
		re = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);

		return re;
	}

	@ModelAttribute
	public void commonData(Model model) {
		List<Hobby> hobbyList = hobbyService.getAllHobbies();
		List<Category> categoryList = categoryService.getAllCategories();
		Map<String, String> genderMap = new HashMap<>();
		genderMap.put("M", "Male");
		genderMap.put("F", "Female");
		model.addAttribute("hobbyList", hobbyList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("genderMap", genderMap);
	}

	@ModelAttribute
	public void getMember(@PathVariable(value = "id", required = false) Integer id,
			@RequestParam(value = "id", required = false) Integer rpId, Model model) {
		logger.info("@ModelAttribute, getMember, id=" + id);
		if (id != null) {
			Member member = memberService.get(id);
			model.addAttribute("member", member);
			
			logger.info(" ========PathVariable: id != null;===============, hashCode=" + member.hashCode());
		} else if (rpId != null) {
			Member member = memberService.get(rpId);
			model.addAttribute("member", member);
			logger.info(" ========RequestParam: id != null;===============, hashCode=" +  member.hashCode());
		} else {
			Member member = new Member();
			member.setLogin("false");
			model.addAttribute("member", member);
			logger.info(" ======== id == null;===============, hashCode=" +  member.hashCode());
		}
	}

	private byte[] fileToByteArray(String path) {
		byte[] result = null;
		try (InputStream is = context.getResourceAsStream(path);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			byte[] b = new byte[819200];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			result = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private byte[] blobToByteArray(Blob blob) {
		byte[] result = null;
		try (InputStream is = blob.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			byte[] b = new byte[819200];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			result = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

}
