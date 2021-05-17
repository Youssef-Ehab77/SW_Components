package eExam.model;

public class Exam {

    private String name;
    private String ID;
    private int numberOfQuestions;
    private int grade;
    private String start_time, end_time;


    public Exam(String name, int grade, int numberOfQuestions, String start_time, String end_time) {
        this.name = name;
        this.grade = grade;
        this.numberOfQuestions = numberOfQuestions;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
