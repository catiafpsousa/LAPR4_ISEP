

#define NUM_COLUNAS 6
#define NUM_LINHAS 6

#define NOME_MEMORIA_PARTILHADA "/quadro"
#define TAMANHO_MEMORIA_PARTILHADA (NUM_COLUNAS * NUM_LINHAS * sizeof(int))
#define NOME_SEMAFORO_COL_QUADRO "/sem_"
#define NOME_SEMAFORO_LEITURA "/sem_leitor"
#define TEMPO_BLOQUEIO_COLUNAS 20

typedef struct
{
    int quadro[NUM_LINHAS][NUM_COLUNAS];
} Board;