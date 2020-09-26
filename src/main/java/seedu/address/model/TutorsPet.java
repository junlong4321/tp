package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.model.moduleclass.ModuleClass;
import seedu.address.model.moduleclass.UniqueModuleClassList;
import seedu.address.model.student.Student;
import seedu.address.model.student.UniqueStudentList;

/**
 * Wraps all data at the application level.
 * Duplicates are not allowed (by .isSameStudent and .isSameModuleClass comparison).
 */
public class TutorsPet implements ReadOnlyTutorsPet {

    private final UniqueStudentList students;
    private final UniqueModuleClassList moduleClasses;

    /**
     * Creates a TutorsPet with no existing data.
     */
    public TutorsPet() {
        students = new UniqueStudentList();
        moduleClasses = new UniqueModuleClassList();
    }

    /**
     * Creates a TutorsPet using the Students in the {@code toBeCopied}.
     */
    public TutorsPet(ReadOnlyTutorsPet toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudent(students);
    }

    /**
     * Replaces the contents of the class list with {@code moduleClasses}.
     * {@code moduleClasses} must not contain duplicate classes.
     */
    public void setModuleClasses(List<ModuleClass> moduleClasses) {
        this.moduleClasses.setModuleClass(moduleClasses);
    }

    /**
     * Resets the existing data of this {@code TutorsPet} with {@code newData}.
     */
    public void resetData(ReadOnlyTutorsPet newData) {
        requireNonNull(newData);

        setStudents(newData.getStudentList());
        setModuleClasses(newData.getModuleClassList());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the application.
     * The student must not already exist in the application.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the application.
     * The student identity of {@code editedStudent} must not be the same as another existing student in the
     * application.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        students.setStudent(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code TutorsPet}.
     * {@code key} must exist in the application.
     */
    public void removeStudent(Student key) {
        students.remove(key);
    }

    //// moduleClass-level operations

    /**
     * Returns true if a class with the same identity as {@code moduleClass} exists.
     */
    public boolean hasModuleClass(ModuleClass moduleClass) {
        requireNonNull(moduleClass);
        return moduleClasses.contains(moduleClass);
    }

    /**
     * Adds a class to the application.
     * The class must not already exist in the application.
     */
    public void addModuleClass(ModuleClass moduleClass) {
        moduleClasses.add(moduleClass);
    }

    /**
     * Replaces the given class {@code target} in the list with {@code editedModuleClass}.
     * {@code target} must exist in the application.
     * The class identity of {@code editedModuleClass} must not be the same as another existing class in the
     * application.
     */
    public void setModuleClass(ModuleClass target, ModuleClass editedModuleClass) {
        requireNonNull(editedModuleClass);
        moduleClasses.setModuleClass(target, editedModuleClass);
    }

    /**
     * Removes {@code key} from this {@code TutorsPet}.
     * {@code key} must exist in the application.
     */
    public void removeModuleClass(ModuleClass key) {
        moduleClasses.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return students.asUnmodifiableObservableList().size() + " students "
                + moduleClasses.asUnmodifiableObservableList().size() + " classes";
        // TODO: refine later
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<ModuleClass> getModuleClassList() {
        return moduleClasses.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TutorsPet // instanceof handles nulls
                && students.equals(((TutorsPet) other).students)
                && moduleClasses.equals((((TutorsPet) other).moduleClasses)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, moduleClasses);
    }
}