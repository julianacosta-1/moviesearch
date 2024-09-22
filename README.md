# Projeto de Busca de Filmes

## Descrição

Este projeto é uma implementação do desafio técnico do Luiza Labs, que envolve a busca de uma sentença em um conjunto de arquivos de dados de filmes. O programa permite que o usuário busque por termos específicos e exiba a contagem e os nomes dos arquivos que contêm essas palavras-chave.

### Funcionalidades

- Processamento de arquivos ZIP contendo dados de filmes.
- Busca por sentenças em arquivos de texto, utilizando um índice invertido.
- Exibição da quantidade de ocorrências e dos nomes dos arquivos que contêm as palavras buscadas.
- Reindexação automática: o índice é reconstruído sempre que uma palavra composta é passada como parâmetro, permitindo a otimização das buscas futuras.
- Ordenação dos resultados em ordem alfabética.

## Estrutura do Projeto

- `/src/main/java/org/search/application`: Contém a lógica de aplicação.
- `/src/main/java/org/search/domain`: Contém as regras de negócio e entidades.
    - `/event`: Contém eventos relacionados à busca.
    - `/exception`: Contém exceções personalizadas.
    - `/model`: Define as entidades do domínio.
    - `/repository`: Interfaces de repositório para acessar dados.
- `/src/main/java/org/search/infrastructure`: Implementações de infraestrutura.
- `/src/main/java/org/search/utils`: Contém utilitários diversos.

## Execução do Programa

Para executar o programa e os testes, siga os passos abaixo:

1. Certifique-se de ter o Java e o Maven instalados em seu sistema.
2. Compile o projeto e gere o arquivo JAR utilizando o seguinte comando:

    ```bash
    mvn clean package
    ```

3. O JAR gerado (`search.jar`) será criado na pasta `target`. Para executar o programa, você pode fazer uma das seguintes opções:
    - **Executar o JAR diretamente na pasta `target`**:
      ```bash
      java -jar target/search.jar "walt disney"
      ```

    - **Mover o JAR para outro local** (opcional) e executá-lo de qualquer diretório:
      ```bash
      mv target/search.jar ./search.jar
      java -jar search.jar "walt disney"
      ```

O programa exibirá o tempo necessário para realizar a busca, a quantidade de ocorrências e os nomes dos arquivos que contêm as palavras buscadas.

## Decisões de Design

### Classe `IndexEntry`

A classe `IndexEntry` foi criada para facilitar a conversão do índice invertido, incluindo a frequência de cada palavra, que indica em quantos arquivos de texto a palavra aparece. Embora a ideia de ordenar as palavras pela frequência tenha sido considerada, percebi que isso não otimizaria o tempo de busca para palavras compostas.

### Reaproveitamento de Palavras-Chave Compostas

Atualmente, o sistema não armazena as palavras-chave compostas geradas a partir das buscas anteriores, o que impede sua reutilização em execuções subsequentes. Essa abordagem foi adotada para manter a simplicidade do projeto e evitar a leitura de arquivos adicionais.

No entanto, uma possível solução para permitir a reutilização seria implementar um mecanismo de cache que armazena as palavras-chave compostas:

1. **Criação de um Cache**: Utilizar uma estrutura de dados, como um `HashSet`, para armazenar as palavras-chave compostas geradas. Isso permitiria verificar rapidamente se uma palavra composta já foi buscada.

2. **Armazenamento em Arquivo**: Persistir essas palavras-chave entre execuções, salvando o cache em um arquivo no disco e carregando-o na inicialização do programa.

3. **Atualização do Cache**: Sempre que uma nova palavra composta for gerada a partir da busca do usuário, adicione-a ao cache e, se necessário, ao arquivo.

## Observação

É importante ressaltar que, para palavras-chave que não são compostas, o programa não gera um novo arquivo de índice invertido (`invertedIndex`).

## Conclusão

Este projeto demonstra a aplicação de algoritmos de busca eficientes e a implementação de um sistema de reindexação automática para otimizar o desempenho das buscas.
