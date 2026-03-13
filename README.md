# eda-hashing-comparison

## Introdução
  O projeto tem como intuito fazer a análise de diferentes funções de hash e seus comportamentos dentro de uma tabela hash, utilizando como onjeto da análise os métodos básicos da tabela e como eles se comportam de acordo com a utilização dessas diferentes funções.

## A tabela
  A tabela foi feita para receber tipos genéricos de chave e objeto, facilitando assim o estudo do comportamento das funções de hash com entradas como int e String.
  
  Os métodos utilizam padrões similares aos utilizados na discilplina, sendo modificados para receberem os tipos genéricos, além de poucas modificações pontuais.

  A tabela é inicializada de forma padrão com fator de carga 0.75 e capacidade 11, que é um número primo e sempre que é necessário um aumento no tamanho da tabela, esse se dá com um novo número primo (o próximo número primo maior que duas vezes o tamanho antigo da tabela), a utilização desses padrões (fator de carga e capacidade) são estratérgicos para minimizar colisões, já que com a utilizaçao de números primos a distribuiçao das chaves se dá de forma bem mais uniforme, além do fator de carga 0.75 que não deixa a tabela fazer os cálculos de hash com muitas posições da tabela já ocupadas. A soma dessas estratérgias faz com que o número de colisões na tabela seja em grande escala diminuido. 

## As funções de hash
  Foi criada uma interface "FuncaoHash" que foi implementada pelas funções específicas, facilitando para que se possa escolher a função a ser utilizada dentro da tabela.

  ### Função de hash com divisão
  Uma das funções mais básicas de hash, nada mais faz do que dividir o valor     da chave pela capacidade da tabela retornando como hash o resto dessa divisão. 
  A função sofre as adaptações necessárias para o caso de chave String, para     retornar o valor de hash em inteiro (a conversão é realizada somando os          valores ASCII de cada caractere da String). 

  ### Função de hash com multiplicação
  Função hash que utiliza uma constante fracionária para embaralhar a chave e melhor distribuição dos hashes, funcionando bem para qualquer capacidade da tabela. A constante recomendada é a razão áurea(A ≈ 0.6180339887), sugerida por Knuth por produzir distribuição uniforme.

  ### Função de hash polinomial
  A Função Polinomial foi utilizada para converter Strings em índices de forma eficiente. Em vez de uma soma simples, ela trata a chave como um polinômio onde cada letra tem um peso baseado em sua posição.
  
  Como funciona: A implementação utiliza o Método de Horner, que processa a palavra caractere por caractere. A cada iteração, o valor acumulado é multiplicado por uma base prima (ex: 31) e somado ao valor da próxima letra. Isso permite que o cálculo seja feito em tempo linear $O(n)$ sem operações pesadas de potência.

  ### Função de hash MidSquare
  Função hash que utiliza o método do quadrado do meio, esse método gera o valor hash a partir do quadrado da chave de entrada. A tecnica consiste em elevar a chave ao quadrado, e em seguida extrair os digitos centrais desse valor para formar o indice da tabela hash.

  ### Função de hash DJB2

## Análise de Desempenho e Metodologia de Benchmark

  O desempenho das estruturas foi medido com o JMH (Java Microbenchmark Harness), framework padrão da JVM, escolhido por neutralizar distorções causadas pela compilação JIT e pelo Garbage Collector.
  
A análise foi dividida em duas frentes: eficiência matemática (colisões e saltos, contados antes do benchmark) e custo computacional (tempo de execução das operações Put, Get e Remove, medido pelo JMH).

Para garantir a confiabilidade dos resultados, foram adotadas três estratégias: uso de Blackhole para evitar que a JVM descartasse operações por Dead Code Elimination; 2 iterações de aquecimento para estabilizar a execução; e coleta em 1 fork com 3 iterações oficiais, medindo o tempo médio em nanossegundos (Mode.AverageTime).

