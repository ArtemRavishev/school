package artefact.school.exception;

public class StudentNotFondException extends RuntimeException {

    private final long id;

    public StudentNotFondException(long id) {
        this.id = id;
    }
    @Override
    public String getMessage() {
        return "Студент с id=" + id + " не найден";
    }
}
