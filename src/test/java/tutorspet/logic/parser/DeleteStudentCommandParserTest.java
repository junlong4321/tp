package tutorspet.logic.parser;

import static tutorspet.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorspet.logic.commands.student.DeleteStudentCommand.MESSAGE_USAGE;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorspet.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorspet.testutil.TypicalIndexes.INDEX_FIRST_ITEM;

import org.junit.jupiter.api.Test;

import tutorspet.logic.commands.student.DeleteStudentCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteStudentCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteStudentCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteStudentCommandParserTest {

    private DeleteStudentCommandParser parser = new DeleteStudentCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteStudentCommand() {
        assertParseSuccess(parser, "1", new DeleteStudentCommand(INDEX_FIRST_ITEM));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }
}
