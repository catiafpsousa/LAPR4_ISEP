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

    // memória PARTILHADA
    fd = shm_open(NOME_MEMORIA_PARTILHADA, O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);
    if (fd == -1)
    {
        perror("Falha na abertura da memória partilhada");
        exit(EXIT_FAILURE);
    }
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
    // Inicializar quadro
    inicializar_quadro(quadro);

    // semáforos para cada coluna
    for (coluna = 0; coluna < NUM_COLUNAS; coluna++)
    {
        char nome_semaforo[20];
        sprintf(nome_semaforo, "%s%d", NOME_SEMAFORO_COL_QUADRO, coluna);
        //sem_unlink(nome_semaforo);
        semaforos[coluna] = sem_open(nome_semaforo, O_CREAT, S_IRUSR | S_IWUSR, 1);
        if (semaforos[coluna] == SEM_FAILED)
        {
            perror("Falha ao abrir o semáforo de colunas da board");
            exit(EXIT_FAILURE);
        }
    }

    // semáforo do leitor
    //sem_unlink(NOME_SEMAFORO_LEITURA);
    leitor_semaforo = sem_open(NOME_SEMAFORO_LEITURA, O_CREAT, S_IRUSR | S_IWUSR, 1);
    if (leitor_semaforo == SEM_FAILED)
    {
        perror("Falha ao abrir o semáforo de leitura");
        exit(EXIT_FAILURE);
    }


    int terminar = 0;
    printf("Introduza qualquer carater para terminar:\n");
    scanf("%d", &terminar);

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
