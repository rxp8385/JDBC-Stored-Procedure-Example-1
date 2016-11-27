package com.rxp8385.JDBCStoredProcedureExample;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;;

public class IncreaseSalariesForDepartment {

	public static void main(String[] args) throws SQLException {

		Connection myConn = null;
		CallableStatement myStmt =null;
		
		// Get a connection...
		
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcexample", "root", "password");
			
			String theDepartment = "Engineering";
			int theIncreaseAmount = 10000;
			
			// Show salaries BEFORE
			System.out.println("Salaries BEFORE\n");
			showSalaries(myConn, theDepartment);
			
			// Prepare the stored procedure call
			myStmt = myConn.prepareCall("{call increase_salaries_for_department(?,?)}");
			
			// Set the parameters
			myStmt.setString(1, theDepartment);
			myStmt.setDouble(2, theIncreaseAmount);
			
			// Call store procedures
			System.out.println("\n\nCalling stored procedure.  " +
								"increase_salaries_for_department('" + theDepartment + "', " + theIncreaseAmount + ")");
			myStmt.execute();
			System.out.println("Finished calling stored procedure");
			
			// Show salaries AFTER
			System.out.println("\n\nSalaries AFTER\n");
			showSalaries(myConn, theDepartment);
		} catch (SQLException e) {
			
			e.printStackTrace();
		} finally {
			close(myConn, myStmt, null);
		}

	}
	
	private static void showSalaries(Connection myConn, String theDepartment) throws SQLException {
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try{
		// Prepare statement
		myStmt = myConn.prepareStatement("select * from employees where department=?");
		
		myStmt.setString(1, theDepartment);
		
		// Execute SQL query
		myRs = myStmt.executeQuery();
		
		// Process result set
		while (myRs.next()){
			String lastName = myRs.getString("last_name");
			String firstName = myRs.getString("first_name");
			double salary = myRs.getDouble("salary");
			String department = myRs.getString("department");
			
			System.out.printf("%s, %s, %s, %.2f\n", lastName, firstName, department, salary);
		}
	} catch (Exception exc){
		exc.printStackTrace();
	} finally {
		close(myStmt, myRs);
	}
	
	}
	
	private static void close(Connection myConn, Statement myStmt,
			ResultSet myRs) throws SQLException {
		if (myRs !=null ){
			myRs.close();
		}
		
		if (myStmt != null){
			myStmt.close();
		}
		
		if (myConn != null);
		
	}
	
	private static void close(Statement myStmt, ResultSet myRs) throws SQLException {
		close(null, myStmt, myRs);
	}
}
