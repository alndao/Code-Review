import fr.isima.codereview.tp1.awesomepasswordchecker.AwesomePasswordChecker;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



/**
 * Test class for the {@link AwesomePasswordChecker} class.
 * This class contains unit tests to verify the behavior of the 
 * {@link AwesomePasswordChecker#ComputeMD5(String)} method.
 */
public class AwesomePasswordCheckerTest {
    /**
     * Test case for verifying the correct computation of the MD5 hash
     * for a non-empty string input.
     * 
     * Given a string "Test123!", the MD5 hash should match the expected 
     * value: "2168ad5e463d9accb215edaafa31c8d9".
     * 
     * @see AwesomePasswordChecker#ComputeMD5(String)
     */
    @Test
    void testComputeMD5() {
        // Given
        String input = "Test123!";
        String expectedHash = "2168ad5e463d9accb215edaafa31c8d9"; 

        // When
        String actualHash = AwesomePasswordChecker.ComputeMD5(input);

        // Then
        assertEquals(expectedHash, actualHash, "The MD5 hash should match the expected value.");
    }

    /**
     * Test case for verifying the correct computation of the MD5 hash
     * for an empty string input.
     * 
     * Given an empty string, the MD5 hash should match the expected
     * value: "d41d8cd98f00b204e9800998ecf8427e".
     * 
     * @see AwesomePasswordChecker#ComputeMD5(String)
     */
    @Test
    void testComputeMD5EmptyString() {
        // Given
        String input = "";
        String expectedHash = "d41d8cd98f00b204e9800998ecf8427e";

        // When
        String actualHash = AwesomePasswordChecker.ComputeMD5(input);

        // Then
        assertEquals(expectedHash, actualHash, "The MD5 hash of an empty string should be correct.");
    }

    /**
     * Test case for verifying the behavior when a null input is passed
     * to the {@link AwesomePasswordChecker#ComputeMD5(String)} method.
     * 
     * Given a null input, the method should throw an 
     * {@link IllegalArgumentException}.
     * 
     * @see AwesomePasswordChecker#ComputeMD5(String)
     */  
    @Test
    void testComputeMD5NullInput() {
        // Given
        String input = null;

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            AwesomePasswordChecker.ComputeMD5(input);
        }, "An IllegalArgumentException should be thrown for null input.");
    }
}

