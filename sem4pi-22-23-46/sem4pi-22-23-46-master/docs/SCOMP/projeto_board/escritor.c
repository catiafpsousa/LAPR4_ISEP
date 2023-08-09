#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <semaphore.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>
#include "setting.h"

void inicializar_quadro(Board *quadro)
{
    memset(quadro, 0, sizeof(Board));
}

int main()
{
    int fd;
    Board *quadro;
    sem_t *semaforos[NUM_COLUNAS];
    sem_t *leitor_semaforo;
    int coluna, linha;

    // Memória PARTILHADA
    fd = shm_open(NOME_MEMORIA_PARTILHADA, O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);
    if (fd == -1)
    {
        perror("Falha na abertura da memória partilhada");
        exit(EXIT_FAILURE);
    };
    if (ftruncate(fd, TAMANHO_MEMORIA_PARTILHADA) == -1)
    {
        perror("Falha em truncate da memória partilhada");
        exit(EXIT_FAILURE);
    }
    quadro = mmap(NULL, TAMANHO_MEMORIA_PARTILHADA, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (quadro == MAP_FAILED)
    {
        perror("Falha no mapeamento da memória partilhada");
        exit(EXIT_FAILURE);
    }

    // quadro
    inicializar_quadro(quadro);

    // semáforos para cada coluna
    for (coluna = 0; coluna < NUM_COLUNAS; coluna++)
    {
        char nome_semaforo[20];
        sprintf(nome_semaforo, "%s%d", NOME_SEMAFORO_COL_QUADRO, coluna);
        semaforos[coluna] = sem_open(nome_semaforo, O_CREAT, S_IRUSR | S_IWUSR, 0);
        if (semaforos[coluna] == SEM_FAILED)
        {
            perror("Falha ao abrir o semáforo de colunas da board");
            exit(EXIT_FAILURE);
        }
    }

    // semáforo do leitor
    leitor_semaforo = sem_open(NOME_SEMAFORO_LEITURA, 0);
    if (leitor_semaforo == SEM_FAILED)
    {
        perror("Falha ao abrir o semáforo de leitura");
        exit(EXIT_FAILURE);
    }

          
    // Processo escritor
    while (1)
    {
        int coluna_destino, linha_destino, valor;
        int tempo_dormir = TEMPO_BLOQUEIO_COLUNAS;

        printf("Introduza a linha a alterar ( -1 para terminar): ");
        scanf("%d", &linha_destino);

        if (linha_destino == -1)
            break;

        printf("Introduza a Coluna a alterar: ");
        scanf("%d", &coluna_destino);


        printf("Introduza o valor: ");
        scanf("%d", &valor);

        printf("Processo %d a tentar escrever o valor | %d | na célula (%d,%d)\n", getpid(), valor, linha_destino + 1, coluna_destino + 1);
     

        sem_wait(semaforos[coluna_destino]);

        quadro->quadro[linha_destino][coluna_destino] = valor;

        printf("Processo %d: Escrita com sucesso\n", getpid());

        sem_post(leitor_semaforo);

        // Dorme x segundos para permitir a demonstração que o bloqueio da coluna impede outros processos de alterar valores na coluna

        printf("Processo %d: A bloquear o acesso è coluna %d durante %d segundos \n\n", getpid(), coluna_destino, tempo_dormir);
        sleep(tempo_dormir);

        sem_post(semaforos[coluna_destino]);

    }

    // Libertar recursos
    for (coluna = 0; coluna < NUM_COLUNAS; coluna++)
    {
        char nome_semaforo[20];
        sprintf(nome_semaforo, "%s%d", NOME_SEMAFORO_COL_QUADRO, coluna);
        if (sem_close(semaforos[coluna]) == -1)
        {
            perror("Falha ao fechar o semáforo de colunas da board");
            exit(EXIT_FAILURE);
        }
        if (sem_unlink(nome_semaforo) == -1)
        {
            perror("Falha no unlink do semáforo de colunas");
            exit(EXIT_FAILURE);
        }
    }

    if (sem_close(leitor_semaforo) == -1)
    {
        perror("Falha ao fechar o semáforo de leitura");
        exit(EXIT_FAILURE);
    }

    if (sem_unlink(NOME_SEMAFORO_LEITURA) == -1)
    {
        perror("Falha no unlink do semáforo de leitura");
        exit(EXIT_FAILURE);
    }

    if (munmap(quadro, TAMANHO_MEMORIA_PARTILHADA) < 0)
    {
        perror("Falha no unmap da memória partilhada");
        exit(EXIT_FAILURE);
    }

    if (shm_unlink(NOME_MEMORIA_PARTILHADA) == -1)
    {
        perror("Falha no unlink da memória partilhada");
        exit(EXIT_FAILURE);
    }

    return 0;
}
