package com.asi.timer.backend.pdfprinter.drawing;

import com.asi.timer.backend.pdfprinter.model.Column;
import com.asi.timer.backend.pdfprinter.model.Columns;
import com.itextpdf.text.Font;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableGeneratorTest {

    @Test
    void testLongName() {

        Font font = new Font(Font.FontFamily.UNDEFINED, 11f);

        List<Column> columns = Columns.getStartListHeader();

        String result = TableGenerator.getCorrectCellContent(
                "ABCDEFGHIJKLMNOPQRST",
                columns,
                2,
                font
        );

        System.out.println(result);

    }


}