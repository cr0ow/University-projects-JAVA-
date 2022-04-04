import java.util.List;

public class Main {

    public static void main(String[] args) {
        Test test = new Test();
        // test from docs
        test.testString(List.of(0, 0, 1), List.of(1, 2, 2),
                "[[0, 0, 1], [0, 0, 2], [0, 1, 1], [0, 1, 2], [0, 2, 1], [0, 2, 2], [1, 0, 1], [1, 0, 2], [1, 1, 1], [1, 1, 2], [1, 2, 1], [1, 2, 2]]");

        // positive numbers test
        test.testString(List.of(0, 1, 2), List.of(3, 3, 3),
                "[[0, 1, 2], [0, 1, 3], [0, 2, 2], [0, 2, 3], [0, 3, 2], [0, 3, 3], [1, 1, 2], [1, 1, 3], [1, 2, 2], [1, 2, 3], [1, 3, 2], [1, 3, 3], [2, 1, 2], [2, 1, 3], [2, 2, 2], [2, 2, 3], [2, 3, 2], [2, 3, 3], [3, 1, 2], [3, 1, 3], [3, 2, 2], [3, 2, 3], [3, 3, 2], [3, 3, 3]]");
        // negative number test
        test.testString(List.of(-1, -1, -1), List.of(0, 1, 0),
                "[[-1, -1, -1], [-1, -1, 0], [-1, 0, -1], [-1, 0, 0], [-1, 1, -1], [-1, 1, 0], [0, -1, -1], [0, -1, 0], [0, 0, -1], [0, 0, 0], [0, 1, -1], [0, 1, 0]]");
        test.testString(List.of(-10, -11, -12), List.of(-9, -10, -11),
                "[[-10, -11, -12], [-10, -11, -11], [-10, -10, -12], [-10, -10, -11], [-9, -11, -12], [-9, -11, -11], [-9, -10, -12], [-9, -10, -11]]");
        // no setLowerLimits call
        test.testString(null, List.of(2, 2), "[[0, 0], [0, 1], [0, 2], [1, 0], [1, 1], [1, 2], [2, 0], [2, 1], [2, 2]]");
        test.testString(null, List.of(2, 1, 0), "[[0, 0, 0], [0, 1, 0], [1, 0, 0], [1, 1, 0], [2, 0, 0], [2, 1, 0]]");
        test.testString(null, List.of(0, 0, 0), "[[0, 0, 0]]");
        test.testString(null, List.of(0, 0, 2), "[[0, 0, 0], [0, 0, 1], [0, 0, 2]]");
        // no setUpperLimits call
        test.testString(List.of(0, 0, 0), null, "[[0, 0, 0]]");
        test.testString(List.of(-1, -1, -2), null,
                "[[-1, -1, -2], [-1, -1, -1], [-1, -1, 0], [-1, 0, -2], [-1, 0, -1], [-1, 0, 0], [0, -1, -2], [0, -1, -1], [0, -1, 0], [0, 0, -2], [0, 0, -1], [0, 0, 0]]");
        test.testString(List.of(-3, -2, -1), null,
                "[[-3, -2, -1], [-3, -2, 0], [-3, -1, -1], [-3, -1, 0], [-3, 0, -1], [-3, 0, 0], [-2, -2, -1], [-2, -2, 0], [-2, -1, -1], [-2, -1, 0], [-2, 0, -1], [-2, 0, 0], [-1, -2, -1], [-1, -2, 0], [-1, -1, -1], [-1, -1, 0], [-1, 0, -1], [-1, 0, 0], [0, -2, -1], [0, -2, 0], [0, -1, -1], [0, -1, 0], [0, 0, -1], [0, 0, 0]]");
        // no method calls
        test.testString(null, null, "[[0]]");

        // runs multiple tests on the same class object
        test.objectTests();

        // print results
        if (test.errorCount == 0)
            System.out.println("All " + test.testCount + " tests passed succesfully");
        else
            System.out.println(test.errorCount + " Tests failed");

        if (!test.doLargeTest)
            return;
        // test with 15 nested loops
        System.out.println("\n------\nTesting with 15 nested loops - this might take a moment ( ram="
                + String.format("%.02f", Runtime.getRuntime().maxMemory() / 1073741824f) + "GB )");
        Loops loopTest = new Loops();
        loopTest.setLowerLimits(List.of(1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1));
        loopTest.setUpperLimits(List.of(3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 3, 3, 3, 2, 2));
        if (loopTest.getResult().toString().length() != 473651712) {
            System.err.println("15 loops test failed, but program managed to not crash");
        } else {
            System.out.println("Program managed to not crash with 15 loops and the result length seems ok");
        }
    }
}


