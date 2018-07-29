package br.ucdb.pos.engenhariasoftware.testesoftware.service;

import br.ucdb.pos.engenhariasoftware.testesoftware.anotacoes.AnotacoesParaTeste;
import br.ucdb.pos.engenhariasoftware.testesoftware.controller.vo.LancamentoVO;
import br.ucdb.pos.engenhariasoftware.testesoftware.controller.vo.ResultadoVO;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.StringToDateConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Lancamento;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.TipoLancamento;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static br.ucdb.pos.engenhariasoftware.testesoftware.modelo.TipoLancamento.SAIDA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotEquals;

public class LancamentoServiceTest {

    @Mock
    private LancamentoService lancamentoService;

    @BeforeClass
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @DataProvider(name = "lancamentos")
    protected Object[][] getLancamentos(Method metodo) {
            AnotacoesParaTeste anotacoesParaTeste = metodo.getAnnotation(AnotacoesParaTeste.class);
            List<Lancamento> lancamentoVOs = new ArrayList<>();
            for(int i = 0; i < anotacoesParaTeste.tamanhoLancamentos(); i++){
                lancamentoVOs.add(new LancamentoBuilder().geraRegistroAleatorio().build());
            }
            return new Object[][]{
                    new Object[]{   }
            };
        }


    @Test(dataProvider = "lancamentos")
    public void getTotalSaidaTest(List<Lancamento> lancamentoVOs){
        given(lancamentoService.getTotalSaida(lancamentoVOs)).willCallRealMethod();
        given(lancamentoService.somaValoresPorTipo(lancamentoVOs, SAIDA)).willCallRealMethod();

        final BigDecimal totalEsperado = BigDecimal.valueOf(658.12);
        final BigDecimal totalObtido = lancamentoService.getTotalSaida(lancamentoVOs);
        assertEquals(totalObtido, totalEsperado);
    }

    @Test(dataProvider = "lancamentos")
    public void getTotalSaidaTestExerc(List<Lancamento> lancamentoVOs){
        given(lancamentoService.getTotalSaida(lancamentoVOs)).willCallRealMethod();
        given(lancamentoService.somaValoresPorTipo(lancamentoVOs, SAIDA)).willCallRealMethod();

        final BigDecimal totalEsperado = BigDecimal.valueOf(500);
        final BigDecimal totalObtido = lancamentoService.getTotalSaida(lancamentoVOs);
        assertNotEquals(totalObtido, totalEsperado);
    }
    @Test(dataProvider = "lancamentos")
    @AnotacoesParaTeste(tamanhoLancamentos = 10)
    public void testeTamanho10(List<Lancamento> lancamentoVOs){
        testeAjax(lancamentoVOs,10);
    }

    @Test(dataProvider = "lancamentos")
    @AnotacoesParaTeste(tamanhoLancamentos = 9)
    public void testeTamanho9(List<Lancamento> lancamentoVOs){
        testeAjax(lancamentoVOs,9);
    }

    @Test(dataProvider = "lancamentos")
    @AnotacoesParaTeste(tamanhoLancamentos = 3)
    public void testeTamanho3(List<Lancamento> lancamentoVOs){
        testeAjax(lancamentoVOs,3);
    }

    @Test(dataProvider = "lancamentos")
    @AnotacoesParaTeste(tamanhoLancamentos = 1)
    public void testeTamanho1(List<Lancamento> lancamentoVOs){
        testeAjax(lancamentoVOs,1);
    }

    @Test(dataProvider = "lancamentos")
    @AnotacoesParaTeste(tamanhoLancamentos = 0)
    public void testeTamanho0(List<Lancamento> lancamentoVOs){
        testeAjax(lancamentoVOs,0);
    }

