# MovieSearch

## Descrição
Este projeto busca uma palavra ou frase nos arquivos de dados de filmes e retorna os arquivos que contêm a palavra-chave, bem como o número de ocorrências. O programa usa ferramentas nativas do Java e boas práticas de design de código.

## Estrutura de Pastas
- `/src/main/java/com/moviesearch/application`: Contém a lógica de aplicação.
- `/src/main/java/com/moviesearch/domain`: Contém as regras de negócio e entidades.
- `/src/main/java/com/moviesearch/infrastructure`: Implementação do repositório de arquivos.

## Execução do Projeto
1. Faça o download do repositório:
   ```bash
   git clone <link_do_repositorio>

2. Compile o projeto:
   ```bash
    mvn clean package

3. Executa o programa:
    ```bash
       java -jar target/search.jar "walt disney"