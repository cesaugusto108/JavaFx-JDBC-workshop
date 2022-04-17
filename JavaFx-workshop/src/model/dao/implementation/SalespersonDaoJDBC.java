package model.dao.implementation;

import db.DBException;
import model.dao.SalespersonDao;
import model.entities.Department;
import model.entities.Salesperson;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalespersonDaoJDBC implements SalespersonDao {

    private final Connection CONNECTION;

    public SalespersonDaoJDBC(Connection CONNECTION) {
        this.CONNECTION = CONNECTION;
    }

    @Override
    public void insert(Salesperson salesperson) {
        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("INSERT INTO salesperson " +
                                "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                                "VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, salesperson.getName());
            preparedStatement.setString(2, salesperson.getEmail());
            preparedStatement.setDate(3, salesperson.getBirthDate());
            preparedStatement.setDouble(4, salesperson.getBaseSalary());
            preparedStatement.setInt(5, salesperson.getDepartmentId());

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    salesperson.setId(id);
                }

                resultSet.close();
            } else {
                throw new DBException("Unexpected error: no rows inserted.");
            }

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void update(Salesperson salesperson) {
        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                                UPDATE salesperson
                                SET Name=?, Email=?, BirthDate=?, BaseSalary=?, DepartmentId=?
                                WHERE Id=?""",
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, salesperson.getName());
            preparedStatement.setString(2, salesperson.getEmail());
            preparedStatement.setDate(3, salesperson.getBirthDate());
            preparedStatement.setDouble(4, salesperson.getBaseSalary());
            preparedStatement.setInt(5, salesperson.getDepartment().getId());
            preparedStatement.setInt(6, salesperson.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                        DELETE FROM salesperson
                        WHERE Id = ?""")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Salesperson findById(Integer id) {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                        SELECT salesperson.*, department.Name as DepName
                        FROM salesperson INNER JOIN department
                        ON salesperson.DepartmentId = department.Id
                        WHERE salesperson.Id = ?""")) {

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);

                return instantiateSalesperson(resultSet, department);
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    public List<Salesperson> findByDepartment(Department department) {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                        SELECT salesperson.*, department.name as DepName
                        FROM salesperson INNER JOIN department
                        ON salesperson.DepartmentId = department.Id
                        WHERE departmentId = ?
                        ORDER by name""")) {

            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();

            List<Salesperson> salespersonList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {
                Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), dep);
                }

                Salesperson salesperson = instantiateSalesperson(resultSet, dep);
                salespersonList.add(salesperson);
            }
            return salespersonList;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Salesperson instantiateSalesperson(ResultSet resultSet, Department department) throws SQLException {
        Salesperson salesperson = new Salesperson();
        salesperson.setId(resultSet.getInt("Id"));
        salesperson.setName(resultSet.getString("Name"));
        salesperson.setEmail(resultSet.getString("Email"));
        salesperson.setBirthDate(resultSet.getDate("BirthDate"));
        salesperson.setBaseSalary(resultSet.getDouble("BaseSalary"));
        salesperson.setDepartment(department);

        return salesperson;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));

        return department;
    }

    @Override
    public List<Salesperson> findAll() {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                        SELECT salesperson.*, department.name as DepName
                        FROM salesperson INNER JOIN department
                        ON salesperson.DepartmentId = department.Id
                        ORDER by name""")) {

            resultSet = preparedStatement.executeQuery();

            List<Salesperson> salespersonList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {
                Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), dep);
                }

                Salesperson salesperson = instantiateSalesperson(resultSet, dep);
                salespersonList.add(salesperson);
            }
            return salespersonList;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
