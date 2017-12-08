# PackingRectanglesOptimizer
Different methods of solving the packing rectangles problem (see https://en.wikipedia.org/wiki/Packing_problems#Packing_rectangles)

## Installation
```
git clone https://github.com/danielroth1/PackingRectanglesOptimizer.git && cd PackingRectanglesOptimizer && mkdir build && javac src/main/GuiControl.java -d build -sourcepath src/
```

## Execution
```
cd build
java main.GuiControl
```

## Features

- three methods of solving the packing rectangles problem are implemented
- it can be chosen between the three different optimization algorithms: local search, simulated annealing, tabu search
- visualization of the search process
- by clicking on the menu entry Test->Test test runs over multiple randomly created instances with all three methods and search algorithms are executed. At the end, the results are visualized.

## Screenshots
Visualization after the execution of a local search run: the dashed line represents the surrounding rectangle of the previously greedily applied approach.
![packing rectangles optimizer_191](https://user-images.githubusercontent.com/34305776/33768229-8494a998-dc25-11e7-929e-eb608a5a0477.png)

Example test results: The red bard indicates the improvements from the greedy approach.
![_192](https://user-images.githubusercontent.com/34305776/33768245-9083452a-dc25-11e7-99b9-2ab8368672c3.png)


