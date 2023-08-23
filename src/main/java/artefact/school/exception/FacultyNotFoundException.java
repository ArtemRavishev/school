package artefact.school.exception;

public class FacultyNotFoundException extends RuntimeException {

    private final long id;

    public FacultyNotFoundException(long id) {
        this.id = id;
    }
    @Override
    public String getMessage() {
        return "Факультет с id=" + id + " не найден";
    }
}
