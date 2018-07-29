package br.ucdb.pos.engenhariasoftware.testesoftware.exemplos.testng;

import br.ucdb.pos.engenhariasoftware.testesoftware.exemplos.CalculadoraSimples;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;

public class CalculadoraSimplesTest {

    @Test
    public void somaNumerosPositivosTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertEquals(calculadoraSimples.soma(10, 20), 30);
    }

    @Test(groups = "somaNegativos")
    public void somaNumerosNegativosTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertEquals(calculadoraSimples.soma(-10, -20), -30);
    }

    @Test
    public void somaNumeroNegativosComPositivoTest() {
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertTrue(calculadoraSimples.soma(-10, 20) == 10);
    }

    @Test
    public void subtraiNumerosPositivosPrimeiroMaiorTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertTrue(calculadoraSimples.subtrai(10,9).compareTo(BigDecimal.ONE) >= 0);
    }

    @Test
    public void subtraiNumerosPositivosPrimeiroMenorTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertTrue(calculadoraSimples.subtrai(9,10).compareTo(new BigDecimal(-1)) <= 0);
    }

    @Test
    public void subtraiPositivoENegativoTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertTrue(calculadoraSimples.subtrai(10,-9).compareTo(new BigDecimal(19)) == 0);
       // assertEquals(calculadoraSimples.subtrai(10, -9), new BigDecimal(19));
    }

    @Test
    public void subtraiZeroEPositivoTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertTrue(calculadoraSimples.subtrai(0,9).compareTo(new BigDecimal(-9)) == 0);
    }

    @Test
    public void subtraiZeroENegativoTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();
        assertTrue(calculadoraSimples.subtrai(0,-9).compareTo(new BigDecimal(9)) == 0);
    }

    @Test
    public void dividePositivosTest(){
        CalculadoraSimples calculadoraSimples = new CalculadoraSimples();

    }
}


