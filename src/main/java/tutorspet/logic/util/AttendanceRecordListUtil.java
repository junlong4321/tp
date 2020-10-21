package tutorspet.logic.util;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_WEEK;
import static tutorspet.commons.core.Messages.MESSAGE_MISSING_STUDENT_ATTENDANCE;
import static tutorspet.commons.core.Messages.MESSAGE_NO_LESSON_ATTENDED;
import static tutorspet.commons.util.CollectionUtil.requireAllNonNull;
import static tutorspet.logic.util.AttendanceRecordUtil.addAttendance;
import static tutorspet.logic.util.AttendanceRecordUtil.setAttendance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tutorspet.logic.commands.exceptions.CommandException;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.attendance.Week;
import tutorspet.model.student.Student;

/**
 * Contains utility methods for modifying {@code Attendance}s in {@code AttendanceRecordList}.
 */
public class AttendanceRecordListUtil {

    /**
     * Returns an {@code AttendanceRecord} where the {@code attendanceToAdd} has been added to the
     * {@code targetAttendanceRecord}.
     *
     * @throws CommandException if the {@code targetWeek} does not exist in the {@code targetAttendanceRecordList} or
     * if there is an existing {@code Attendance} for the {@code targetStudent}.
     */
    public static AttendanceRecordList addAttendanceToAttendanceRecordList(
            AttendanceRecordList targetAttendanceRecordList,
            Student targetStudent, Week targetWeek,
            Attendance attendanceToAdd) throws CommandException {
        requireAllNonNull(targetAttendanceRecordList, targetStudent, targetWeek, attendanceToAdd);

        if (!targetAttendanceRecordList.isWeekContained(targetWeek)) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }

        AttendanceRecord targetAttendanceRecord = targetAttendanceRecordList.getAttendanceRecord(targetWeek);

        AttendanceRecord editedWeekAttendanceRecord =
                addAttendance(targetAttendanceRecord, targetStudent, attendanceToAdd);

