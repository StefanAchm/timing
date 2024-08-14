package com.asi.timer.backend.pdfprinter.utils;

import com.asi.timer.backend.pdfprinter.model.Column;
import com.itextpdf.text.Font;
import org.junit.jupiter.api.Test;

import java.util.List;

class TableGeneratorTest {

    @Test
    void testLongName() {

        Font font = new Font(Font.FontFamily.UNDEFINED, 11f);

        List<Column> columns = HeaderGenerator.getStartListHeader();

        String result = TableGenerator.getCorrectCellContent(
                "ABCDEFGHIJKLMNOPQRST",
                columns,
                2,
                font
        );

        System.out.println(result);

    }


}