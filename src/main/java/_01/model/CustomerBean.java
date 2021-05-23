package _01.model;

import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Customer")
public class CustomerBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerId;
	private String name;
	private String password;
	@Transient
	private String password1;
	private String email;
	private Date birthday;
	private java.util.Date lastPostTime;
	private Timestamp registerTime;
	
	private Double totalPayment;
	private Blob photo;
	
	public CustomerBean(Integer customerId, String name) {
		this.customerId = customerId;
		this.name = name;
	}

	public CustomerBean() {  
		
	}

	public Integer getCustomerId() {   // customerId
		return customerId;
	}
	
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public java.util.Date getLastPostTime() {
		return lastPostTime;
	}

	public void setLastPostTime(java.util.Date lastPostTime) {
		this.lastPostTime = lastPostTime;
	}

	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	public Blob getPhoto() {
		return photo;
	}

	public void setPhoto(Blob photo) {
		this.photo = photo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerBean [customerId=");
		builder.append(customerId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", password=");
		builder.append(password);
		builder.append(", password1=");
		builder.append(password1);
		builder.append(", email=");
		builder.append(email);
		builder.append(", birthday=");
		builder.append(birthday);
		builder.append(", lastPostTime=");
		builder.append(lastPostTime);
		builder.append(", registerTime=");
		builder.append(registerTime);
		builder.append(", totalPayment=");
		builder.append(totalPayment);
		builder.append(", photo=");
		builder.append(photo);
		builder.append(", hashcode=");
		builder.append(hashCode());
		builder.append("]");
		return builder.toString();
	}

}