### Para executar o experimento:

 O projeto utiliza um arquivo de configurações chamado config.properties, localizado na raiz do repositório. Nele, você pode alterar os parâmetros do benchmark de forma centralizada, sem precisar modificar o código-fonte. O próprio arquivo contém instruções detalhadas sobre o que cada variável faz.

Passo a Passo para Execução:

#### 1. Configuração Inicial:
  Edite o arquivo config.properties para definir os cenários e volumes de dados desejados.

#### 2. Geração da Base de Dados:
  Para garantir a integridade do experimento (reprodutibilidade), a geração de dados foi separada da execução. Para gerar ou renovar os arquivos de entrada, execute:

Bash
```
./gerarDados.sh
```

#### 3. Execução do Benchmark:

  Com os dados gerados, inicie os testes automatizados rodando:

  ```
./runBenchMark.sh
  ```


## Resultado do BenchMark

### Entrada int (Introdução):

Para fazer essa análise foram realizados experimentos com três quantidades de conjunto de dados: 10 mil, 100 mil,  e 1 milhão de elementos. Além disso, as entradas foram formadas por trez variações de tamanho: 6, 7 e 9 dígitos. Para cada cenário foram testados três fatores de carga da tabela hash: 0.5, 0.75 e 0.9.
Os conjuntos de dados foram formados por diferentes características de entrada, incluindo valores com distribuição uniforme e entradas com padrões específicos. Essa variação permite observar como cada método de hashing se comporta em cenários distintos, identificando possíveis vantagens ou limitações de cada abordagem.

A Partir dos resultados obtidos no experimento, foram gerados gráficos que mostram o desempenho de cada função hash. Abaixo estão as análises realizadas a partir dos gráficos, comparando o desempenho das funções de hash que trabalhamos para cada método:

### Gráficos + Análises (Execução):

#### Gráfico GetAll fatordecarga
*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/58a7e5f6-c46b-4848-a885-6e00d8ae7307" target="_blank">
  <img src="https://github.com/user-attachments/assets/58a7e5f6-c46b-4848-a885-6e00d8ae7307" alt="Get_Fatordecarga" width="100%" />
</a>  

Com base no gráfico acima, os métodos de Divisão e Multiplicação apresentaram o melhor desempenho durante as buscas. Essas funções mantiveram os tempos médios de resposta mais baixos e estáveis, mesmo com o aumento crítico do volume de dados e do fator de carga.
A função Mid-Square apresentou desempenho significativamente inferior em todos os cenários analisados. O tempo médio por operação disparou conforme o conjunto de dados cresceu, atingindo a marca de 1 segundo nos cenários de maior estresse.
As funções DJB2 e Polinomial apresentaram desempenho intermediário. Embora consigam manter tempos de resposta controlados em tabelas menores ou com baixa ocupação, elas demonstram uma perda de eficiência acentuada quando o fator de carga atinge 0.9.  


#### Gráfico GetAll tamanho
*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/94c35309-935c-4db8-9947-963e98311fb9" target="_blank">
  <img src="https://github.com/user-attachments/assets/94c35309-935c-4db8-9947-963e98311fb9" alt="Get_tamanho" width="100%" />
</a>


De acordo com o gráfico, os métodos de Divisão e Multiplicação apresentaram o melhor desempenho em relação ao tamanho da entrada (6, 7 e 9 dígitos). Essas funções mantiveram os tempos médios de resposta mais baixos e constantes, demonstrando uma excelente capacidade de lidar com variações na magnitude das chaves. 
A função Mid-Square apresentou desempenho significativamente inferior em todos os cenários de tamanho de entrada. O tempo médio por operação escalou de forma agressiva conforme o volume de dados aumentou, atingindo picos próximos a 1 segundo no cenário de 1M de elementos.
As funções DJB2 e Polinomial apresentaram desempenho intermediário. Embora consigam manter uma performance aceitável com entradas menores e volumes baixos, elas mostram uma sensibilidade maior ao aumento do tamanho das chaves e do conjunto de dados.

