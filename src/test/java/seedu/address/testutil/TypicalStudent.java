package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_AVERAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_EXPERIENCED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.student.Student;

/**
 * A utility class containing a list of {@code Student} objects to be used in tests.
 */
public class TypicalStudent {

    public static final Student ALICE = new StudentBuilder().withUuid("0c527a3f-8a6f-4c16-b57d-563a9c43cf6b")
            .withName("Alice Pauline")
            .withEmail("alice@example.com")
            .withTelegram("4Lice_P")
            .withTags("Average").build();
    public static final Student BENSON = new StudentBuilder().withUuid("5c09bbdc-b89c-482a-a4b0-32ede6d679e7")
            .withName("Benson Meier")
            .withEmail("johnd@example.com").withTelegram("BeN_10")
            .withTags("PotentialTA", "Experienced").build();
    public static final Student CARL = new StudentBuilder().withUuid("62e9abd0-5456-4979-a9cc-8a0818421c2a")
            .withName("Carl Kurz").withTelegram("cK2zz")
            .withEmail("heinz@example.com").build();
    public static final Student DANIEL = new StudentBuilder().withUuid("d3593480-6648-4f7a-afa5-6c1b09b52718")
            .withName("Daniel Meier").withTelegram("danielmeier")
            .withEmail("cornelia@example.com").withTags("Weak").build();
    public static final Student ELLE = new StudentBuilder().withUuid("abb96663-589f-4553-bc25-fa93fed7459c")
            .withName("Elle Meyer").withTelegram("4114_my")
            .withEmail("werner@example.com").build();
    public static final Student FIONA = new StudentBuilder().withUuid("2be53daa-e18d-4de1-828b-31ad891ff7fc")
            .withName("Fiona Kunz").withTelegram("kunz_F")
            .withEmail("lydia@example.com").build();
    public static final Student GEORGE = new StudentBuilder().withUuid("48a5bfc5-782a-4861-8b36-9db2833cfd2d")
            .withName("George Best").withTelegram("dabest")
            .withEmail("anna@example.com").build();

    // Manually added
    public static final Student HOON = new StudentBuilder().withName("Hoon Meier").withTelegram("8482424")
            .withEmail("stefan@example.com").build();
    public static final Student IDA = new StudentBuilder().withName("Ida Mueller").withTelegram("8482131")
            .withEmail("hans@example.com").build();

    // Manually added - Student's details found in {@code CommandTestUtil}
    public static final Student AMY = new StudentBuilder().withName(VALID_NAME_AMY).withTelegram(VALID_TELEGRAM_AMY)
            .withEmail(VALID_EMAIL_AMY).withTags(VALID_TAG_EXPERIENCED).build();
    public static final Student BOB = new StudentBuilder().withName(VALID_NAME_BOB).withTelegram(VALID_TELEGRAM_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withTags(VALID_TAG_AVERAGE, VALID_TAG_EXPERIENCED).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalStudent() {} // prevents instantiation

    public static List<Student> getTypicalStudents() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}