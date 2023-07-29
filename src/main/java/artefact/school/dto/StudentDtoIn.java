package artefact.school.dto;

public class StudentDtoIn {

    private String name;

    private int age;

    public StudentDtoIn(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public StudentDtoIn() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
