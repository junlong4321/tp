package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceRecord;
import seedu.address.model.attendance.AttendanceRecordList;
import seedu.address.model.attendance.Week;
import seedu.address.model.components.name.Name;
import seedu.address.model.lesson.Day;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.NumberOfOccurrences;
import seedu.address.model.lesson.Venue;
import seedu.address.model.moduleclass.ModuleClass;
import seedu.address.model.student.Student;

/**
 * Deletes an attendance record of a student for a specific week identified using it's
 * displayed index in the displayed module class list.
 */
public class DeleteAttendanceRecordCommand extends Command {

    public static final String COMMAND_WORD = "delete-attendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the attendance record of a student in a specific week identified by the "
            + "index number used in the displayed class list, student list, and lesson list respectively. "
            + "Note: All indexes must be a positive integer.\n"
            + "Parameters: "
            + PREFIX_CLASS_INDEX + "CLASS_INDEX "
            + PREFIX_LESSON_INDEX + "LESSON_INDEX "
            + PREFIX_STUDENT_INDEX + "STUDENT_INDEX "
            + PREFIX_WEEK + "WEEK_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLASS_INDEX + "1 "
            + PREFIX_LESSON_INDEX + "1 "
            + PREFIX_STUDENT_INDEX + "1 "
            + PREFIX_WEEK + "1";

    public static final String MESSAGE_DELETE_ATTENDANCE_SUCCESS =
            "Deleted week %1$s attendance of student %2$s from lesson %3$s";

    private final Index moduleClassIndex;
    private final Index lessonIndex;
    private final Index studentIndex;
    private final Week week;

    /**
     * @param moduleClassIndex in the filtered class list.
     * @param lessonIndex in the filtered lesson list.
     * @param studentIndex in the filtered student list.
     * @param week in the specified attendance list to be deleted.
     */
    public DeleteAttendanceRecordCommand(
            Index moduleClassIndex, Index lessonIndex, Index studentIndex, Week week) {
        requireAllNonNull(moduleClassIndex, lessonIndex, studentIndex, week);

        this.moduleClassIndex = moduleClassIndex;
        this.lessonIndex = lessonIndex;
        this.studentIndex = studentIndex;
        this.week = week;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Student> lastShownStudentList = model.getFilteredStudentList();
        List<ModuleClass> lastShownModuleClassList = model.getFilteredModuleClassList();

        if (studentIndex.getZeroBased() >= lastShownStudentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        if (moduleClassIndex.getZeroBased() >= lastShownModuleClassList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULE_CLASS_DISPLAYED_INDEX);
        }

        Student targetStudent = lastShownStudentList.get(studentIndex.getZeroBased());
        ModuleClass targetModuleClass = lastShownModuleClassList.get(moduleClassIndex.getZeroBased());

        if (lessonIndex.getZeroBased() >= targetModuleClass.getLessons().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
        }

        Lesson targetLesson = targetModuleClass.getLessons().get(lessonIndex.getZeroBased());

        if (!targetLesson.getAttendanceRecordList().isWeekContained(week)) {
            throw new CommandException(Messages.MESSAGE_INVALID_WEEK);
        }

        Lesson modifiedLesson = createModifiedLesson(targetLesson, targetStudent, week);
        ModuleClass modifiedModuleClass = createModifiedModuleClass(targetModuleClass, lessonIndex, modifiedLesson);
        model.setModuleClass(targetModuleClass, modifiedModuleClass);

        String message = String.format(MESSAGE_DELETE_ATTENDANCE_SUCCESS, week, targetStudent.getName(), targetLesson);
        model.commit(message);
        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteAttendanceRecordCommand // instance of handles nulls
                && moduleClassIndex.equals(((DeleteAttendanceRecordCommand) other).moduleClassIndex)
                && lessonIndex.equals(((DeleteAttendanceRecordCommand) other).lessonIndex))
                && studentIndex.equals(((DeleteAttendanceRecordCommand) other).studentIndex)
                && week.equals(((DeleteAttendanceRecordCommand) other).week);
    }

    private static ModuleClass createModifiedModuleClass(ModuleClass targetModuleClass,
                                                         Index lessonToEditIndex, Lesson lessonToUpdate) {
        assert targetModuleClass != null;
        assert lessonToUpdate != null;

        Name moduleClassName = targetModuleClass.getName();
        Set<UUID> studentsIds = targetModuleClass.getStudentUuids();
        List<Lesson> lessons = new ArrayList<>(targetModuleClass.getLessons());
        lessons.set(lessonToEditIndex.getZeroBased(), lessonToUpdate);

        return new ModuleClass(moduleClassName, studentsIds, lessons);
    }

    private static Lesson createModifiedLesson (
            Lesson targetLesson, Student targetStudent, Week targetWeek) throws CommandException {
        assert targetLesson != null;
        assert targetStudent != null;
        assert targetWeek != null;

        Map<UUID, Attendance> record = targetLesson.getAttendanceRecordList()
                .getAttendanceRecord(targetWeek).getAttendanceRecord();
        Map<UUID, Attendance> updatedRecord = new HashMap<>(record);
        // delete attendance record
        updatedRecord.remove(targetStudent.getUuid());
        AttendanceRecord updatedAttendanceRecord = new AttendanceRecord(updatedRecord);
        List<AttendanceRecord> updatedAttendanceRecords =
                new ArrayList<>(targetLesson.getAttendanceRecordList().getAttendanceRecordList());
        updatedAttendanceRecords.set(targetWeek.getZeroBasedWeekIndex(), updatedAttendanceRecord);
        AttendanceRecordList updatedAttendanceRecordList = new AttendanceRecordList(updatedAttendanceRecords);

        // unchanged lesson fields
        LocalTime startTime = targetLesson.getStartTime();
        LocalTime endTime = targetLesson.getEndTime();
        Day day = targetLesson.getDay();
        NumberOfOccurrences numberOfOccurrences = targetLesson.getNumberOfOccurrences();
        Venue venue = targetLesson.getVenue();

        return new Lesson(startTime, endTime, day, numberOfOccurrences, venue, updatedAttendanceRecordList);
    }
}