    private void testeAjax(List<Lancamento> lancamentoVOs, int tamanhoEsperado){
        when(lancamentoService.getTotalEntrada(anyListOf(Lancamento.class))).thenCallRealMethod();
        when(lancamentoService.getTotalSaida(anyListOf(Lancamento.class))).thenCallRealMethod();
        when(lancamentoService.somaValoresPorTipo(anyListOf(Lancamento.class), any(TipoLancamento.class))).thenCallRealMethod();
        when(lancamentoService.getResultadoVO(anyListOf(Lancamento.class), anyInt(), anyLong())).thenCallRealMethod();
        when(lancamentoService.busca(anyString())).thenReturn(lancamentoVOs);
        when(lancamentoService.conta(anyString())).thenReturn((long) lancamentoVOs.size());
        when(lancamentoService.tamanhoPagina()).thenReturn(10);
        given(lancamentoService.buscaAjax(anyString())).willCallRealMethod();

        ResultadoVO resultadoVO = lancamentoService.buscaAjax(anyString());
        getBuscaAjaxTotalSaidaTest(lancamentoVOs);
        getBuscaAjaxTotalEntradaTest(lancamentoVOs);
        validarTamanhoLista(lancamentoVOs,tamanhoEsperado);
        validarExisteCampo(lancamentoVOs, resultadoVO);
        validarValorCampo(lancamentoVOs, resultadoVO);
    }

    //Método para verificar o tamanho da lista
    public void getBuscaAjaxTamListaTest(List<Lancamento> lancamentoVOs,String stringParaBusca){
        final Long tamanhoLista =  10L;
        final Long tamanhoObtido = lancamentoService.buscaAjax(stringParaBusca).getTotalRegistros();
        String erroTamListaInesperado = "O tamanho da lista deve ser 10";
        assertEquals(tamanhoLista,tamanhoObtido,erroTamListaInesperado);

    }

    // Método para contar a quantidade de tipoLancamento da lista gerada aleatória
    public BigDecimal contaTotalPorTipo( List<Lancamento> lancamentoVOs,TipoLancamento tipoLancamento){
        if(lancamentoVOs.isEmpty())
            return BigDecimal.ZERO;

       BigDecimal qtd = BigDecimal.ZERO;
        for(Lancamento lanc : lancamentoVOs){
            if(lanc.getTipoLancamento().equals(tipoLancamento)){
                qtd.add(qtd.add(BigDecimal.ONE));
            }
        }
        return qtd;
    }

    //Método para verificar o total de saída

    public void getBuscaAjaxTotalSaidaTest(List<Lancamento> lancamentoVOs){
        final BigDecimal totalEsperado = contaTotalPorTipo(lancamentoVOs, SAIDA);
        final BigDecimal totalObtido = lancamentoService.getTotalSaida(lancamentoVOs);
        String erroTotalSaidaInesperado = "O total de saída não é o esperado!";
        assertEquals(totalEsperado,totalObtido,erroTotalSaidaInesperado);

    }

    private void validarTamanhoLista(List<Lancamento> lancamentoVOs, int tamanhoEsperado){
        long tamanhoObtido = lancamentoService.buscaAjax(anyString()).getTotalRegistros();
        assertEquals(tamanhoObtido, tamanhoEsperado,  tamanhoEsperado + " != " + tamanhoObtido );
    }

    //Método para verificar o total de entrada

    public void getBuscaAjaxTotalEntradaTest(List<Lancamento> lancamentoVOs){
        final BigDecimal totalEsperado = contaTotalPorTipo(lancamentoVOs, TipoLancamento.ENTRADA);
        final BigDecimal totalObtido = lancamentoService.getTotalEntrada(lancamentoVOs);
        String erroTotalEntradaInesperado = "O total de entrada não é o esperado!";
        assertEquals(totalEsperado,totalObtido,erroTotalEntradaInesperado);

    }

