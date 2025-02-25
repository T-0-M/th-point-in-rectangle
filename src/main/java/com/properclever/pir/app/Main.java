package com.properclever.pir.app;

import com.properclever.pir.solution.BasicSolution;
import com.properclever.pir.solution.GeneralSolution;
import com.properclever.pir.solution.Solvable;

import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;

public class Main {
    // colors for terminal
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                clearScreen();
                System.out.print(getMainMenu());
                int choice = readInt(scanner);
                scanner.nextLine(); // Consume the leftover newline
                switch (choice) {
                    case 1 ->
                        // inject BasicSolution with method ref
                            handleSolutionMenu(scanner, "BASIC", BasicSolution::new);
                    case 2 ->
                        // inject GeneralSolution with method ref
                            handleSolutionMenu(scanner, "GENERAL", GeneralSolution::new);
                    case 9 -> {
                        System.out.println("Exiting program. Goodbye!");
                        return;
                    }
                    default -> {
                        System.out.println("Invalid selection. Please try again.");
                        pause(scanner);
                    }
                }
            }
        }
    }

    private static String getMainMenu() {
        var menuMainTemplate = """
                %s
                %s    MAIN MENU%s
                [%s1%s] Basic Solution
                [%s2%s] General Solution
                [%s9%s] Exit
                %s
                Please enter number:\s""";
        return String.format(menuMainTemplate, BLUE, YELLOW, BLUE, CYAN, BLUE, CYAN, BLUE, CYAN, BLUE, RESET);
    }

    private static String getSolutionMenu(String solutionType) {
        var menuSolutionTemplate = """
                %s
                %s    SOLUTION MENU: %s%s
                [%s1%s] Enter a shape and point to test
                [%s2%s] Run basic problem tests
                [%s3%s] Run general problem tests
                [%s8%s] Back to Main Menu
                [%s9%s] Exit
                %s
                Please enter number:\s""";
        return String.format(menuSolutionTemplate, BLUE, GREEN, solutionType, BLUE, CYAN, BLUE, CYAN, BLUE, CYAN, BLUE, CYAN, BLUE, CYAN, BLUE, RESET);
    }

    // inject solution
    private static void handleSolutionMenu(Scanner scanner, String solutionType, BiFunction<String, String, Solvable<Boolean>> solutionFactory) {
        while (true) {
            clearScreen();
            System.out.print(getSolutionMenu(solutionType));
            int subChoice = readInt(scanner);
            scanner.nextLine();
            switch (subChoice) {
                case 1 -> runInteractiveTest(scanner, solutionFactory);
                case 2 -> runTestCases(scanner, getBasicTestCases(), solutionFactory);
                case 3 -> runTestCases(scanner, getGeneralTestCases(), solutionFactory);
                case 8 -> {
                    return;
                } // to main
                case 9 -> {
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Invalid selection. Please try again.");
                    pause(scanner);
                }
            }
        }
    }

    // interactive input
    private static void runInteractiveTest(Scanner scanner, BiFunction<String, String, Solvable<Boolean>> solutionFactory) {
        do {
            System.out.println("Enter a shape as a string of points in the form: [[x0,y0],[x1,y1],...,[xN,yN]]:");
            String shape = scanner.nextLine();
            System.out.println("Enter a point as a string in the form: [x,y]:");
            String point = scanner.nextLine();
            System.out.println("Calculating...");
            try {
                boolean result = solutionFactory.apply(shape, point).solve();
                System.out.println(result ? "TRUE! Point inside Rectangle" : "FALSE! Point outside Rectangle (or invalid Rectangle)");
            } catch (Exception e) {
                System.out.println(MessageFormat.format("Error: {0}", e.getMessage()));
            }
            System.out.println("\nPress Enter to test another shape, or type 'menu' to return to the solution menu.");
        } while (!scanner.nextLine().trim().equalsIgnoreCase("menu"));
    }

    // run test cases automatically
    private static void runTestCases(Scanner scanner, List<TestCase> testCases, BiFunction<String, String, Solvable<Boolean>> solutionFactory) {
        for (TestCase test : testCases) {
            System.out.println("Test Case: " + test.name());
            System.out.println("Shape: " + test.shape());
            System.out.println("Point: " + test.testPoint());
            System.out.println("Expected: " + (test.expected() ? "TRUE! Point inside Rectangle" : "FALSE! Point outside Rectangle (or invalid Rectangle)"));
            try {
                boolean result = solutionFactory.apply(test.shape(), test.testPoint()).solve();
                System.out.println("Actual: " + (result ? "TRUE! Point inside Rectangle" : "FALSE! Point outside Rectangle (or invalid Rectangle)"));
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("--");
        }
        System.out.println("\nPress Enter to return to the solution menu.");
        scanner.nextLine();
    }

    // read an int from the scanner with error check
    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer value.");
                scanner.next();
            }
        }
    }

    private static void pause(Scanner scanner) {
        // pause exec
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void clearScreen() {
        // todo: look for a better way to do this
        System.out.print("\n".repeat(50));
    }

    private static List<TestCase> getBasicTestCases() {
        return List.of(
                new TestCase("[[1,1], [2,7], [10,7], [9,1]]", "[8,2]", false, "Parallelogram"),
                new TestCase("[[-1,-1], [-1,1], [1,1], [1,-1], [-1,-1]]", "[0,0]", true, "Orthogonal Square"),
                new TestCase("[[1,1], [10,7], [10,1], [1,7]]", "[2,6]", false, "Bowtie"),
                new TestCase("[[1,1], [1,3], [3,3], [5,3], [5,2], [5,1]]", "[4,2]", true, "Orthogonal Rectangle"),
                new TestCase("[[0,0], [3,0], [3,10], [0,10]]", "[2,10]", false, "Point not strictly inside"));
    }

    private static List<TestCase> getGeneralTestCases() {
        return List.of(
                new TestCase("[[0,4], [4,7], [7,3], [3,0]]", "[2,5]", true, "Rotated square and inside"),
                new TestCase("[[0,4], [4,7], [7,3], [3,0]]", "[1,2]", false, "Rotated square but outside"),
                new TestCase("[[0,4], [4,7], [7,3], [3,0]]", "[3,0]", false, "Rotated square but on edge"),
                new TestCase("[[0,4], [8,10], [11,6], [3,0]]", "[5,5]", true, "Rotated rectangle and inside"),
                new TestCase("[[0,4], [8,10], [11,6], [3,0]]", "[7,2]", false, "Rotated rectangle but outside"),
                new TestCase("[[0,4], [8,10], [11,6], [3,0]]", "[8,10]", false, "Rotated rectangle but on edge"),
                new TestCase("[[0,4], [4,7], [8,10], [11,6], [7,3], [3,0]]", "[5,5]", true, "Rotated rectangle w/extra and inside"),
                new TestCase("[[0,4], [4,7], [8,10], [11,6], [7,3], [3,0]]", "[7,2]", false, "Rotated rectangle w/extra but outside"),
                new TestCase("[[0,4], [4,7], [8,10], [11,6], [7,3], [3,0]]", "[11,6]", false, "Rotated rectangle w/extra but on edge"),
                new TestCase("[[3.5, 1.2], [1.9, 3.8], [4.0, 7.9], [8.1, 6.1], [7.4, 2.5]]", "[5,5]", false, "Some pentagon"),
                new TestCase("[[2,1], [4,1], [5,3], [4,5], [2,5], [1,3]]", "[3,3]", false, "Some hexagon"));
    }

    // I can't even remember life before records :)
    private record TestCase(String shape, String testPoint, boolean expected, String name) {
    }
}