        return replaceAttendanceRecordInList(targetAttendanceRecordList, targetWeek, editedWeekAttendanceRecord);
    }

    /**
     * Returns an {@code AttendanceRecordList} where the {@code AttendanceToSet} has replaced the existing
     * {@code Attendance} for the {@code targetStudent} in the {@code targetWeek} in the
     * {@code targetAttendanceRecordList}.
     *
     * @throws CommandException if the {@code targetWeek} does not exist in the {@code targetAttendanceRecordList} or
     * if the {@code Attendance} of the {@code targetStudent} in the {@code targetWeek} does not exist.
     */
    public static AttendanceRecordList editAttendanceInAttendanceRecordList(
            AttendanceRecordList targetAttendanceRecordList,
            Student targetStudent, Week targetWeek,
            Attendance attendanceToSet) throws CommandException {
        requireAllNonNull(targetAttendanceRecordList, targetStudent, targetWeek, attendanceToSet);

        if (!targetAttendanceRecordList.isWeekContained(targetWeek)) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }

        AttendanceRecord targetAttendanceRecord = targetAttendanceRecordList.getAttendanceRecord(targetWeek);

        AttendanceRecord editedWeekAttendanceRecord =
                setAttendance(targetAttendanceRecord, targetStudent, attendanceToSet);

        return replaceAttendanceRecordInList(targetAttendanceRecordList, targetWeek, editedWeekAttendanceRecord);
    }

    /**
     * Returns an {@code AttendanceRecordList} where the {@code Attendance} for the {@code targetStudent} in the
     * {@code targetWeek} in the {@code targetAttendanceRecordList} has been removed.
     *
     * @throws CommandException if the {@code targetWeek} does not exist in the {@code targetAttendanceRecordList} or
     * if the {@code Attendance} of the {@code targetStudent} in the {@code targetWeek} does not exist.
     */
    public static AttendanceRecordList removeAttendanceFromAttendanceRecordList(
            AttendanceRecordList targetAttendanceRecordList,
            Student targetStudent,
            Week targetWeek) throws CommandException {
        requireAllNonNull(targetAttendanceRecordList, targetStudent, targetWeek);

        if (!targetAttendanceRecordList.isWeekContained(targetWeek)) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }

        AttendanceRecord targetAttendanceRecord = targetAttendanceRecordList.getAttendanceRecord(targetWeek);

        AttendanceRecord editedWeekAttendanceRecord =
                AttendanceRecordUtil.removeAttendance(targetAttendanceRecord, targetStudent);

        return replaceAttendanceRecordInList(targetAttendanceRecordList, targetWeek, editedWeekAttendanceRecord);
    }

    /**
     * Returns the {@code Attendance} for the {@code targetStudent} in the {@code targetWeek} in the
     * {@code targetAttendanceRecordList}.
     *
     * @throws CommandException if the {@code targetWeek} does not exist in the {@code targetAttendanceRecordList} or
     * if the {@code Attendance} of the {@code targetStudent} in the {@code targetWeek} does not exist.
     */
    public static Attendance getAttendanceFromAttendanceRecordList(
            AttendanceRecordList targetAttendanceRecordList, Student targetStudent,
            Week targetWeek) throws CommandException {
        requireAllNonNull(targetAttendanceRecordList, targetStudent, targetWeek);

        if (!targetAttendanceRecordList.isWeekContained(targetWeek)) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }

        AttendanceRecord targetAttendanceRecord = targetAttendanceRecordList.getAttendanceRecord(targetWeek);

        if (!targetAttendanceRecord.hasAttendance(targetStudent.getUuid())) {
            throw new CommandException(MESSAGE_MISSING_STUDENT_ATTENDANCE);
        }

        return targetAttendanceRecord.getAttendance(targetStudent.getUuid());
    }

    /**
     * Returns the average participation score for {@code targetStudent}.
     */
    public static int getScoreFromAttendance(AttendanceRecordList targetAttendanceRecordList, Student targetStudent)
            throws CommandException {
        requireAllNonNull(targetAttendanceRecordList, targetStudent);

        List<Optional<Attendance>> listOfAttendance = getAttendances(targetAttendanceRecordList, targetStudent);

        int totalScore = 0;
        int numOfWeeksParticipated = 0;

        for (Optional<Attendance> attendance : listOfAttendance) {
            if (attendance.isPresent()) {
                totalScore = totalScore + attendance.get().getParticipationScore();
                numOfWeeksParticipated++;
            }
        }

        if (numOfWeeksParticipated == 0) {
            throw new CommandException(MESSAGE_NO_LESSON_ATTENDED);
        }

        return totalScore / numOfWeeksParticipated;
    }

    /**
     * Returns a string containing lessons in which {@code targetStudent} did not attend.
     */
    public static String getAbsentWeekFromAttendance(AttendanceRecordList targetAttendanceRecordList,
                                                Student targetStudent) {
        requireAllNonNull(targetAttendanceRecordList, targetStudent);

        List<Optional<Attendance>> listOfAttendance = getAttendances(targetAttendanceRecordList, targetStudent);
        StringBuilder weeksNotPresent = new StringBuilder();
        int weekNo = 1;

        for (Optional<Attendance> attendance : listOfAttendance) {
            if (attendance.isEmpty()) {
                weeksNotPresent.append(" ").append(weekNo);
            }
            weekNo++;
        }

        return weeksNotPresent.toString();
    }

    private static AttendanceRecordList replaceAttendanceRecordInList(AttendanceRecordList attendanceRecordList,
                                                                      Week week,
                                                                      AttendanceRecord attendanceRecordToSet) {
        List<AttendanceRecord> modifiedAttendanceRecordList =
                new ArrayList<>(attendanceRecordList.getAttendanceRecordList());
        modifiedAttendanceRecordList.set(week.getZeroBasedWeekIndex(), attendanceRecordToSet);
        return new AttendanceRecordList(modifiedAttendanceRecordList);
    }

    private static List<Optional<Attendance>> getAttendances(AttendanceRecordList targetAttendanceRecordList,
                                                             Student targetStudent) {
        requireAllNonNull(targetAttendanceRecordList, targetStudent);

        List<Optional<Attendance>> studentRecords = new ArrayList<>();

        List<AttendanceRecord> attendanceRecords = targetAttendanceRecordList.getAttendanceRecordList();

        for (AttendanceRecord attendanceRecord : attendanceRecords) {
            if (attendanceRecord.hasAttendance(targetStudent.getUuid())) {
                studentRecords.add(Optional.of(attendanceRecord.getAttendance(targetStudent.getUuid())));
            } else {
                studentRecords.add(Optional.empty());
            }
        }

        return Collections.unmodifiableList(studentRecords);
    }
}
