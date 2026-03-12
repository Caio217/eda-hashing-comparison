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
  
## Resultado do BenchMark

### Entrada int (Introdução):

Para fazer essa análise foram realizados experimentos com três quantidades de conjunto de dados: 10 mil, 100 mil,  e 1 milhão de elementos. Além disso, as entradas foram formadas por trez variações de tamanho: 6, 7 e 9 dígitos. Para cada cenário foram testados três fatores de carga da tabela hash: 0.5, 0.75 e 0.9.
Os conjuntos de dados foram formados por diferentes características de entrada, incluindo valores com distribuição uniforme e entradas com padrões específicos. Essa variação permite observar como cada método de hashing se comporta em cenários distintos, identificando possíveis vantagens ou limitações de cada abordagem.

A Partir dos resultados obtidos no experimento, foram gerados gráficos que mostram o desempenho de cada função hash. Abaixo estão as análises realizadas a partir dos gráficos, comparando o desempenho das funções de hash que trabalhamos para cada método:

### Gráficos + Análises (Execução):

Analise Get_fatordecarga
*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/58a7e5f6-c46b-4848-a885-6e00d8ae7307" target="_blank">
  <img src="https://github.com/user-attachments/assets/58a7e5f6-c46b-4848-a885-6e00d8ae7307" alt="Get_Fatordecarga" width="100%" />
</a>  

Com base no gráfico acima, os métodos de Divisão e Multiplicação apresentaram o melhor desempenho durante as buscas. Essas funções mantiveram os tempos médios de resposta mais baixos e estáveis, mesmo com o aumento crítico do volume de dados e do fator de carga.
A função Mid-Square apresentou desempenho significativamente inferior em todos os cenários analisados. O tempo médio por operação disparou conforme o conjunto de dados cresceu, atingindo a marca de 1 segundo nos cenários de maior estresse.
As funções DJB2 e Polinomial apresentaram desempenho intermediário. Embora consigam manter tempos de resposta controlados em tabelas menores ou com baixa ocupação, elas demonstram uma perda de eficiência acentuada quando o fator de carga atinge 0.9.  


Analise GetAll_tamanho
*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/94c35309-935c-4db8-9947-963e98311fb9" target="_blank">
  <img src="https://github.com/user-attachments/assets/94c35309-935c-4db8-9947-963e98311fb9" alt="Get_tamanho" width="100%" />
</a>


De acordo com o gráfico, os métodos de Divisão e Multiplicação apresentaram o melhor desempenho em relação ao tamanho da entrada (6, 7 e 9 dígitos). Essas funções mantiveram os tempos médios de resposta mais baixos e constantes, demonstrando uma excelente capacidade de lidar com variações na magnitude das chaves. 
A função Mid-Square apresentou desempenho significativamente inferior em todos os cenários de tamanho de entrada. O tempo médio por operação escalou de forma agressiva conforme o volume de dados aumentou, atingindo picos próximos a 1 segundo no cenário de 1M de elementos.
As funções DJB2 e Polinomial apresentaram desempenho intermediário. Embora consigam manter uma performance aceitável com entradas menores e volumes baixos, elas mostram uma sensibilidade maior ao aumento do tamanho das chaves e do conjunto de dados.


Analise PutAll_Fatordecarga

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/5f0b43ed-7e43-4b98-9724-9b554197701c" target="_blank">
  <img src="https://github.com/user-attachments/assets/5f0b43ed-7e43-4b98-9724-9b554197701c" alt="Put_Fatordecarga" width="100%" />
</a>

Neste gráfico o Fator de Carga(fc) revela um impacto significativo ao tempo de execuções das funções. Em fc = 0.5 todos os hashes mantêm um tempo de execução mesmo ao aumentar a quantidade das entradas, o MID-SQUARE em especial não explode se compararmos com os testes por tamanho de entrada. Em fc = 0.75 as funções continuam se comportando bem mesmo com o aumento do fator de carga. Já em fc = 0.9 a situação de todas piora, com o destaque do aumento significativo para DJB2 e POLINOMIAL que se aproximam de 1s para entradas de 1000000. 
DIVISÃO e MULTIPLICAÇÃO mantém o crescimento de suas barras de forma proporcional ao n, que é o comportamento ideal para uma tabela hash.

Analise PutAll_tamanho

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/5b081318-a542-4100-89d9-fc087529518e" target="_blank">
  <img src="https://github.com/user-attachments/assets/5b081318-a542-4100-89d9-fc087529518e" alt="Put_tamanho" width="100%" />
</a>

