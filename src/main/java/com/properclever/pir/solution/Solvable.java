package com.properclever.pir.solution;

@FunctionalInterface
public interface Solvable<T> {
    /**
     * Executes a solution and returns a result of type T.
     *
     * @return the result of the solution.
     */
    T solve();
}
