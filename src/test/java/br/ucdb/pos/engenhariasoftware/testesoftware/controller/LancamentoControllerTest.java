package br.ucdb.pos.engenhariasoftware.testesoftware.controller;


import br.ucdb.pos.engenhariasoftware.testesoftware.converter.DateToStringConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.MoneyToStringConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.StringToMoneyConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Categoria;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Lancamento;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.TipoLancamento;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class LancamentoControllerTest {

    // Conclusão:
    // Após efetuar os dois testes viu-se que a forma 2 utilizada, forma esta de comparação de valores percorrendo a lista
    // foi mais eficiente que a do JSON Path, pois quando colocamos valores onde a primeira casa é menor que a segunda,
    // ele retorna este número como o menor, independente da quantidade de casas decimais.
    // Exemplo: No JSON Path utilizando os valores 20 e 150, ele retorna o 150 como menor valor pois o 1 é menor que o 2.
    //          Já percorrendo a lista e comparando os valores ele retorna o 20 como menor valor.

    @BeforeTest
    public void atributosDeAcessoPadrao() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void ListaCom1Lancamento() {
        limpaBaseDeDados();
        List<Lancamento> lancamentos = new ArrayList<>();
        lancamentos.add(criaLancamento(new BigDecimal("20.00")));
        inseriLancamento(lancamentos);
        assertEquals(menorValorJson(),new BigDecimal("20.00"));
        assertEquals(menorValorJava(),new BigDecimal("20.00"));
    }

    @Test
    public void ListaCom2Lancamentos(){
        limpaBaseDeDados();
        List<Lancamento> lancamentos = new ArrayList<>();
        lancamentos.add(criaLancamento(new BigDecimal("20.00")));
        lancamentos.add(criaLancamento(new BigDecimal("500.00")));
        inseriLancamento(lancamentos);
        assertEquals(menorValorJson(),new BigDecimal("20.00"));
        assertEquals(menorValorJava(),new BigDecimal("20.00"));
    }

    @Test
    public void ListaCom8Lancamentos(){
        limpaBaseDeDados();
        List<Lancamento> lancamentos = new ArrayList<>();
        lancamentos.add(criaLancamento(new BigDecimal("20.00")));
        lancamentos.add(criaLancamento(new BigDecimal("500.00")));
        lancamentos.add(criaLancamento(new BigDecimal("500.00")));
        lancamentos.add(criaLancamento(new BigDecimal("300.00")));
        lancamentos.add(criaLancamento(new BigDecimal("759.00")));
        lancamentos.add(criaLancamento(new BigDecimal("555.00")));
        lancamentos.add(criaLancamento(new BigDecimal("759.00")));
        lancamentos.add(criaLancamento(new BigDecimal("324.00")));
        inseriLancamento(lancamentos);
        assertEquals(menorValorJson(),new BigDecimal("20.00"));
        assertEquals(menorValorJava(),new BigDecimal("20.00"));
    }

    public BigDecimal menorValorJson(){
        String valor = JsonPath.with(buscaLancamentos()).getString("lancamentos.min{it.valor}.valor");
        valor = valor.replace(",",".");
        return new BigDecimal(valor);
    }

    public BigDecimal menorValorJava(){
        List<Lancamento> lancamentos = retornaLancamento();
        if(!lancamentos.isEmpty()){
            BigDecimal menorValor = lancamentos.get(0).getValor();
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

    public void inseriLancamento(List<Lancamento> lancamentos){
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

    public InputStream buscaLancamentos(){
        Response response = given()
                .when()
                .body("Assured")
                .post("/buscaLancamentos");
        InputStream inputStream = response.asInputStream();
        assertEquals(response.getStatusCode(), 200);

        return inputStream;
    }

    public List<Lancamento> retornaLancamento(){
        return JsonPath.with(buscaLancamentos()).getList("lancamentos", Lancamento.class);
    }

    public void limpaBaseDeDados() {

        List<Lancamento> lancamentos = retornaLancamento();
        for (Lancamento lancamento : lancamentos) {
            Response response = given().pathParam("id", lancamento.getId()).when().get("/remover/{id}");
        }
    }

    public Lancamento criaLancamento(BigDecimal valor){


        Lancamento lancamento = new Lancamento();

        Random random = new Random();
        LocalDateTime dataHoraBase = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.set(dataHoraBase.getYear(), dataHoraBase.getMonth().getValue(), random.nextInt(27) + 1);

        lancamento.setDataLancamento(calendar.getTime());
        lancamento.setDescricao("Trabalho Assured Mayara");
        lancamento.setValor(valor);

        boolean tipo = random.nextBoolean();

        if (tipo)
            lancamento.setTipoLancamento(TipoLancamento.ENTRADA);
        else
            lancamento.setTipoLancamento(TipoLancamento.SAIDA);

        int retorno = random.nextInt(7);

        if (retorno == 0) {
            lancamento.setCategoria(Categoria.ALIMENTACAO);
        } else if (retorno == 1) {
            lancamento.setCategoria(Categoria.SALARIO);
        } else if (retorno == 2){
            lancamento.setCategoria(Categoria.LAZER);
        }else if(retorno ==3){
            lancamento.setCategoria(Categoria.TELEFONE_INTERNET);
        }else if (retorno == 4){
            lancamento.setCategoria(Categoria.CARRO);
        }else if (retorno == 5){
            lancamento.setCategoria(Categoria.EMPRESTIMO);
        }else if (retorno == 6){
            lancamento.setCategoria(Categoria.INVESTIMENTOS);
        }else{
            lancamento.setCategoria(Categoria.OUTROS);
        }

        return lancamento;
    }
}
