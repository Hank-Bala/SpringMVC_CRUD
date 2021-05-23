package _00_init.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.ServletContext;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import _02.model.Category;
import _02.model.Hobby;
import _02.model.Member;
import _02.service.CategoryService;
import _02.service.HobbyService;
import _02.service.MemberService;

@Controller
public class DatabaseInitializationController {
	String [][] categoryData = {
	   {"101", "有學籍學生", "學"}, 
	   {"111", "公務人員", "公"},	
	   {"121", "軍職人員", "軍"},
	   {"131", "自由業", "自"},	
	   {"141", "家庭主婦", "家"}
	};
	String [][] hobbyData = {
			   {"1", "電腦/手機遊戲", "才藝"}, 
			   {"2", "游泳", "運動"},	
			   {"3", "登山", "運動"},
			   {"4", "寫程式", "才藝"},	
			   {"5", "健行", "運動"},
			   {"6", "逛街", "休閒"}
			};
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	HobbyService hobbyService;
	
	
	@GetMapping({"/datainit", "/initdata"})
	public String dbInit(RedirectAttributes ra) {
		// truncate Member Table
		memberService.truncateTable();
		
		// truncate Category Table
		categoryService.truncateTable();
		// truncate Hobby Table		
		hobbyService.truncateTable();
		
		// Insert categoryData Data
		for(int n = 0; n < categoryData.length; n++) {
			Category category = new Category(Integer.parseInt(categoryData[n][0].trim()), categoryData[n][1], categoryData[n][2]);
			categoryService.save(category);
		}
		
		// Insert Hobby Data
		for(int n = 0; n < hobbyData.length; n++) {
			Hobby hobby = new Hobby(Integer.parseInt(hobbyData[n][0].trim()), hobbyData[n][1], hobbyData[n][2]);
			hobbyService.save(hobby);
		}
		
		try (
			// 由讀入/WEB-INF/data/Input.txt檔案中Member的資料，然後寫入資料庫
			InputStream is = servletContext.getResourceAsStream("/data/memberCRUD.txt");	
			InputStreamReader isr0 = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr0);
		)
		{
			String line = "";
			while ((line = br.readLine()) != null) {
				// 未處理BOM字元，若有需要，請自行加入
				String[] sa = line.split("\\|");
				Member member = new Member();
				member.setAccount(sa[0]);
				member.setName(sa[1]);
				member.setEmail(sa[2]);
				member.setBirthday(Date.valueOf(sa[3]));
				member.setWeight(Double.parseDouble(sa[4]));
				Hobby hobby = hobbyService.getHobby(Integer.parseInt(sa[5]));
				System.out.println("hobby=" + hobby + ", key=" + Integer.parseInt(sa[5]));
				member.setHobby(hobby);
					
				Category category = categoryService.getCategory(Integer.parseInt(sa[6]));
				System.out.println("category=" + category + ", key=" + Integer.parseInt(sa[6]));
				member.setCategory(category);
					
				member.setGender(sa[7]);
				// --------------處理Blob(圖片)欄位----------------
//				File f = new File(sa[8]);
//				System.out.println(f.getAbsolutePath());
//				long size = f.length();
//				InputStream is = new FileInputStream(f);
				InputStream isImage = servletContext.getResourceAsStream(sa[8]);
				
//				System.out.println("isImage=" + isImage + ", " + sa[8]);
				Blob sb = inputStreamToBlob(isImage);
				member.setImage(sb);
				String imageFileName = sa[8].substring(sa[8].lastIndexOf("/") + 1);
				member.setFileName(imageFileName);
				Timestamp adminTime = new Timestamp(System.currentTimeMillis());
				member.setAdmissionTime(adminTime);
				memberService.save(member);
//				System.out.println("新增記錄:" + member);
				
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
		ra.addFlashAttribute("DataReset", "重置Category與Hobby資料完畢");
		return "redirect:/";
	} 

	private Blob inputStreamToBlob(InputStream is) {
		Blob blob = null ;
	 
		try 	{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] b = new byte[8192];
			int len = 0 ;
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			blob = new SerialBlob(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blob;
	}
}