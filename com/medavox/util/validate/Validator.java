package com.medavox.util.validate;

/**
 * @author Adam Howard
 * @date 21/03/17
 */

/**A static-only class containing methods for performing validation checks.
 * <p>
 Rather than nesting if-statements during validation,
 we pass boolean expressions to calls to one of these methods,
 which throw an exception if the expression evaluates to false.
 </p>

 This is useful for runnning a sequence of validation checks on data
 (each of which may or may not transform that data) without going full Rx.

 The successive calls (which can be interspersed with operations on the data-under-validation)
 can then be wrapped in a try block.
 These calls act as a series of guard clauses,
 without cluttering the main path of execution with inline edge-case handling
 (as in nested if-statements).
 This also keeps all the validation checks in one place,
 making it easier to read (without having to cross-reference multiple files),
 and allows us to interleave 
 transformations on the data-under-validation with the checks, inline.

 This class is best used as a static import, 
 * eg {@code import static com.medavox.util.validate.Validator.check; }.*/

public class Validator {
    /**Check that the passed boolean expression evaluates to true,
     * on false throwing an {@link Exception} with the specified error message, and
     * running the specified {@link Runnable}.
     * @param rule the expression which must evaluate to true.
     * @param explan a human-readable explanation (used in the thrown Exception) as to why this check failed.
     * @param onFail this is run just before the Exception is thrown,
            giving a chance to perform some arbitrary work (such as a toast).
     * @throws Exception if the boolean evaluates to false.*/
    public static void check(boolean rule, String explan, Runnable onFail) throws Exception {
        check(rule, new Exception(/*"validation failed: "+*/explan), onFail);
    }

    public static void check(boolean rule, Exception exceptionToThrow, Runnable onFail) throws Exception {
        if(!rule) {
            if(onFail != null) {
                onFail.run();
            }
            throw exceptionToThrow;
        }
    }

    public static void check(boolean rule, Exception exceptionToThrow) throws Exception{
        check(rule, exceptionToThrow, null);
    }

    /**Check that the passed boolean expression evaluates to true,
     * on false throwing an {@link Exception} with the specified error message.
     * @param rule the expression which must evaluate to true.
     * @param explan a human-readable explanation (used in the thrown Exception)
     *               as to why this check failed.
     * @throws Exception if the boolean evaluates to false.*/
    public static void check(boolean rule, String explan) throws Exception {
        check(rule, explan, null);
    }
}
