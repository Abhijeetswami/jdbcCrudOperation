package EmployeeDetails;

import java.sql.*;
import java.util.Scanner;

public class Employee {
    private static final String url = "jdbc:mysql://localhost:3306/jdbcCrudOperation";
    private static final String username= "root";
    private static final String password= "Abhijeet@45";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nEmployee Management System\n");
                System.out.println("1. Add employee details");
                System.out.println("2. Show all employee details");
                System.out.println("3. Update employee details");
                System.out.println("4. Delete employee details");
                System.out.println("5. Exit");
                System.out.print("\nEnter your choice (1-5): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                switch (choice) {
                    case 1:
                        addEmployee(connection, scanner);
                        break;
                    case 2:
                        showAllEmployees(connection);
                        break;
                    case 3:
                        updateEmployee(connection, scanner);
                        break;
                    case 4:
                        deleteEmployee(connection, scanner);
                        break;
                    case 5:
                        System.out.println("\nExiting...");
                        return;
                    default:
                        System.out.println("\nInvalid choice! Please enter a valid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addEmployee(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("\nAdd Employee Details");

        System.out.print("Enter employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();

        System.out.print("Enter employee salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        String query = "INSERT INTO employees (id, name, salary) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setDouble(3, salary);
            statement.executeUpdate();
            System.out.println("\nEmployee details added successfully!");
        }
    }
    private static void showAllEmployees(Connection connection) throws SQLException {
        System.out.println("\nAll Employee Details");

        String query = "SELECT * FROM employees";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                System.out.println("ID: " + id + ", Name: " + name + ", Salary: " + salary);
            }
        }
    }
    private static void updateEmployee(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("\nUpdate Employee Details");

        System.out.print("Enter employee ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String query = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(query)) {
            selectStatement.setInt(1, id);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.print("Enter updated employee name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter updated employee salary: ");
                    double salary = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline character

                    String updateQuery = "UPDATE employees SET name = ?, salary = ? WHERE id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, name);
                        updateStatement.setDouble(2, salary);
                        updateStatement.setInt(3, id);
                        updateStatement.executeUpdate();
                        System.out.println("\nEmployee details updated successfully!");
                    }
                } else {
                    System.out.println("\nEmployee not found with ID: " + id);
                }
            }
        }
    }
    private static void deleteEmployee(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("\nDelete Employee Details");

        System.out.print("Enter employee ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String query = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(query)) {
            selectStatement.setInt(1, id);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    String deleteQuery = "DELETE FROM employees WHERE id = ?";
                    try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                        deleteStatement.setInt(1, id);
                        deleteStatement.executeUpdate();
                        System.out.println("\nEmployee details deleted successfully!");
                    }
                } else {
                    System.out.println("\nEmployee not found with ID: " + id);
                }
            }
        }
    }
}
