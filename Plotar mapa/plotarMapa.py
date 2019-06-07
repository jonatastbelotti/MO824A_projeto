#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import matplotlib.pyplot as plt
import numpy as np


# Função que lê a rede de distribuição do arquivo
def lerArquivo(arquivo_):
    file = open(arquivo_, 'r')
    rede = {}
    lendo_posicao_nos = False
    lendo_arestas = False
    isChave = False

    for linha in file:
        linha = linha.replace("\r", "")
        linha = linha.replace("\n", "")
        linha = linha.replace("\t", " ")
        partes = linha.split(" ")

        if partes[0].startswith("."):
            lendo_posicao_nos = False
            lendo_arestas = False

        # Lendo número de nós da rede
        if partes[0] == ".Nodes":
            rede['num_nos'] = int(partes[1])
            rede['nos'] = []

            for i in range(rede['num_nos']):
                rede['nos'].append({})
        # Lendo número de arestas da rede
        elif partes[0] == ".Branch_count":
            rede['num_arestas'] = int(partes[1])
            rede['arestas'] = []

            for i in range(rede['num_arestas']):
                rede['arestas'].append({})
        # Lendo número de subestações
        elif partes[0] == '.Feeders':
            rede['numFontes'] = int(partes[1])
        # Lendo quais são as subestações
        elif partes[0] in ['.Feeder_node_ids', '.Feder_node_ids']:
            fontes = partes[1:]

            for i in range(len(rede['nos'])):
                no = rede['nos'][i]
                no['isFonte'] = False
                if str(i) in fontes:
                    no['isFonte'] = True
        # Lendo dimenções da rede
        elif partes[0] == '.Grid_size':
            rede['largura'] = int(partes[1])
            rede['altura'] = int(partes[2])
            lendo_posicao_nos = True
        # Lendo a posição de cada nó
        elif lendo_posicao_nos:
            no = rede['nos'][int(partes[0])]
            no['nome'] = partes[0]
            no['x'] = int(partes[1])
            no['y'] = int(partes[2])
        # Lendo detalhes de cada nó
        elif partes[0] == '.Branch':
            lendo_arestas = True
            isChave = False
        elif partes[0] == '.Tie':
            lendo_arestas = True
            isChave = True
        elif lendo_arestas:
            aresta = rede['arestas'][int(partes[0])]
            origem = int(partes[1])
            destino = int(partes[2])

            aresta['origem'] = origem
            aresta['destino'] = destino
            aresta['isChave'] = isChave

    return rede


# Função que plota a rede de distribuição
def plotarRede(rede_):
    # Plotando arestas
    for i in range(len(rede_['arestas'])):
        aresta = rede_['arestas'][i]
        no_origem = rede_['nos'][aresta['origem']]
        no_destino = rede_['nos'][aresta['destino']]

        cor = 'k-'
        if aresta['isChave']:
            cor = 'k--'

        plt.plot([no_origem['x'], no_destino['x']], [no_origem['y'], no_destino['y']], cor)
    
    # Plotando vertices
    for i in range(len(rede_['nos'])):
        no = rede_['nos'][i]

        cor = 'black'
        if no['isFonte']:
            cor = 'red'
        plt.plot(no['x'], no['y'], marker='o', color=cor)
    
    plt.xticks(np.arange(0, rede_['largura'] + 1, step=1))
    plt.yticks(np.arange(0, rede_['altura'] + 1, step=1))
    plt.show()



# CÓDIGO PRINCIPAL
def main(arquivoEntrada):
    # lendo rede do arquivo arquivo
    rede_distribuicao = lerArquivo(arquivoEntrada)

    # Plotando rede
    plotarRede(rede_distribuicao)
    




if __name__ == '__main__':

    # Verificando parâmetros de execução
    if len(sys.argv) != 2:
        print("Imforme o arquivo .pos com os dados da rede de distribuição")
        sys.exit(0)
    
    # Verificando se o formato do arquivo está correto
    if not sys.argv[1].upper().endswith(".POS"):
        print("O arquivo de entrada deve ser no formato .pos")
        sys.exit(0)
    
    main(sys.argv[1])