package eExam.model;

import eExam.controller.Professor_HomePage_Controller;
import eExam.controller.Professor_Subject_Controller;
import eExam.controller.Welcome_Controller;

import java.sql.*;

public class DBConnection implements DB {

    private static final DBConnection db = new DBConnection();
    private Connection con;
    private Statement stmt;

    /**
     * DBConnection is the constructor where i set my connection to database
     * wither its a cloud database (AWS) or local host
     */
    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            //local db
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "gamd1998");
            //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sw_test1", "root", "gamd1998");

            //AWS cloud db
            //con = DriverManager.getConnection("jdbc:mysql://database-1.ccpxmnqempna.us-east-2.rds.amazonaws.com:3306/project1", "admin", "gamd1998");

            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }

    public static DBConnection getInstance() {
        return db;
    }

    /**
     * check_login method checks if the user is in the database and wither
     * he is professor or a student
     *
     * @param name     username that the user entered
     * @param password password that the user entered
     * @return returns a boolean to know wither if the username are correct or even exist or not
     * @throws SQLException we call the static variable @userType from the welcome page controller to know wither
     *                      the user entered the professor or the student portal to login.
     *                      if the data was right we set the user id to use it later on.
     */
    public boolean check_login(String name, String password) throws SQLException {
        String sql;
        if (Welcome_Controller.userType.equals("student")) {
            sql = "select id from student where name = '" + name + "' and password = '" + password + "'";
        } else
            sql = "select  id from professor where name = '" + name + "' and password='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (!rs.isBeforeFirst()) {
            return false;
        } else {
            if (Welcome_Controller.userType.equals("professor")) {
                rs.next();
                Professor_HomePage_Controller.professor.setID(rs.getInt(1));
            } else {
                rs.next();
                //  Professor_HomePage_Controller.professor.setID(rs.getInt(1));
            }
            return true;
        }
    }

    /**
     * @param name     username that the user entered
     * @param password password that the user entered and we confirm it before we send it
     * @param gender   user gender
     * @param dob      date of birth
     * @return returns zero or value to confirm that the edit is done
     * @throws SQLException And here we also check wither the user is professor or student.
     */

    @Override
    public int add_user(String name, String password, String gender, String dob) throws SQLException {
        String sql;
        if (Welcome_Controller.userType.equals("student")) {
            sql = "insert into student(name,password,gender,birth_date) values('" + name + "'," +
                    "'" + password + "','" + gender + "','" + dob + "')";
        } else sql = "insert into professor(name,password,gender,birth_date) values('" + name + "'," +
                "'" + password + "','" + gender + "','" + dob + "')";
        int rs = stmt.executeUpdate(sql);
        return rs;
    }

    /**
     * @param name     username
     * @param password password
     * @throws SQLException We get all the subjects that the professor teaches and add it to the Professor's
     *                      subject Arraylist by calling the static object from the professor home page controller.
     */

    public void get_professor_subjects(String name, String password) throws SQLException {
        String sql = "select name\n" +
                "from professor_subject ps\n" +
                "         join subject s\n" +
                "              on ps.subject_id = id\n" +
                "where professor_id =\n" +
                "      (select id from professor where name ='" + name + "' and password = '" + password + "')";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Professor_HomePage_Controller.professor.addSubject(rs.getString(1));
        }
    }

    public void get_students_in_subject(int professor_id, String subject_name) throws SQLException {
        String sql = "SELECT id, name, 7th, 12th, final\n" +
                "from student_subject ss\n" +
                "         join student s\n" +
                "              on ss.student_id = s.id\n" +
                "where ss.professor_id = " + professor_id + "\n" +
                "  and ss.subject_id =(select id from subject where subject.name = '" + subject_name + "')";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Professor_Subject_Controller.ol.add(new Student_Subjects(rs.getInt("id"), rs.getString("name"),
                    rs.getInt("7th"), rs.getInt("12th"), rs.getInt("final")));
        }
    }

    public void update_student_grade(String student_id, String subject_name, String grade_7th, String grade_12th, String grade_final) throws SQLException {
        String sql = "UPDATE student_subject\n" +
                "SET 7th   = " + grade_7th + ",\n" +
                "    12th  = " + grade_12th + ",\n" +
                "    final = " + grade_final + "\n" +
                "WHERE (student_id = " + student_id + ")\n" +
                "  and subject_id = (select id from subject where name ='" + subject_name + "')";
        int rs = stmt.executeUpdate(sql);
    }
}
