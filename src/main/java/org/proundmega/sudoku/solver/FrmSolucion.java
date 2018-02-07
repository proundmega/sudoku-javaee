package org.proundmega.sudoku.solver;

import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.proundmega.sudoku.usuario.UsuarioData;
import org.proundmega.sudokucore.IteradorSudoku;
import org.proundmega.sudokucore.MetadataSolver;
import org.proundmega.sudokucore.Posicion;
import org.proundmega.sudokucore.Sudoku;

@Named(value = "frmSolucion")
@ViewScoped
public class FrmSolucion implements Serializable {
    private IteradorSudoku pasos;
    
    @Inject
    private UsuarioData usuarioData;
    
    public FrmSolucion() {
    }
    
    @PostConstruct
    private void init() {
        Sudoku sudokuAResolver = usuarioData.getSudokuActual();
        pasos = sudokuAResolver.solveAsIterador();
    }

    public IteradorSudoku getPasos() {
        return pasos;
    }

    public void setPasos(IteradorSudoku pasos) {
        this.pasos = pasos;
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
            
            if(esParteDelBloque(metadatos, fila, columna)) {
                return "celda_bloque";
            }
        }
        return "";
    }

    private static boolean esLaCasillaResuelta(Posicion posicionResuelta, int fila, int columna) {
        return posicionResuelta.getFila().getIndiceFilaParaArray() == fila
                && posicionResuelta.getColumna().getIndiceColumnaParaArray() == columna;
    }

    private boolean esUnaCasillaQueLimitaSudoku(MetadataSolver metadata, int fila, int columna) {
        return metadata.getPosicionBundle().getPosicionesQueLimitanValor().stream()
                .anyMatch(posicion -> posicion.getFila().getIndiceFilaParaArray() == fila
                && posicion.getColumna().getIndiceColumnaParaArray() == columna);
    }
    
    private boolean esParteDelBloque(MetadataSolver metadata, int fila, int columna) {
        return metadata.getPosicionBundle().getPosicionesLimitantesEnBloque().stream()
                .anyMatch(posicion -> posicion.getFila().getIndiceFilaParaArray() == fila
                && posicion.getColumna().getIndiceColumnaParaArray() == columna);
    }
    
    public String getExplicacionPaso() {
        Locale localActual = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return pasos.getActual().getMetadatos().getExplicacion(localActual);
    }
}
