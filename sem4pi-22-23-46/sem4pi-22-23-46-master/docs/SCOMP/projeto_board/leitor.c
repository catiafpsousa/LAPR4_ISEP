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

void imprimir_quadro(Board *quadro)
{
    int coluna, linha;

    for (linha = 0; linha < NUM_LINHAS; linha++)
    {
        for (coluna = 0; coluna < NUM_COLUNAS; coluna++)
        {
            printf("%d\t", quadro->quadro[linha][coluna]);
        }
        printf("\n");
    }
    printf("\n");
}

int main()
{
    int fd;
    Board *quadro;
    sem_t *leitor_semaforo;
    Board quadro_anterior;

    // memória partilhada
    fd = shm_open(NOME_MEMORIA_PARTILHADA, O_RDONLY, S_IRUSR | S_IWUSR);
    if (fd == -1)
    {
        perror("Falha na abertura da memória partilhada");
        exit(EXIT_FAILURE);
    }
    quadro = mmap(NULL, TAMANHO_MEMORIA_PARTILHADA, PROT_READ, MAP_SHARED, fd, 0);
    if (quadro == MAP_FAILED)
    {
        perror("Falha no mapeamento da memória partilhada");
        exit(EXIT_FAILURE);
    }

    // semáforo do leitor
    leitor_semaforo = sem_open(NOME_SEMAFORO_LEITURA, 0);
    if (leitor_semaforo == SEM_FAILED)
    {
        perror("Falha ao abrir o semáforo de leitura");
        exit(EXIT_FAILURE);
    }

    // Impressao do quadro sempre que algum processo fizer post ao leitor_semaforo
    while (1)
    {
        sem_wait(leitor_semaforo);
        imprimir_quadro(quadro);
    }

    // Libertar recursos
    if (sem_close(leitor_semaforo) == -1)
    {
        perror("Falha ao fechar o semáforo de leitura");
        exit(EXIT_FAILURE);
    }


    if (munmap(quadro, TAMANHO_MEMORIA_PARTILHADA) < 0)
    {
        perror("Falha no unmap da memória partilhada");
        exit(EXIT_FAILURE);
    }

    return 0;
}