#### Gráfico PutAll Fatordecarga

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/5f0b43ed-7e43-4b98-9724-9b554197701c" target="_blank">
  <img src="https://github.com/user-attachments/assets/5f0b43ed-7e43-4b98-9724-9b554197701c" alt="Put_Fatordecarga" width="100%" />
</a>

Neste gráfico o Fator de Carga(fc) revela um impacto significativo ao tempo de execuções das funções. Em fc = 0.5 todos os hashes mantêm um tempo de execução mesmo ao aumentar a quantidade das entradas, o MID-SQUARE em especial não explode se compararmos com os testes por tamanho de entrada. Em fc = 0.75 as funções continuam se comportando bem mesmo com o aumento do fator de carga. Já em fc = 0.9 a situação de todas piora, com o destaque do aumento significativo para DJB2 e POLINOMIAL que se aproximam de 1s para entradas de 1000000. 
DIVISÃO e MULTIPLICAÇÃO mantém o crescimento de suas barras de forma proporcional ao n, que é o comportamento ideal para uma tabela hash.

#### Gráfico PutAll tamanho

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/5b081318-a542-4100-89d9-fc087529518e" target="_blank">
  <img src="https://github.com/user-attachments/assets/5b081318-a542-4100-89d9-fc087529518e" alt="Put_tamanho" width="100%" />
</a>

O gráfico mostra consistência para todos os tamanhos de entradas, com destaque para as funções de MULTIPLICAÇÃO e DIVISÃO, que mantém tempos em centenas de microssegundos mesmo para quantidade 1000000 de entradas, enquanto se destaca negativamente o MID-SQUARE, que em testes com 100k de entradas já explode chegando a centenas de milissegundos e também é perceptível que em 9 dígitos o mesmo piora relativamente mais que os outros. No cenário UNIFORME as funções DJB2 E POLINOMIAL sobem significativamente em 1000000. O cenário PADRÃO é mais rápido para quase todos os hashes, em exceção ao MID-SQUARE e ao DJB2 para entradas de 100k.

#### Gráfico RemoveAll Fatordecarga

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/f84be536-c9a2-4655-b78f-ad0625fad9cf" target="_blank">
  <img src="https://github.com/user-attachments/assets/f84be536-c9a2-4655-b78f-ad0625fad9cf" alt="Remove_Fatordecarga" width="100%" />
</a>

Este gráfico mostra melhor a sensibilidade ao Fator de Carga(fc) das funções hashes. Em fc = 0.5 o cenário é mais tranquilo, até o MID-SQUARE fica na casa das centenas de milissegundos em 1000000, o que ainda é controlável. Em fc = 0.75 a comparação entre os hashes continua clara, com MID-SQUARE pior que os demais em 10k e 100k de entradas, já para 1000000 de entradas o POLINOMIAL explode, o que pode indicar um ponto crítico da função. Em fc = 0.9 todos estão piorando, MID-SQUARE e POLINOMIAL ultrapassam 1s em 1000000, e DJB2 tem cenário próximo a isso. O POLINOMIAL supera o MID-SQUARE em alguns casos de remoção UNIFORME para fc = 0.9 o que indica que para remoções com tabela muito cheia e distribuição aleatória, o POLINOMIAL tem comportamento ruim. DIVISÃO e MULTIPLICAÇÃO continuam com tempo consideravelmente pequeno , na casa dos milissegundos, confirmando ser escolhas seguras.

#### Gráfico RemoveAll tamanho

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/ebeb69e9-4fff-446c-85e2-ba61f9858eaf" target="_blank">
  <img src="https://github.com/user-attachments/assets/ebeb69e9-4fff-446c-85e2-ba61f9858eaf" alt="Remove_tamanho" width="100%" />
</a>

A remoção demonstra um quadro mais dramático que a inserção. O MID-SQUARE em 1000000 atinge valores acima de 1s para todos tamanhos e o POLINOMIAL no cenário UNIFORME sobe agressivamente, ultrapassando 1s no tamanho de 6 dígitos e mantém-se próximo nos demais. A informação mais interessante é que o DJB2 no UNIFORME aparece visivelmente abaixo que no PADRÃO, sugerindo que o mesmo lida melhor com chaves aleatórias que com chaves padrões. A DIVISÃO e MULTIPLICAÇÃO continuam sendo mais estáveis, variando em tempo na casa dos milissegundos mesmo em 1000000.

