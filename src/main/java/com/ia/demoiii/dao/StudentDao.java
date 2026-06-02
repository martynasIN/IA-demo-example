package com.ia.demoiii.dao;

import com.ia.demoiii.db.DatabaseConfig;
import com.ia.demoiii.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    public int create(Student student) {
        String sql = "INSERT INTO students (name, email, `group`, description) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getGroup());
            statement.setString(4, student.getDescription());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new RuntimeException("No ID generated");
        } catch (SQLException e) {
            throw new RuntimeException("Student not saved", e);
        }
    }

    public List<Student> findAll() {
        String sql = "SELECT id, name, email, `group`, description FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("group"),
                        resultSet.getString("description")
                ));
            }
            return students;
        } catch (SQLException e) {
            throw new RuntimeException("Students not loaded from DB", e);
        }
    }

    public void update(Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, `group` = ?, description = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getGroup());
            statement.setString(4, student.getDescription());
            statement.setInt(5, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Student not updated", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Student not deleted", e);
        }
    }
}
