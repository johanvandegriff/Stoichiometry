# Stoichiometry
Java project that balances reactions and computes grams/moles.

Features:
* Fractions and fractional operations: [src/numbers/Fraction.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/numbers/Fraction.java)
* Solving systems of equations with fractional coefficients: [src/numbers/Solver.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/numbers/Solver.java)
* Python program to extract data about the elements from text files: [elements/compile_data.py](https://github.com/johanvandegriff/Stoichiometry/blob/master/elements/compile_data.py)
* Enum of all the elements with symbol, full name, atomic number, and atomic mass: [src/stoichiometry/Element.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/stoichiometry/Element.java)
* StringTable to draw a table in ascii: [src/output/StringTable.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/output/StringTable.java)


Sample output of the program:
```
+---------+----------+----------+--------+----------+
|         |   2 H2   |    O2    |   =>   |  2 H2O   |
+---------+----------+----------+--------+----------+
|  g/mol  |  2.016   |  31.998  |        |  18.015  |
+---------+----------+----------+--------+----------+
|   mol   |   6.0    |   3.0    |        |   6.0    |
+---------+----------+----------+--------+----------+
|    g    |  12.096  |  95.994  |        |  108.09  |
+---------+----------+----------+--------+----------+
```
