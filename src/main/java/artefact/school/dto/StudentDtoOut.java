package artefact.school.dto;

public class StudentDtoOut {

    private long id;
    private String name;
    private int age;

    private FacultyDtoOut faculty;

    private AvatarDto avatar;




    public AvatarDto getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarDto avatar) {
        this.avatar = avatar;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public FacultyDtoOut getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDtoOut faculty) {
        this.faculty = faculty;
    }
}