    private void validarExisteCampo(List<Lancamento> lancamentoVOs, ResultadoVO resultadoVO){
        Field[] campos = Lancamento.class.getDeclaredFields();
        for (LancamentoVO lancamentoVO : resultadoVO.getLancamentos()) {
            for (Field campo : campos) {
                assertTrue(comparaCampos(lancamentoVO, campo.getName()), campo.getName() + " não está presente no LancamentoVO.");
            }
        }
    }

    private void validarValorCampo(List<Lancamento> lancamentoVOs, ResultadoVO resultadoVO){
        Field[] campos = Lancamento.class.getDeclaredFields();
        for (LancamentoVO lancamentoVO : resultadoVO.getLancamentos()) {
            for (Field campo : campos) {
                String valorObtido = getMethodValue(lancamentoVO, campo.getName()).toString();
                assertTrue(!valorObtido.equals("") && valorObtido != null, campo.getName() + " é nulo dentro LancamentoVO.");
            }
        }
    }

    //Método para garantir que tod(o) atributo da classe lancamento esteja na lancamentoVO e nao nula

    static class LancamentoBuilder{
        private Lancamento lancamento;

        LancamentoBuilder(){
            lancamento = new Lancamento();
        }

        LancamentoBuilder comDescricao(String descricao){
            lancamento.setDescricao(descricao);
            return this;
        }
        LancamentoBuilder comData(String data){
            StringToDateConverter stringToDateConverter = new StringToDateConverter();
            lancamento.setDataLancamento(stringToDateConverter.convert(data));
            return this;
        }
        LancamentoBuilder comValor(double valor){
            lancamento.setValor(BigDecimal.valueOf(valor));
            return this;
        }
        LancamentoBuilder comTipo(TipoLancamento tipo){
            lancamento.setTipoLancamento(tipo);
            return this;
        }

        LancamentoBuilder comId(Integer id){
            lancamento.setId(id);
            return this;
        }

        // Popula o objeto aleatóriamente chamando os metodos de retorno aleatório
        LancamentoBuilder geraRegistroAleatorio(){
            return comId(geraIDAleatorio())
                    .comData(geraDataAleatoria())
                    .comTipo(geraTipoLancamentoAleatorio())
                    .comDescricao(geraDescricaoAleatoria())
                    .comValor(geraValorAleatorio());
        }

        public Integer geraIDAleatorio(){
            return new Random().nextInt();
        }

        public String geraDataAleatoria(){
            return String.valueOf(new Random().nextInt(27)+1)+
                    String.valueOf(new Random().nextInt(11)+1)+"2018";
        }

        public TipoLancamento geraTipoLancamentoAleatorio(){
            Integer retorno;

            retorno = new Random().nextInt(1);

            if(retorno==0)
                return TipoLancamento.ENTRADA;

            return SAIDA;
        }

        public String geraDescricaoAleatoria(){
            return "Registro com descricao aleatória número "+new Random().nextInt();
        }

        public Double geraValorAleatorio(){
            return Double.valueOf(new Random().nextInt()/new Random().nextInt());
        }

        Lancamento build(){
            return lancamento;
        }
    }

    private boolean comparaCampos(LancamentoVO lancamentoVO, String nomeCampo) {
        Field[] campos = lancamentoVO.getClass().getDeclaredFields();
        for(Field campo: campos){
            if(campo.getName().equals(nomeCampo))
                return true;
        }
        return false;
    }

    private BigDecimal calculaTotal(List<Lancamento> lancamentoVOs, TipoLancamento tipoLancamento){
        BigDecimal total = new BigDecimal(0);

        for(Lancamento lancamento :lancamentoVOs){
            if(lancamento.getTipoLancamento() == tipoLancamento)
                total.add(lancamento.getValor());
        }

        return total;
    }

    private Object getMethodValue(Object object, String atributo){
        try {
            String metodoGetGerado = "get" + atributo.substring(0, 1).toUpperCase() + atributo.substring(1);
            Method metodo = object.getClass().getMethod(metodoGetGerado, new Class[]{});
            return metodo.invoke(object, new Object[]{});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
