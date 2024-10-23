package com.woking.demo.entity;



import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name="recruitment")
public class RecruitmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="address")
    @NotBlank(message = "Yêu cầu địa chỉ.")
    private String address;

    @Column(name="description")
    @NotBlank(message = "Yêu cầu thông tin miêu tả.")
    private String description; 

    @Column(name="experience")
    @NotBlank(message =  "Yêu cầu cần thông tin về kinh nghiệm.")
    private String experience;

    @Column(name="quantity")
    @Min(value = 1, message = "Giá trị phải lớn hơn hoặc bằng 1.")
    private int quantity;

    @Column(name="rank_at")
    @NotBlank(message =  "Yêu cầu cần thông tin về kinh nghiệm.")
    private String rankAt;

    @Column(name="salary")
    @Size(min = 10, message = "Yêu cầu điền thông tin thu nhập, thấp nhất là 10,000 VND.")
//    @MoneyFormat(message = "Không để trống và kết thúc bằng VND")
    private String salary;

    @Column(name="title")
    @NotBlank(message = "Yêu cầu điền thông tin tiêu đề.")
    private String title; 

    @Column(name="type")
    @NotBlank(message = "Yêu cầu có thông tin hình thức làm việc.")
    private String type;

    @Column(name="view")
    private int view;

    @Column(name="status")
	private boolean status;

    @Column(name="created_at")
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
 
    @Column(name="deadline")
    @Temporal(value = TemporalType.DATE)
    @NotNull(message = "Yêu cầu có ngày kết thúc.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    @ManyToOne
    @JoinColumn( name="company_id")
    private CompanyEntity companyEntity;
    
    @ManyToOne
    @NotNull(message = "Yêu cầu danh mục công việc.")
    @JoinColumn(name="category_id")
    private CategoryEntity category;

    @ManyToMany()
	@JoinTable(
		name="save_job",
		joinColumns = @JoinColumn(name="recruitment_id"),
		inverseJoinColumns = @JoinColumn(name="user_id")
	)
	List<UserEntity> listUserSavedThisJob;

    @ManyToMany()
    @JoinTable(
    name="applyjob",
    joinColumns = @JoinColumn(name="recruitment_id"),
    inverseJoinColumns = @JoinColumn(name="user_id")
    )   
    List<UserEntity> listUserApplyThisJob;

	@Override
	public String toString() {
		return "RecruitmentEntity [id=" + id + ", address=" + address + ", description=" + description + ", experience="
				+ experience + ", quantity=" + quantity + ", rankAt=" + rankAt + ", salary=" + salary + ", title="
				+ title + ", type=" + type + ", view=" + view + ", status=" + status + ", createdAt=" + createdAt
				+ ", deadline=" + deadline + ", category=" + category + "]";
	}

    
}
