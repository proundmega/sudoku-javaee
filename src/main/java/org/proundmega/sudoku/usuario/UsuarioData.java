package org.proundmega.sudoku.usuario;

import java.io.Serializable;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import org.proundmega.sudokucore.Sudoku;

@Stateful
@SessionScoped
public class UsuarioData implements Serializable {
    private String[][] sudokuActual;
    
    public void setSudokuActual(Sudoku sudoku) {
        this.sudokuActual = sudoku.getGridAsString();
    }
    
    public Sudoku getSudokuActual() {
        return new Sudoku(sudokuActual);
    }
}
