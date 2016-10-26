# Stoichiometry
Java project that balances reactions and computes grams/moles.

## Main Features
* Reading chemical compounds (e.g. `C3H5(NO3)3(s)`) from a string: [src/stoichiometry/Compound.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/stoichiometry/Compound.java)
* Reading expressions (e.g. `H2+O2`) from a string: [src/stoichiometry/Expression.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/stoichiometry/Expression.java)
* Reading chemical reactions (e.g. `Al + I2 -> AlI3`) from a string: [src/stoichiometry/Reaction.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/stoichiometry/Reaction.java)
* Balancing chemical reactions: [src/stoichiometry/Reaction.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/stoichiometry/Reaction.java)
* Setting the grams or moles of any compound to compute the other compounds grams/moles: [src/stoichiometry/Reaction.java](https://github.com/johanvandegriff/Stoichiometry/blob/master/src/stoichiometry/Reaction.java)

## Support Features
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
