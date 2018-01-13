package org.proundmega.sudoku.solver;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import lombok.Data;
import org.proundmega.sudoku.io.SudokuUploader;
import org.proundmega.sudoku.usuario.UsuarioData;
import org.proundmega.sudokucore.*;
import org.proundmega.sudokucore.io.InvalidSudokuFormatException;
import org.slf4j.Logger;

@Data
@Named
@ViewScoped
public class FrmIndex implements Serializable {

    private String[][] celdas;
    private IteradorSudoku pasos;
    private boolean archivoSubido;

    @Inject
    private SudokuUploader sudokuUploader;

    @Inject
    private Logger logger;
    
    @Inject
    private UsuarioData usuarioData;

    private static final String VACIO = "";
    private static final Integer[] INDICES;

    static {
        INDICES = new Integer[9];
        for (int tupla = 0; tupla < 9; tupla++) {
            INDICES[tupla] = tupla;
        }
    }

    public FrmIndex() {
    }

    @PostConstruct
    private void init() {
        inicializarSudoku();
        archivoSubido = false;
    }

    private void inicializarSudoku() {
        celdas = new String[9][9];

        for (int fila = 0; fila < 9; fila++) {
            for (int columna = 0; columna < 9; columna++) {
                celdas[fila][columna] = VACIO;
            }
        }
    }

    public void solveSudoku() {
        usuarioData.setSudokuActual(new Sudoku(celdas));
    }

    public Integer[] getIndices() {
        return INDICES;
    }

    public void limpiar() {
        init();
    }

    public void subirArchivo() {
        try {
            Sudoku crearSudoku = sudokuUploader.crearSudoku();
            celdas = crearSudoku.getGridAsString();
        } catch (IOException ex) {
            crearErrorFaces("No se pudo subir el archivo al servido, contacte a IT sobre el error");
            logger.error("No se pudo leer el archivo enviado", ex);
        } catch (InvalidSudokuFormatException ex) {
            crearErrorFaces("El archivo no posee el formato adecuado");
            logger.info("Se intento subir un archivo como sudoku sin exito alguno");
        } catch (NullPointerException ex) {
            crearErrorFaces("Debe subir un archivo para usar la importacion por archivo");
            logger.info("Se intento subir un archivo como sudoku sin exito alguno");
        }
    }

    private void crearErrorFaces(String mensaje) {
        FacesMessage error = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage("error", error);
    }
}
