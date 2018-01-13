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
import org.proundmega.sudokucore.io.FileToSudoku;

@Named
@Dependent
@Data
public class SudokuUploader implements Serializable {

    private Part archivo;

    public Sudoku crearSudoku() throws IOException {
        InputStream inputStream = archivo.getInputStream();
        return new FileToSudoku().crearSudoku(new InputStreamReader(inputStream));
    }

}
