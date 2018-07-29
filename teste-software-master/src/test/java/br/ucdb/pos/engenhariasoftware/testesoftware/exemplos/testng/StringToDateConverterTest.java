package br.ucdb.pos.engenhariasoftware.testesoftware.exemplos.testng;

import br.ucdb.pos.engenhariasoftware.testesoftware.converter.StringToDateConverter;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static br.ucdb.pos.engenhariasoftware.testesoftware.util.Constantes.DD_MM_YYYY;
import static org.testng.Assert.*;

public class StringToDateConverterTest {

    @Test
    public void stringNula(){
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        assertEquals(stringToDateConverter.convert(null),null);
    }

    @Test
    public void stringVazia(){
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        assertEquals(stringToDateConverter.convert(""),null);
    }

    @Test
    public void stringEspaco(){
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        assertEquals(stringToDateConverter.convert(" "),null);
    }

    @Test
    public void stringDataValidaPassado() throws ParseException {
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        DateFormat formatter = new SimpleDateFormat(DD_MM_YYYY);
        assertEquals(stringToDateConverter.convert("14/06/2018"), formatter.parse("14/06/2018"));
    }

    @Test
    public void stringDataValidaCorrente() throws ParseException {
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        DateFormat formatter = new SimpleDateFormat(DD_MM_YYYY);
        assertEquals(stringToDateConverter.convert("14/07/2018"), formatter.parse("14/07/2018"));
    }

    @Test
    public void stringDataValidaFutura() throws ParseException {
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        DateFormat formatter = new SimpleDateFormat(DD_MM_YYYY);
        assertEquals(stringToDateConverter.convert("30/07/2018"), formatter.parse("30/07/2018"));
    }

    @Test (expectedExceptions = IllegalStateException.class)
    public void stringDataInvalida() throws ParseException {
        StringToDateConverter stringToDateConverter = new StringToDateConverter();
        DateFormat formatter = new SimpleDateFormat(DD_MM_YYYY);
        assertEquals(stringToDateConverter.convert("30/07/yyyy"), formatter.parse("30/07/2018"));
    }
}
