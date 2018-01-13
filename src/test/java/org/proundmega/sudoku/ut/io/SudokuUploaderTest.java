package org.proundmega.sudoku.ut.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.Part;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.experimental.categories.Category;
import static org.mockito.Mockito.*;
import org.proundmega.sudoku.data.GridFactory;
import org.proundmega.sudokucore.io.InvalidSudokuFormatException;
import org.proundmega.sudoku.io.SudokuUploader;
import org.proundmega.sudokucore.Sudoku;
import org.proundmega.test.UnitTest;

@Category(UnitTest.class)
public class SudokuUploaderTest {
    
    public SudokuUploaderTest() {
    }

    @Test
    public void leerSudokuDeUnarchivoCorrectamenteYValidarlo() throws Exception {
        File archivo = TestUtils.getTestResource("data", "sudokuValido1.txt");
        Sudoku esperado = new Sudoku(GridFactory.getSudokuFacil1());
        
        SudokuUploader uploader = crearSudokuUploader(archivo);
        Sudoku obtenido = uploader.crearSudoku();
        assertNotNull(obtenido);
        assertEquals(esperado, obtenido);
    }
    
    private SudokuUploader crearSudokuUploader(File mock) throws IOException {
        SudokuUploader uploader = new SudokuUploader();
        uploader.setArchivo(crearParteDeArchivo(mock));
        
        return uploader;
    }
    
    private Part crearParteDeArchivo(File archivo) throws IOException {
        Part mock = mock(Part.class);
        when(mock.getInputStream())
                .thenReturn(new FileInputStream(archivo));
        
        return mock;
    }
    
    @Test(expected = InvalidSudokuFormatException.class)
    public void archivoConCeldasConValoresIncorrectos() throws Exception {
        File archivo = TestUtils.getTestResource("data", "sudokuInvalido1.txt");
        
        SudokuUploader uploader = crearSudokuUploader(archivo);
        Sudoku obtenido = uploader.crearSudoku();
    }
    
    @Test(expected = InvalidSudokuFormatException.class)
    public void archivoConFilaExtraMala() throws Exception {
        File archivo = TestUtils.getTestResource("data", "sudokuInvalido2.txt");
        
        SudokuUploader uploader = crearSudokuUploader(archivo);
        Sudoku obtenido = uploader.crearSudoku();
    }
    
    @Test(expected = InvalidSudokuFormatException.class)
    public void archivoConFilaFaltante() throws Exception {
        File archivo = TestUtils.getTestResource("data", "sudokuInvalido3.txt");
        
        SudokuUploader uploader = crearSudokuUploader(archivo);
        Sudoku obtenido = uploader.crearSudoku();
    }
}
