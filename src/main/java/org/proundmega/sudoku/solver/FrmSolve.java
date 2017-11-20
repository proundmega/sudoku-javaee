package org.proundmega.sudoku.solver;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import lombok.Data;
import org.proundmega.sudokucore.IteradorSudoku;
import org.proundmega.sudokucore.Sudoku;
import org.proundmega.sudokucore.Respuesta;

@Data
@Named(value = "frmSolve")
@SessionScoped
public class FrmSolve implements Serializable {
    private String[][] celdas;
    private IteradorSudoku pasos;
    private boolean resuelto;
    
    private static final String VACIO = "";
    private static final Integer[] INDICES;
    
    static {
        INDICES = new Integer[9];
        for(int tupla = 0; tupla < 9; tupla++) {
            INDICES[tupla] = tupla;
        }
    }
    
    public FrmSolve() {
    }
    
    @PostConstruct
    private void init() {
        inicializarSudoku();
        resuelto = false;
    }

    private void inicializarSudoku() {
        celdas = new String[9][9];
        
        for(int fila = 0; fila < 9; fila++) {
            for(int columna = 0; columna < 9; columna++) {
                celdas[fila][columna] = VACIO;
            }
        }
    }
    
    public void solveSudoku() {
        Sudoku sudokuAResolver = new Sudoku(celdas);
        pasos = sudokuAResolver.solveAsIterador();
        
        resuelto = true;
    }
    
    public Integer[] getIndices() {
        return INDICES;
    }
    
    public void limpiar() {
        init();
    }
    
    public void siguiente() {
        pasos.siguiente();
    }
}