O gráfico mostra consistência para todos os tamanhos de entradas, com destaque para as funções de MULTIPLICAÇÃO e DIVISÃO, que mantém tempos em centenas de microssegundos mesmo para quantidade 1000000 de entradas, enquanto se destaca negativamente o MID-SQUARE, que em testes com 100k de entradas já explode chegando a centenas de milissegundos e também é perceptível que em 9 dígitos o mesmo piora relativamente mais que os outros. No cenário UNIFORME as funções DJB2 E POLINOMIAL sobem significativamente em 1000000. O cenário PADRÃO é mais rápido para quase todos os hashes, em exceção ao MID-SQUARE e ao DJB2 para entradas de 100k.

Analise RemoveAll_Fatordecarga

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/f84be536-c9a2-4655-b78f-ad0625fad9cf" target="_blank">
  <img src="https://github.com/user-attachments/assets/f84be536-c9a2-4655-b78f-ad0625fad9cf" alt="Remove_Fatordecarga" width="100%" />
</a>

Este gráfico mostra melhor a sensibilidade ao Fator de Carga(fc) das funções hashes. Em fc = 0.5 o cenário é mais tranquilo, até o MID-SQUARE fica na casa das centenas de milissegundos em 1000000, o que ainda é controlável. Em fc = 0.75 a comparação entre os hashes continua clara, com MID-SQUARE pior que os demais em 10k e 100k de entradas, já para 1000000 de entradas o POLINOMIAL explode, o que pode indicar um ponto crítico da função. Em fc = 0.9 todos estão piorando, MID-SQUARE e POLINOMIAL ultrapassam 1s em 1000000, e DJB2 tem cenário próximo a isso. O POLINOMIAL supera o MID-SQUARE em alguns casos de remoção UNIFORME para fc = 0.9 o que indica que para remoções com tabela muito cheia e distribuição aleatória, o POLINOMIAL tem comportamento ruim. DIVISÃO e MULTIPLICAÇÃO continuam com tempo consideravelmente pequeno , na casa dos milissegundos, confirmando ser escolhas seguras.

Analise RemoveAll_tamanho

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/ebeb69e9-4fff-446c-85e2-ba61f9858eaf" target="_blank">
  <img src="https://github.com/user-attachments/assets/ebeb69e9-4fff-446c-85e2-ba61f9858eaf" alt="Remove_tamanho" width="100%" />
</a>

A remoção demonstra um quadro mais dramático que a inserção. O MID-SQUARE em 1000000 atinge valores acima de 1s para todos tamanhos e o POLINOMIAL no cenário UNIFORME sobe agressivamente, ultrapassando 1s no tamanho de 6 dígitos e mantém-se próximo nos demais. A informação mais interessante é que o DJB2 no UNIFORME aparece visivelmente abaixo que no PADRÃO, sugerindo que o mesmo lida melhor com chaves aleatórias que com chaves padrões. A DIVISÃO e MULTIPLICAÇÃO continuam sendo mais estáveis, variando em tempo na casa dos milissegundos mesmo em 1000000.

### Gráficos + Análises (Colisões):
Analise colisoes getAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/0a089fa5-16fe-4294-8d08-82cfd8440be2" target="_blank">
  <img src="https://github.com/user-attachments/assets/0a089fa5-16fe-4294-8d08-82cfd8440be2" alt="Colisoes_Get" width="100%" />
</a>

Com base nos resultados apresentados no gráfico, é possivel concluir que as funções hash Divisão e Multiplicação apresentaram o melhor desempenho, mantendo baixos níveis de colisão mesmo com o aumento do fator de carga e do tamanho do conjunto de dados.
As funções DJB2 e Polinomial apresentaram desempenho intermediário, com crescimento gradual de colisões conforme a tabela se torna mais carregada.
Por outro lado, a função Mid-Square demonstrou desempenho significativamente inferior, apresentando um número muito elevado de colisões em todos os cenários analisados.


Analise colisoes putAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/e3755ba6-d47c-43ee-87dc-3a4c1234dc08" target="_blank">
  <img src="https://github.com/user-attachments/assets/e3755ba6-d47c-43ee-87dc-3a4c1234dc08" alt="Colisoes_Put" width="100%" />
</a>

Conforme mostra o gráfico, os métodos de Divisão e Multiplicação apresentaram o melhor desempenho nas inserções, os dois mantiveram um crescimento mais controlado no número de colisões mesmo com o aumento do fator de carga e do tamanho do conjunto de dados.
A função Mid-Square apresentou desempenho significativamente inferior em todos os cenários analisados. O número de colisões gerado por essa técnica foi consideravelmente maior quando comparado às demais funções.
As funções DJB2 e Polinomial apresentaram desempenho intermediário. Embora o número de colisões aumente conforme o fator de carga e o tamanho da tabela crescem, essas funções ainda conseguem manter um comportamento mais equilibrado do que o observado na função Mid-Square.

