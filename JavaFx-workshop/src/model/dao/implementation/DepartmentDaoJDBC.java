package model.dao.implementation;

import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDaoJDBC implements DepartmentDao {

    private final Connection CONNECTION;

    public DepartmentDaoJDBC(Connection CONNECTION) {

        this.CONNECTION = CONNECTION;
    }

    @Override
    public void insert(Department department) {

        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                                INSERT INTO department (Name) VALUES (?)""",
                        Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, department.getName());

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {

                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {

                    int id = resultSet.getInt(1);
                    department.setId(id);
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
    public void update(Department department) {

        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                                UPDATE department
                                SET NAME=?
                                WHERE ID=?""",
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Integer id) {

        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement("""
                DELETE FROM department
                WHERE Id = ?""")) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (DBException e) {

            e.printStackTrace();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Department findById(Integer id) {

        ResultSet resultSet = null;

        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                        SELECT department.*, department.Name as DepName
                        FROM salesperson INNER JOIN department
                        ON salesperson.DepartmentId = department.Id
                        WHERE department.Id = ?""")) {

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {

                return new Department(resultSet.getInt(1), resultSet.getString(2));
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
    public List<Department> findAll() {

        ResultSet resultSet = null;

        try (PreparedStatement preparedStatement = CONNECTION
                .prepareStatement("""
                        SELECT department.*
                        FROM department
                        ORDER by Id""")) {

            resultSet = preparedStatement.executeQuery();

            List<Department> departmentList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()) {

                Department dep = departmentMap.get(resultSet.getInt("Id"));
                if (dep == null) {

                    departmentMap.put(resultSet.getInt("Id"), dep);
                    dep = new Department(resultSet.getInt(1), resultSet.getString(2));
                }

                departmentList.add(dep);
            }

            return departmentList;

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
