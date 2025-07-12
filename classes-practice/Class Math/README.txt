# MyMath (Java Practice)

This is my first custom Java class, created as part of my practice while learning the Java programming language.

I know that Java provides its own powerful math utilities (like `Math` and `BigInteger`), but I wanted to implement my own versions of common mathematical functions to better understand how they work under the hood.

This class is not meant for production use — it's a personal learning project.

## What’s included?

The `MyMath` class contains the following methods:

- **Addition**: `mySum(int, int)`, `mySum(double, double)`
- **Subtraction**: `mySub(int, int)`, `mySub(double, double)`
- **Multiplication**: `myMult(int, int)`, `myMult(double, double)`
- **Division**: `myDiv(int, int)`, `myDiv(double, double)` (with division-by-zero checks)
- **Power**: `myRaiseToPower(double, double)` — raises a number to a positive or negative power
- **Absolute value**: `myAbs(int)`, `myAbs(double)`
- **Square root**: `mySqrtNewton(double)` — square root using the Newton-Raphson method
- **Factorial**: `myFactorial(int)` — uses `BigInteger` to support large values
- **Quadratic equation solver**: `myQuadEquation(int a, int b, int c)` — returns real roots if available

---

This repository reflects my current progress and will evolve as I continue to improve my Java skills.

Thanks for checking it out!