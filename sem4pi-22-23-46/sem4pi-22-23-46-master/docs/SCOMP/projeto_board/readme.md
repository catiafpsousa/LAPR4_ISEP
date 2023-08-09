**Simulação de controlo de acesso a Quadro Partilhado, no âmbito na UC de SCOMP para o Sprint B do Projeto Integrador**

[3003] As Project Manager, I want the team to "explore" the synchronization problems related to the shyncronization of shared boards and design a conceptual solution based on practical evidence.

Num contexto de uma Shared Board em que vários utilizadores queiram editar e poder aceder às suas células em simultâneo, geram-se possíveis problemas de coerência e consistência dos dados da Board. A Shared Borad configura uma área crítica cujo acesso deve implementar um mecanismo de sincronização. A abordagem preferencial para este caso seria a utilização de semáforos, que possuem por si só complexidade intrínseca à sua escolha.

Para o problema conceptual e de acordo com a situação real que iremos implementar no projeto, iremos possuir vários processos escritores e um visualizador da Shared Board. Para esta demonstração foi desenvolvida uma região de memória partilhada que contém uma matriz bidimensional que será acedida por eles, a fim de escreverem valores inteiros positivos nas suas células. Essa região trata-se, portanto, da zona crítica cujo acesso iremos controlar.

Criámos um processo leitor que irá apresentar no ecrã o quadro atualizado após cada alteração e um main que se encarrega das inicializações necessárias.

Três cenários principais podiam ser adoptados:
    
    1 --> Um semáforo que sincronize o acesso à totalidade da Board sempre que um utilizador pretende fazer a sua edição
    
    2 --> Um conjunto de semáforos que condicione o acesso a cada célula do quadro que esteja a ser editada (restringe acesso exclusivo a cada célula apenas, todas as restantes ficam livres, independentemente da coluna/linha)
    
    3 --> Um conjunto de semáforos que condicione o acesso a uma determinada coluna que esteja a ser editada (não permitindo editar qualquer linha nela contida)

    
    Em todos os cenários, está considerada a criação de um outro semáforo para controlar o acesso do leitor. O leitor terá acesso no momento de criação e sempre que algum processo escritor faça alterações

O primeiro cenário possui menor exigência de desevolvimento, seria seguro para a consistência de dados de forma eficaz. Contudo, não permitiria tirar o benefício desejado da execução concorrente de processos e todo o fluxo decorreria de forma lenta devido às limitações elevadas.

De forma oposta, o segundo cenário seria o que daria mais liberdade na execução concurrente de edição, contudo, considerando a escalabilidade do tamanho da Board, poderíamos ter um número de semáforos demasiado elevado.

Apontando para um meio termo e focando no melhor custo-benefício, optámos pela implementação do último cenário: limitar acesso concorrente à mesmo coluna do quadro.




**Pseudocódigo/Explicação:**

    Para a solução conceptual, foram implementados **3 programas**:
        1. Base
        2. Leitor
        3. Escritor

**1. Programa Base:**

    1.1. Inicializa a região de memória partilhada do tipo da estrutura Board, realizando memset a zeros da sua matriz bidimensional. 

    1.2. São criados os semáforos para cada coluna (array de semáforos), inicializados a 1, aplicando assim o padrão da exclusividade mútua.

    1.3 É criado o semáforo de leitura da Board, também inicializado a 1.

**2. Programa Leitor:**

    2.1. Abre a zona de memória partilhada correspondente à Board;

    2.2. Num ciclo infinito, é feita a impressão do conteúdo da Board, condicionada pelo semáforo de leitura (acede com sem_wait(), decrementando o semáforo; depois de apresentar o quadro atualizado, fica à espera que um dos escritores faça sem_post() ao seu semáforo de leitura para ver uma nova atualização).

    
**2. Programa Escritor:**

    2.1. Abre a zona de memória partilhada correspondente à Board;

    2.2. Abre os semáforos de acesso às colunas da Board e o o semáforo de leitura; 

    2.3. Através de um ciclo, o utilizador é questionado sobre a linha, coluna e valor da célula que pretende editar, ficando esta edição condicionada através do semáforo da respectiva coluna.

    2.4 Caso o semáforo esteja a 1, este é decrementado, sendo feita a edição do valor da célula. É incrementado o semáforo de leitura para que o processo leitor possa imprimir a nova configuração da Board e o processo é colocado a dormir durante um período de tempo definido (em segundos) no ficheiro setting.h, por forma a ser possível constatar que a edição de células desta mesma coluna está bloqueada para outros processos escritores.

    2.5 No final deste período, é desbloqueada a edição das células da coluna através do semáforo da coluna respetiva.

 A introdução de dados é finalizada quando o utilizador introduz "-1".