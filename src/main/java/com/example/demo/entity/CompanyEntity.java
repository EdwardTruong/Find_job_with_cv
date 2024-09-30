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


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="company")
public class CompanyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="address")
	@NotBlank(message = "Nhập địa chỉ.")
	private String address;

    @Column(name="description")
	private String description;

    @Column(name="email")
    @NotBlank(message = "Nhập địa chỉ mail")
	@Email(message = "Địa chỉ mail không đúng định dạng !", regexp = "^[\\w-\\+]+(\\.[\\w-\\+]+)*@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}$")
	private String email;

	@Column(name = "logo")
	private String logo;

    @Column(name="name_company",columnDefinition = "nvarchar(100)")
    @NotBlank(message = "Nhập tên Công ty.")
	private String name;

    @Column(name="phone_number")
    @Pattern(regexp = "^\\d{3,15}", message = "Chỉ nhật số. Ít nhất 3 nhiều nhất là 15 số.")
	private String phoneNumber;

    @Column(name="status")
	private boolean status;

    @OneToOne()
    @JoinColumn(name="user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "companyEntity", fetch = FetchType.EAGER)
    private List<RecruitmentEntity> recruitmentListEntity;

    public void addRecruitment(RecruitmentEntity recruitment){
        if( recruitmentListEntity == null){
            recruitmentListEntity = new ArrayList<>();
        }
        recruitmentListEntity.add(recruitment);
        recruitment.setCompanyEntity(this);
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
        name="follow_companny",
        joinColumns = @JoinColumn(name="company_id"),
        inverseJoinColumns = @JoinColumn(name="user_id")
    )
    private List<FollowCompany> listUserFollow;
    
    @Override
    public String toString() {
        return "CompanyEntity [id=" + id + ", address=" + address + ", description=" + description + ", email=" + email
                + ", nameCompany=" + name + ", phoneNumber=" + phoneNumber
                + ", status=" + status + ", user=" + user.getId() + "]";
    } 
}
