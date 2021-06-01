package br.com.zup
import br.com.zup.carro.Carro
import br.com.zup.carro.CarrosRepository
import io.micronaut.test.annotation.TransactionMode
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest(
    rollback = false, //Desativa o rollback das transções ao final dos testes

    //Já é ativo por padrão, as transações @BeforeEach, @Test e @AfterEach ocorrem separadas
    //transactionMode = TransactionMode.SEPARATE_TRANSACTIONS

    //As transações @BeforeEach e @Test occorem juntas
    transactionMode = TransactionMode.SINGLE_TRANSACTION,

    //Desativa o controle das transações dos testes, sendo assim não vai abrir, não vai ter commit e nem rollback
    //Os repositórios serão auto commit
    transactional = false
)
class CarrosTest {

    /*
    Os testes devem ser independentes e isolados, um teste não deve impactar outro teste

    Estrategias(analogia com louça):
    louça: sujou, limpou -> @AfterEach
    louça: limpou, usou -> @BeforeEach
    louça: usar louça descartavel -> rollback=true
    louça: usar a louça, jogar fora, comprar nova -> recriar o banco a cada teste
     */

    @Inject
    lateinit var repository: CarrosRepository

    @BeforeEach //Executa antes dos testes
    fun setup() {
        repository.deleteAll()
    }

    @AfterEach //Executa após os testes
    fun cleanUp() {
        repository.deleteAll()
    }

    @Test
    fun `deve inserir`() {
        // cenário

        // ação
        repository.save(Carro(modelo = "Gol", placa = "HGI-3265"))
        // validação
        assertEquals(1, repository.count())
    }

    @Test
    fun `deve encontrar carro por placa`() {
        // cenário
        repository.save(Carro(modelo = "Gol", placa = "LOA-4785"))

        // ação
        var encontrado = repository.existsByPlaca("LOA-4785")

        // validação
        assertTrue(encontrado)
    }

}
