# th-point-in-rectangle
A Java tool to determine if a point is inside a rectangular shape, given an ordered list of coordinates. Supports non-corner points in shape definitions and excludes points on edges. Rejects non-rectangular shapes.


## Building and Running the Project

### Prerequisites
- Maven: Ensure Maven is installed on your system.
- JDK 21 Runtime: The project is built with JDK 21 using preview features. I've used this because it is a recent long-term support release.
- If you don’t have JDK 21 installed, you can download it from:
   - https://adoptium.net/

### Installation & Build
1.	Clone the Repository.
2.	Build the Project with Maven (you'll need to have `JAVA_HOME` pointing to sdk21):
```
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn clean package
```
This command compiles the project and creates a JAR file in the target/ directory (e.g., /path/to/project/target/th-point-in-rectangle-1.0-SNAPSHOT.jar).

### Running the Application
Once built, we can execute the JAR.
Run the JAR file using the JDK 21 runtime with preview features enabled:
```
$JAVA_HOME/bin/java --enable-preview -jar /path/to/project/target/th-point-in-rectangle-1.0-SNAPSHOT.jar
```
## Using the Tool

After building the project and creating the executable JAR, you can run the tool on the command line. When executed, the tool presents a menu-based interface that guides you through the available options. Here’s how to interact with it:

#### Main Menu
   Upon launch, the tool displays a main menu with the following options:
  - **[1] Basic Solution:**  
    Select this option to use the basic algorithm for determining if a point is strictly inside a shape.
  - **[2] General Solution:**  
    Select this option to use the general algorithm, which may handle more complex shapes.
  - **[9] Exit:**  
    This option quits the program.

#### Solution Menu
   Once you select either the Basic or General Solution, you are presented with a solution menu. The solution menu includes:
  - **[1] Enter a shape and point to test:**  
    This interactive mode allows you to manually enter:
    - A **shape**, which should be a string of points formatted as:  
      `[[x0,y0],[x1,y1],...,[xN,yN]]`
    - A **point**, formatted as:  
      `[x,y]`
      After you enter both values, the tool calculates and displays whether the point is strictly inside the shape (if it is a validated rectangle).
  - **[2] Run basic problem tests:**  
    This option automatically runs a series of built-in test cases (for the basic solution) and shows the expected versus actual results.
  - **[3] Run general problem tests:**  
    This option automatically runs built-in test cases (for the general solution, which allows rotated rectangles) to validate the algorithm. We can expect some of these to fail when using the orthogonal/basic implementation.
  - **[8] Back to Main Menu:**  
    Returns you to the main menu.
  - **[9] Exit:**  
    Exits the program.

#### Interactive Testing
   When using interactive testing:
  - You will be prompted to enter a shape and a point.
  - The tool will then process the input and display one of the following messages:
    - **TRUE! Point inside Rectangle**, if the point is strictly inside the shape.
    - **FALSE! Point outside Rectangle (or invalid Rectangle)**, if the point is not strictly inside or if the shape is not valid.
  - After the result is displayed, you’ll have the option to test another case or return to the solution menu by typing `menu`.

#### Exiting the Tool:
   You can exit (when not actively entering interactive input) by selecting option **9** from either the solution menu or the main menu. Otherwise, `ctrl`+`c`.

**N.B.** The tool uses ANSI escape codes for coloured output in the terminal. If you run it in an environment that does not support these codes, the output may appear unformatted.