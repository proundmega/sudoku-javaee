package org.proundmega.sudoku.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Data;
import org.proundmega.sudokucore.Sudoku;

@Named
@Dependent
@Data
public class SudokuUploader implements Serializable {
    private Part archivo;
    
    private static final long UID = 1L;
    private static final Pattern PATRON_SUDOKU = Pattern.compile(Pattern.quote("[") + "(.*?){1}" + Pattern.quote("]"));
    
    public Sudoku crearSudoku() throws IOException {
        try {
            InputStream inputStream = archivo.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String[][] valores = reader.lines()
                    .filter(linea -> !linea.trim().isEmpty())
                    .map(this::getValores)
                    .toArray(String[][]::new);

            return new Sudoku(valores);
        } catch (IllegalArgumentException ex) {
            throw new InvalidFormatException("El archivo no cumple con el formato parseable de un sudoku", ex);
        }

    }

    private String[] getValores(String linea) {
        Matcher matcher = PATRON_SUDOKU.matcher(linea);
        String[] lineas = new String[9];

        for (int fila = 0; matcher.find(); fila++) {
            lineas[fila] = matcher.group(1);
        }
        return lineas;
    }
}
