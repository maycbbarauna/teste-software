package br.ucdb.pos.engenhariasoftware.testesoftware.controller;


import br.ucdb.pos.engenhariasoftware.testesoftware.converter.DateToStringConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.MoneyToStringConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.StringToMoneyConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Lancamento;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class LancamentoControllerTest {

    @BeforeTest
    public void atributosDeAcessoPadrao() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void ListaCom1Lancamento() {
        limpaBaseDeDados();
        List<Lancamento> lancamentos = new ArrayList<>();
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(20.00)).build());
        inseriLancamento(lancamentos);
        assertEquals(menorValorJson(),"20.00");
        assertEquals(menorValorJava(),"20.00");
    }

    @Test
    public void ListaCom2Lancamentos(){
        limpaBaseDeDados();
        List<Lancamento> lancamentos = new ArrayList<>();
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(20)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(500)).build());
        inseriLancamento(lancamentos);
        assertEquals(menorValorJson(),"20.00");
        assertEquals(menorValorJava(),"20.00");
    }

    @Test
    public void ListaCom8Lancamentos(){
        limpaBaseDeDados();
        List<Lancamento> lancamentos = new ArrayList<>();
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(20)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(500)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(500)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(100)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(759)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(155)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(759)).build());
        lancamentos.add(new LancamentoBuilder().lancamento(new BigDecimal(324)).build());
        inseriLancamento(lancamentos);
        assertEquals(menorValorJson(),"20.00");
        assertEquals(menorValorJava(),"20.00");
    }

    private BigDecimal menorValorJson(){
        String valor = JsonPath.with(buscaLancamentos()).getString("lancamentos.min{it.valor}.valor");
        valor = valor.replace(",",".");
        return new BigDecimal(valor);
    }

    private BigDecimal menorValorJava(){
        List<Lancamento> lancamentos = retornaLancamento();
        if(!lancamentos.isEmpty()){
            BigDecimal menorValor = BigDecimal.ZERO;
            for(Lancamento lancamento : lancamentos){
                if(lancamento.getValor().compareTo(menorValor) == -1){
                    menorValor = BigDecimal.ZERO;
                    menorValor = menorValor.add(lancamento.getValor());
                }
            }
            return menorValor;
        }
        return BigDecimal.ZERO;
    }

    private void inseriLancamento(List<Lancamento> lancamentos){
        for(Lancamento lancamento : lancamentos) {
            Response response = given().when()
                    .formParam("descricao", lancamento.getDescricao())
                    .formParam("valor", new MoneyToStringConverter().convert(lancamento.getValor()))
                    .formParam("dataLancamento", new DateToStringConverter().convert(lancamento.getDataLancamento()))
                    .formParam("tipoLancamento", lancamento.getTipoLancamento())
                    .formParam("categoria", lancamento.getCategoria())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .post("/salvar");

            assertEquals(response.getStatusCode(), 302);
        }
    }

    private InputStream buscaLancamentos(){
        Response response = given()
                .when()
                .body("Assured")
                .post("/buscaLancamentos");
        InputStream inputStream = response.asInputStream();
        assertEquals(response.getStatusCode(), 200);

        return inputStream;
    }

    private List<Lancamento> retornaLancamento(){
        return JsonPath.with(buscaLancamentos()).getList("lancamentos", Lancamento.class);
    }

    private void limpaBaseDeDados() {

        List<Lancamento> lancamentos = retornaLancamento();
        for (Lancamento lancamento : lancamentos) {
            Response response = given().pathParam("id", lancamento.getId()).when().get("/remover/{id}");
        }
    }
}