### Gráficos + Análises (Colisões):
#### Gráfico colisoes getAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/0a089fa5-16fe-4294-8d08-82cfd8440be2" target="_blank">
  <img src="https://github.com/user-attachments/assets/0a089fa5-16fe-4294-8d08-82cfd8440be2" alt="Colisoes_Get" width="100%" />
</a>

Com base nos resultados apresentados no gráfico, é possivel concluir que as funções hash Divisão e Multiplicação apresentaram o melhor desempenho, mantendo baixos níveis de colisão mesmo com o aumento do fator de carga e do tamanho do conjunto de dados.
As funções DJB2 e Polinomial apresentaram desempenho intermediário, com crescimento gradual de colisões conforme a tabela se torna mais carregada.
Por outro lado, a função Mid-Square demonstrou desempenho significativamente inferior, apresentando um número muito elevado de colisões em todos os cenários analisados.


#### Gráfico colisoes putAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/e3755ba6-d47c-43ee-87dc-3a4c1234dc08" target="_blank">
  <img src="https://github.com/user-attachments/assets/e3755ba6-d47c-43ee-87dc-3a4c1234dc08" alt="Colisoes_Put" width="100%" />
</a>

Conforme mostra o gráfico, os métodos de Divisão e Multiplicação apresentaram o melhor desempenho nas inserções, os dois mantiveram um crescimento mais controlado no número de colisões mesmo com o aumento do fator de carga e do tamanho do conjunto de dados.
A função Mid-Square apresentou desempenho significativamente inferior em todos os cenários analisados. O número de colisões gerado por essa técnica foi consideravelmente maior quando comparado às demais funções.
As funções DJB2 e Polinomial apresentaram desempenho intermediário. Embora o número de colisões aumente conforme o fator de carga e o tamanho da tabela crescem, essas funções ainda conseguem manter um comportamento mais equilibrado do que o observado na função Mid-Square.

#### Gráfico colisoes removeAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/751daa0b-ca14-48fa-b850-9f848c43a8df" target="_blank">
  <img src="https://github.com/user-attachments/assets/751daa0b-ca14-48fa-b850-9f848c43a8df" alt="Colisoes_Remove" width="100%" />
</a>

Os resultados no gráfico mostram que os métodos de Divisão e Multiplicação apresentaram o melhor desempenho nas remoções. Essas funções mantiveram o menor índice de colisões e um crescimento controlado, mesmo nos cenários mais críticos de 1M de elementos e fator de carga de 0.9. 
A função Mid-Square apresentou um desempenho catastrófico e significativamente inferior em todos os cenários de remoção.
As funções DJB2 e Polinomial apresentaram desempenho intermediário. Embora consigam manter um comportamento muito mais equilibrado do que a Mid-Square, elas demonstram uma sensibilidade maior ao aumento do fator de carga e ao volume de dados, onde o número de colisões começa a se distanciar das funções de  Divisão e Multiplicação.

### Introdução aos Cenários com Strings

  Diferente de chaves numéricas, o tratamento de textos (Strings) exige algoritmos que considerem não apenas os caracteres, mas também sua ordem. Para este experimento, os testes foram escalados em volumes de 10 mil, 100 mil e 1 milhão de elementos.

### Nota sobre a Metodologia: 

  Visando viabilizar a coleta de dados e o tempo total de execução do benchmark, optou-se por fixar o tamanho das chaves em 10 caracteres. Essa redução na variedade de parâmetros foi necessária devido à alta latência observada nas funções mais simples; sem essa limitação, o tempo de execução para volumes maiores tornaria a automação do experimento inviável. Os testes foram aplicados sob dois cenários: Uniforme e Anagramas, permitindo observar como cada função lida com a entropia do texto conforme o volume de dados cresce.

