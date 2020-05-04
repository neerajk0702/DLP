package dlp.bluelupin.dlp.Models;

import java.util.List;

public class DashboardData {
    private  int courses;
    private  int users;
    private  int topics;
    private  int chapters;
    private  int quizzes;
    private int courseChapterType;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseChapterType() {
        return courseChapterType;
    }

    public void setCourseChapterType(int courseChapterType) {
        this.courseChapterType = courseChapterType;
    }

    public int getCourses() {
        return courses;
    }

    public void setCourses(int courses) {
        this.courses = courses;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getTopics() {
        return topics;
    }

    public void setTopics(int topics) {
        this.topics = topics;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public int getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(int quizzes) {
        this.quizzes = quizzes;
    }
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ClassPojo [courses = " + courses + ", users = " + users + ", topics = " + topics + ", chapters = " + chapters + ", quizzes = " + quizzes + ", courseChapterType = " + courseChapterType + "]";
    }
}
