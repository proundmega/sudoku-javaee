package org.proundmega.sudoku.solver;

import javax.inject.Named;
import javax.enterprise.context.Dependent;
import lombok.Value;

@Named
@Dependent
public class CommonsSudoku {
    
    private static final String VACIO = "";
    private static final Integer[] INDICES;

    static {
        INDICES = new Integer[9];
        for (int tupla = 0; tupla < 9; tupla++) {
            INDICES[tupla] = tupla;
        }
    }
    
    public Integer[] getIndices() {
        return INDICES;
    }
}
