package br.ucdb.pos.engenhariasoftware.testesoftware.exemplos.testng;

import br.ucdb.pos.engenhariasoftware.testesoftware.converter.DateToStringConverter;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static br.ucdb.pos.engenhariasoftware.testesoftware.util.Constantes.DD_MM_YYYY;
import static org.testng.Assert.*;

public class DateToStringConverterTest {

    @Test
    public void dataNulaTest(){
        DateToStringConverter dateToStringConverter = new DateToStringConverter();
        assertEquals(dateToStringConverter.convert(null), null);
    }

    @Test
    public void dataPassadaTest() throws ParseException{
        DateToStringConverter dateToStringConverter = new DateToStringConverter();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = df.parse("14/06/2018");
            assertEquals(dateToStringConverter.convert(date),"14/06/2018");
    }

    @Test
    public void dataPresenteTest() throws ParseException{
        DateToStringConverter dateToStringConverter = new DateToStringConverter();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = df.parse("14/07/2018");
            assertEquals(dateToStringConverter.convert(date),"14/07/2018");
    }

    @Test
    public void dataFuturaTest() throws ParseException {
        DateToStringConverter dateToStringConverter = new DateToStringConverter();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = df.parse("30/07/2018");
            assertEquals(dateToStringConverter.convert(date),"30/07/2018");
    }
}
