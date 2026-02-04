package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Constraints.Admin;

public class UserDataSql {
	
	public static Boolean flag = true; 

    public static void CreateAdminDataTable() {
        String CreateAdminDataTable =
            "CREATE TABLE IF NOT EXISTS AdminData (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "UserName VARCHAR(200) NOT NULL UNIQUE, " +
            "FirstName VARCHAR(200) NOT NULL, " +
            "LastName VARCHAR(200) NOT NULL, " +
            "Contact VARCHAR(200) NOT NULL, " +
            "Gender VARCHAR(200) NOT NULL, " +
            "Password VARCHAR(200) NOT NULL, " +
            "ImagePath VARCHAR(500)" +
            ")";
        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,
                jdbcConnection.USER, jdbcConnection.PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(CreateAdminDataTable);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
    }

    public static boolean insertAdmin(Admin adm) {
    	 CreateAdminDataTable() ;
        String checkSQL = "SELECT COUNT(*) FROM AdminData WHERE UserName = ?";
        String insertSQL = "INSERT INTO AdminData "
        		+ "(UserName, FirstName, LastName, Contact, Gender, Password, ImagePath) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER, jdbcConnection.PASSWORD)) {

            // 1. Check if username already exists
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
                checkStmt.setString(1, adm.getUserId());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, adm.getUserId());
                pstmt.setString(2, adm.getFirstName());
                pstmt.setString(3, adm.getLastName());
                pstmt.setString(4, adm.getContact());
                pstmt.setString(5, adm.getgender());
                pstmt.setString(6, adm.getPassword());
                pstmt.setString(7, adm.getimagePath());

                pstmt.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }


    
    
    public static Admin getAdminByLogin(String username, String password) {

        String sql = "SELECT * FROM AdminData WHERE UserName = ? AND Password = ?";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL, jdbcConnection.USER,jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Contact"),
                        rs.getString("UserName"),
                        rs.getString("Password"),
                        rs.getString("Gender"),
                        rs.getString("ImagePath")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    
    public static boolean updateAdmin(Admin admin) {

        String sql = "UPDATE AdminData SET FirstName=?, LastName=?, Contact=?, Gender=?, Password=?, ImagePath=? WHERE UserName=?";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, admin.getFirstName());
            ps.setString(2, admin.getLastName());
            ps.setString(3, admin.getContact());
            ps.setString(4, admin.getgender());
            ps.setString(5, admin.getPassword());
            ps.setString(6, admin.getimagePath());
            ps.setString(7, admin.getUserId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAdmin(String userId) {

        String sql = "DELETE FROM AdminData WHERE UserName = ?";

        try (Connection conn = DriverManager.getConnection(jdbcConnection.dbURL,jdbcConnection.USER,jdbcConnection.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