## Colisões

#### Colisoes_10k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/b8225304-b196-4067-b17e-13d87e3b12e4" target="_blank">
  <img src="https://github.com/user-attachments/assets/b8225304-b196-4067-b17e-13d87e3b12e4" alt="Colisoes_10k" width="100%" />
</a>

#### Análise 10k:
  
  Neste volume inicial, o gráfico (em escala logarítmica) já escancara a ineficiência das funções mais simples. As funções Divisão, Multiplicação e Mid-Square apresentam colisões na casa dos milhões, com um salto drástico no cenário de Anagramas (barras listradas). Enquanto isso, DJB2 e Polinomial mantêm as colisões na casa dos milhares, mostrando que o espalhamento posicional evita o agrupamento precoce.

#### Colisoes_100k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/1deaf224-4108-460a-ad27-592d7579798d" target="_blank">
  <img src="https://github.com/user-attachments/assets/1deaf224-4108-460a-ad27-592d7579798d" alt="Colisoes_100k" width="100%" />
</a>

#### Análise 100k:

  Com o aumento de $n$, a situação das funções simples se torna insustentável. O volume de colisões para Divisão, Multiplicação e Mid-Square ultrapassa a marca de centenas de milhões. Aqui, a Sondagem Linear começa a enfrentar gargalos severos, pois o alto índice de ocupação contígua na tabela gera cadeias de busca imensas para as chaves mal distribuídas. DJB2 e Polinomial permanecem estáveis e quase invisíveis perto das outras no gráfico.

#### Colisoes_1M:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/4bf7f9dc-2d99-44f6-a7d2-0357e41bd63b" target="_blank">
  <img src="https://github.com/user-attachments/assets/4bf7f9dc-2d99-44f6-a7d2-0357e41bd63b" alt="Colisoes_1M" width="100%" />
</a>

#### Análise 1M:

  O detalhe mais importante deste gráfico é quem não está nele. As funções Divisão, Multiplicação e Mid-Square foram omitidas, pois seu nível de colisão tornou a execução inviável. Sobraram apenas DJB2 e Polinomial, que demonstram uma escalabilidade real: para 1 milhão de elementos, as colisões totais flutuam entre 1.2M e 1.8M (aprox. 1 a 2 colisões por operação), um índice excelente que mantém a estrutura de dados saudável e rápida.

## Desempenho(Tempo de execução)
#### Desempenho_10k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/f50bca23-d21d-4ee6-84e9-62f95f203f0c" target="_blank">
  <img src="https://github.com/user-attachments/assets/f50bca23-d21d-4ee6-84e9-62f95f203f0c" alt="Desempenho_10k" width="100%" />
</a>

#### Análise de Desempenho 10k:

  Mesmo com um volume inicial considerado pequeno, a diferença de tempo de execução já é gritante. Enquanto DJB2 e Polinomial resolvem as operações na casa dos microssegundos ($\approx 1000\mu s$), as funções Divisão e Mid-Square já operam na escala de segundos. Um destaque importante é a função Multiplicação, que apresenta um tempo razoável no cenário Uniforme, mas seu desempenho colapsa (saltando para $\approx 1.0s$) quando submetida aos Anagramas (barras listradas). Isso comprova que o custo de sondar posições adjacentes na memória devido a colisões é muito maior do que o custo computacional de calcular um Hash posicional mais complexo.

#### Desempenho_100k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/8535e2ea-e58d-4588-9a16-2fa656bc2ffa" target="_blank">
  <img src="https://github.com/user-attachments/assets/8535e2ea-e58d-4588-9a16-2fa656bc2ffa" alt="Desempenho_100k" width="100%" />
</a>

#### Análise de Desempenho 100k:

  Neste cenário, a degradação de performance das funções não-posicionais se torna extrema. Os tempos médios das funções Divisão, Multiplicação e Mid-Square disparam, chegando a registrar até 100 segundos por operação em alguns casos. Essa latência absurda confirma a transição da complexidade esperada de $O(1)$ para um degradante $O(n)$. O agrupamento primário (primary clustering) na tabela gerou listas contíguas tão longas que o algoritmo passa a maior parte do tempo apenas procurando um slot livre. DJB2 e Polinomial continuam inabaláveis, na parte mais baixa do gráfico.

