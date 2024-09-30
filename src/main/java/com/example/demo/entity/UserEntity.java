package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserEntity
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user")
public class UserEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="email")
	@NotBlank(message = "Nhập địa chỉ mail")
	@Email(message = "Địa chỉ mail không đúng định dạng !", regexp = "^[\\w-\\+]+(\\.[\\w-\\+]+)*@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}$")
    private String email;

    @Column(name="password")
    private String password;
    
    @Column(name="address")
	@NotBlank(message = "Nhập địa chỉ.")
	private String address;

    @Column(name="full_name")
	@NotBlank(message = "Nhập họ và tên.")
    private String fullName;

    @Column(name="phone_number")
	@Pattern(regexp = "^\\d{3,12}", message = "Chỉ nhật số. Ít nhất 3 nhiều nhất là 15 số.")
	private String phoneNumber;
	
	@Column(name="description")
	private String description;

	@Column(name="avatar")
	private String avatar;

    @Column(name ="status")
    private boolean status;
	
	@OneToOne(mappedBy = "user")
	private CompanyEntity companyEntity;
    
	public void addRole(RoleEntity role){
		if(this.authorities == null){
			authorities = new ArrayList<>();
		}
		authorities.add(role);
	}

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable(
			name = "users_roles",
			joinColumns=@JoinColumn(name="user_id"),
			inverseJoinColumns =@JoinColumn(name="role_id")
			)
    private List<RoleEntity> authorities;

	
	@OneToMany(mappedBy = "user",  fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private List<CvEntity> listCvEntity;

	public void addCv (CvEntity cvEntity) {
		if(listCvEntity == null) {
			listCvEntity = new ArrayList<>();
		}
            this.listCvEntity.add(cvEntity);
            cvEntity.setUser(this);
            System.out.println("Check User Entity : adCv ");
	}


	@ManyToMany()
	@JoinTable(
		name="follow_companny",
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="company_id")
		)
	private List<FollowCompany> listCompanyFollower;

	@ManyToMany()
	@JoinTable(
		name="save_job",
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="recruitment_id")
	)
	private List<SaveJob> listSavejob;

	
	@ManyToMany(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable(
    name="applyjob",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="recruitment_id")
	)
	List<Applyjob> listAppliedOfUser;

    
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", address=" + address + ", email=" + email + ", fullName=" + fullName
				+ ", password=" + password + ", phoneNumber=" + phoneNumber + ", description=" + description
				+ ", status=" + status + ", roles=" + authorities.get(0)+ "]";
	}


}