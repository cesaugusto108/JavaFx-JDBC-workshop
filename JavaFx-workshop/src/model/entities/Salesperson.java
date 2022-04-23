package model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Salesperson implements Serializable {

    @Serial
    private static final long serialVersionUID = 2724058024214740941L;

    private Integer id;

    private String name;

    private String email;

    private Date birthdate;

    private Double baseSalary;

    private Integer departmentId;

    private Department department;

    public Salesperson() {
    }

    public Salesperson(Integer id, String name, String email, Date birthdate, Double baseSalary, Integer departmentId, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.baseSalary = baseSalary;
        this.departmentId = departmentId;
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salesperson that)) return false;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Salesperson id: " + id + ";\n"
                + "Name: " + name + ";\n"
                + "Email: " + email + ";\n"
                + "Birthdate: " + birthdate + ";\n"
                + "Base salary: " + baseSalary + ";\n"
                + "Department: " + department;
    }
}