#### Desempenho_1M:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/abdb79c2-0a63-4975-b7d1-05cb07423c33" target="_blank">
  <img src="https://github.com/user-attachments/assets/abdb79c2-0a63-4975-b7d1-05cb07423c33" alt="Desempenho_1M" width="100%" />
</a>

#### Análise de Desempenho 1M:

  O gráfico de 1 milhão ilustra a inviabilidade técnica das funções Divisão, Multiplicação e Mid-Square, que precisaram ser retiradas do benchmark pois o tempo para conclusão do teste seria impraticável. O gráfico então foca na comparação direta entre DJB2 e Polinomial. Ambas demonstram resiliência e escalabilidade, conseguindo processar o volume máximo mantendo a estabilidade. Curiosamente, a Polinomial chega a apresentar uma leve vantagem sobre si mesma no cenário de Anagramas nas operações de inserção (Put) e busca (Get), mas de forma geral, ambas provam ser as únicas escolhas viáveis para indexar grandes volumes de texto sob a estratégia de endereçamento aberto.

## Conclusão
  Este estudo comparou cinco funções de hash — Divisão, Multiplicação, Mid-Square, DJB2 e Polinomial — avaliando seu comportamento em operações de inserção, busca e remoção em uma tabela hash com endereçamento aberto, sob diferentes volumes de dados, fatores de carga e tipos de entrada (inteiros e Strings).
  Para entradas inteiras, as funções de Divisão e Multiplicação se destacaram positivamente em todos os cenários analisados, mantendo tempos de execução na casa dos milissegundos e crescimento proporcional ao volume de dados mesmo com fator de carga elevado (0.9), o que representa o comportamento ideal de uma tabela hash, também vale destacar que a função de Multiplicação tere zero colições para 10k e 1M, com fc = 0,5 tanto no PutAll como no GetAll. A função Mid-Square apresentou o pior desempenho dentre as testadas, com tempos chegando à casa de segundos nos cenários de maior estresse, evidenciando sua inadequação para uso em larga escala. As funções DJB2 e Polinomial demonstraram desempenho intermediário, lidando bem com volumes menores, mas sofrendo degradação significativa em fatores de carga mais altos, especialmente nas operações de remoção.
  O cenário com entradas do tipo String revelou uma inversão relevante nos resultados: as funções que melhor desempenharam com inteiros (Divisão, Mid-Square , Multiplicação) tornaram-se inviáveis ao lidar com texto, gerando colisões na casa dos milhões já em volumes de 10 mil elementos, e sendo completamente descartadas nos testes de 1 milhão por inviabilidade de execução. Esse comportamento decorre da incapacidade dessas funções de distinguir strings com os mesmos caracteres em ordens diferentes (anagramas), causando agrupamento primário severo e degradando a complexidade das operações de O(1) para O(n). Em contraste, DJB2 e Polinomial, por considerarem a posição de cada caractere no cálculo do hash, mantiveram distribuição eficiente e escalabilidade real, processando 1 milhão de elementos com aproximadamente 1 a 2 colisões por operação.
  
Esses resultados evidenciam que não existe uma função de hash universalmente superior: a escolha mais adequada depende diretamente do tipo de dado que será indexado. Para chaves numéricas com distribuição uniforme, funções simples como Divisão e Multiplicação oferecem excelente desempenho com baixo custo computacional. Já para chaves textuais, funções posicionais como DJB2 e Polinomial são as únicas escolhas viáveis, sendo capazes de manter a eficiência da estrutura independentemente do volume de dados ou da similaridade entre as chaves.

### Link para a proposta inicial do projeto

https://docs.google.com/document/d/1wRATvmS8OLfc9ZOSKYeruE9zE7GXBX_YqrYl-edYfjw/edit?usp=sharing
