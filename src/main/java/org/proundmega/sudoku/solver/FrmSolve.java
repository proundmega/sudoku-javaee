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
import org.proundmega.sudoku.io.InvalidFormatException;
import org.proundmega.sudoku.io.SudokuUploader;
import org.proundmega.sudokucore.*;
import org.slf4j.Logger;

@Data
@Named(value = "frmSolve")
@ViewScoped
public class FrmSolve implements Serializable {

    private String[][] celdas;
    private IteradorSudoku pasos;
    private boolean resuelto;

    @Inject
    private SudokuUploader sudokuUploader;

    @Inject
    private Logger logger;

    private static final String VACIO = "";
    private static final Integer[] INDICES;

    static {
        INDICES = new Integer[9];
        for (int tupla = 0; tupla < 9; tupla++) {
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

        for (int fila = 0; fila < 9; fila++) {
            for (int columna = 0; columna < 9; columna++) {
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

    public String getStyleDeCelda(int fila, int columna) {
        if (pasos.getActual().isAvanceEnResolver()) {
            MetadataSolver metadatos = pasos.getActual().getMetadatos();
            Posicion posicionResuelta = metadatos.getPosicionResuelta();

            if (esLaCasillaResuelta(posicionResuelta, fila, columna)) {
                return "celda_resuelta";
            }

            if (esUnaCasillaQueLimitaSudoku(metadatos, fila, columna)) {
                return "celda_limitante";
            }
        }
        return "";
    }

    private static boolean esLaCasillaResuelta(Posicion posicionResuelta, int fila, int columna) {
        return posicionResuelta.getFila().getIndiceFilaParaArray() == fila
                && posicionResuelta.getColumna().getIndiceColumnaParaArray() == columna;
    }

    private boolean esUnaCasillaQueLimitaSudoku(MetadataSolver metadata, int fila, int columna) {
        return metadata.getCeldasQueLimitanValor().stream()
                .anyMatch(posicion -> posicion.getFila().getIndiceFilaParaArray() == fila
                && posicion.getColumna().getIndiceColumnaParaArray() == columna);
    }

    public void subirArchivo() {
        try {
            Sudoku crearSudoku = sudokuUploader.crearSudoku();
            celdas = crearSudoku.getGridAsString();
        } catch (IOException ex) {
            crearErrorFaces("No se pudo subir el archivo al servido, contacte a IT sobre el error");
            logger.error("No se pudo leer el archivo enviado", ex);
        } catch (InvalidFormatException ex) {
            crearErrorFaces("El archivo no posee el formato adecuado");
            logger.info("Se intento subir un archivo como sudoku sin exito alguno");
        }
    }

    private void crearErrorFaces(String mensaje) {
        FacesMessage error = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage("error", error);
    }
}