Analise colisoes removeAll

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

### Colisoes_10k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/f8138afb-a6cd-413d-b9cd-85a74d40c93d" target="_blank">
  <img src="https://github.com/user-attachments/assets/f8138afb-a6cd-413d-b9cd-85a74d40c93d" alt="Colisoes_10k" width="100%" />
</a>

### Colisoes_100k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/1deaf224-4108-460a-ad27-592d7579798d" target="_blank">
  <img src="https://github.com/user-attachments/assets/1deaf224-4108-460a-ad27-592d7579798d" alt="Colisoes_100k" width="100%" />
</a>

### Colisoes_1M:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/4bf7f9dc-2d99-44f6-a7d2-0357e41bd63b" target="_blank">
  <img src="https://github.com/user-attachments/assets/4bf7f9dc-2d99-44f6-a7d2-0357e41bd63b" alt="Colisoes_1M" width="100%" />
</a>

### Análise de Colisões (Gráficos de 10k, 100k e 1M)
  Ao observar o conjunto de gráficos de colisões, fica clara a diferença de lidar com esse tipo de dado entre os algoritmos para o tipo String:

  O Colapso das Funções Simples: As funções Divisão, Multiplicação e Mid-Square apresentam uma quantidade massiva de colisões (escala de milhões) desde os volumes iniciais. Isso ocorre porque elas não processam a posição dos caracteres, sofrendo severamente no cenário de Anagramas.

  A Estabilidade de DJB2 e Polinomial: Ambas mantiveram índices de colisão extremamente baixos e constantes. Como utilizam o processamento posicional (Método de Horner e constantes primas), elas garantem que strings diferentes quase nunca ocupem o mesmo índice, mantendo a Tabela Hash saudável mesmo com 1 milhão de elementos.

## Desempenho(Tempo de execução)
### Desempenho_10k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/0b41e3bd-e141-48f6-b456-da5d981b90a2" target="_blank">
  <img src="https://github.com/user-attachments/assets/0b41e3bd-e141-48f6-b456-da5d981b90a2" alt="Desempenho_10k" width="100%" />
</a>
### Desempenho_100k:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/a515f771-33f0-4b46-a50d-98f66bb2fc01" target="_blank">
  <img src="https://github.com/user-attachments/assets/a515f771-33f0-4b46-a50d-98f66bb2fc01" alt="Desempenho_100k" width="100%" />
</a>
### Desempenho_1M:

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/abdb79c2-0a63-4975-b7d1-05cb07423c33" target="_blank">
  <img src="https://github.com/user-attachments/assets/abdb79c2-0a63-4975-b7d1-05cb07423c33" alt="Desempenho_1M" width="100%" />
</a>

### Analise de Desempenho

  Esta seção detalha o comportamento das funções hash frente à Sondagem Linear. Como essa estratégia exige a busca de slots adjacentes em caso de colisão, a eficiência do sistema depende diretamente da qualidade da distribuição dos índices.

1. O Impacto do Agrupamento Primário

  Quando as funções Divisão, Multiplicação e Mid-Square geram agrupamentos (especialmente em cenários de Anagramas), ocorre o fenômeno de Agrupamento Primário (primary clustering).Isso preenche a tabela com blocos contíguos de dados, obrigando o algoritmo a percorrer um número crescente de posições a cada operação. Esse efeito cascata degrada a performance de $O(1)$ para $O(n)$, explicando o aumento drástico no tempo de execução observado nos gráficos.

2. Resiliência das Funções Especializadas (DJB2 e Polinomial)

  As funções DJB2 e Polinomial mantiveram desempenho estável ao distribuir as chaves de forma uniforme. Ao minimizar o agrupamento, elas permitem que a Sondagem Linear encontre um slot disponível rapidamente, mantendo os tempos de acesso próximos ao esperado para $O(1)$, independentemente do volume de dados.

3. Inviabilidade Técnica no Volume de 1 Milhão

  Nos testes com 1 milhão de elementos, as funções Divisão, Multiplicação e Mid-Square não tiveram seus dados coletados.Com o aumento da carga, o tempo necessário para encontrar um slot vazio em uma tabela com alto índice de agrupamento tornou-se proibitivo para o JMH.A interrupção desses testes foi uma decisão metodológica para garantir a viabilidade da automação, confirmando que tais funções são tecnicamente inadequadas para grandes volumes de dados do tipo String sob a estratégia de sondagem linear.
