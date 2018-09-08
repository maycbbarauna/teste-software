package br.ucdb.pos.engenhariasoftware.testesoftware.controller;

import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Categoria;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Lancamento;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.TipoLancamento;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public class LancamentoBuilder {

    public Lancamento lancamento = new Lancamento();

    LancamentoBuilder lancamento(BigDecimal valor) {
        return DataLancamento()
                .Tipo()
                .Descricao()
                .Valor(valor)
                .Categoria();
    }


    Lancamento build() {
        return lancamento;
    }

    LancamentoBuilder DataLancamento() {

        Random dia = new Random();
        LocalDateTime dataHoraBase = LocalDateTime.now();
        Calendar calendar = Calendar.getInstance();
        calendar.set(dataHoraBase.getYear(), dataHoraBase.getMonthValue(), dia.nextInt(27) + 1);

        lancamento.setDataLancamento(calendar.getTime());
        return this;
    }

    LancamentoBuilder Descricao() {
        lancamento.setDescricao("Teste REST Mayara");
        return this;
    }

    LancamentoBuilder Valor(BigDecimal valor) {

        lancamento.setValor(valor);
        return this;
    }

    LancamentoBuilder Tipo() {

        Random random = new Random();
        boolean tipo = random.nextBoolean();

        if (tipo)
            lancamento.setTipoLancamento(TipoLancamento.ENTRADA);
        else
            lancamento.setTipoLancamento(TipoLancamento.SAIDA);

        return this;
    }

    LancamentoBuilder Categoria() {

        Integer retorno;

        Random random = new Random();
        retorno = random.nextInt(7);

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
        return this;
    }
}
