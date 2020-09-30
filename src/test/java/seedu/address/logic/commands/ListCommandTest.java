package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showModuleClassAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static seedu.address.testutil.TypicalTutorsPet.getTypicalTutorsPet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTutorsPet(), new UserPrefs());
        expectedModel = new ModelManager(model.getTutorsPet(), new UserPrefs());
    }

    @Test
    public void execute_studentListIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListStudentCommand(), model, ListStudentCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_studentListIsFiltered_showsEverything() {
        showStudentAtIndex(model, INDEX_FIRST_ITEM);
        assertCommandSuccess(new ListStudentCommand(), model, ListStudentCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_moduleClassListIsNotFiltered_showsSameList() {
        assertCommandSuccess(
                new ListModuleClassCommand(), model, ListModuleClassCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_moduleClassListIsFiltered_showsEverything() {
        showModuleClassAtIndex(model, INDEX_FIRST_ITEM);
        assertCommandSuccess(
                new ListModuleClassCommand(), model, ListModuleClassCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
