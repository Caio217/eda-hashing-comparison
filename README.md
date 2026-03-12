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

  ### Função de hash DJB2

## Análise de Desempenho e Metodologia de Benchmark

  O desempenho das estruturas foi medido com o JMH (Java Microbenchmark Harness), framework padrão da JVM, escolhido por neutralizar distorções causadas pela compilação JIT e pelo Garbage Collector.
  
A análise foi dividida em duas frentes: eficiência matemática (colisões e saltos, contados antes do benchmark) e custo computacional (tempo de execução das operações Put, Get e Remove, medido pelo JMH).

Para garantir a confiabilidade dos resultados, foram adotadas três estratégias: uso de Blackhole para evitar que a JVM descartasse operações por Dead Code Elimination; 2 iterações de aquecimento para estabilizar a execução; e coleta em 1 fork com 3 iterações oficiais, medindo o tempo médio em nanossegundos (Mode.AverageTime).
  
## Resultado do BenchMark

### Entrada int:

Analise Get_fatordecarga
*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/58a7e5f6-c46b-4848-a885-6e00d8ae7307" target="_blank">
  <img src="https://github.com/user-attachments/assets/58a7e5f6-c46b-4848-a885-6e00d8ae7307" alt="Get_Fatordecarga" width="100%" />
</a>  

Analise GetAll_tamanho
*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/94c35309-935c-4db8-9947-963e98311fb9" target="_blank">
  <img src="https://github.com/user-attachments/assets/94c35309-935c-4db8-9947-963e98311fb9" alt="Get_tamanho" width="100%" />
</a>

Analise PutAll_Fatordecarga

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/5f0b43ed-7e43-4b98-9724-9b554197701c" target="_blank">
  <img src="https://github.com/user-attachments/assets/5f0b43ed-7e43-4b98-9724-9b554197701c" alt="Put_Fatordecarga" width="100%" />
</a>

Analise PutAll_tamanho

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/5b081318-a542-4100-89d9-fc087529518e" target="_blank">
  <img src="https://github.com/user-attachments/assets/5b081318-a542-4100-89d9-fc087529518e" alt="Put_tamanho" width="100%" />
</a>

Analise RemoveAll_Fatordecarga

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/f84be536-c9a2-4655-b78f-ad0625fad9cf" target="_blank">
  <img src="https://github.com/user-attachments/assets/f84be536-c9a2-4655-b78f-ad0625fad9cf" alt="Remove_Fatordecarga" width="100%" />
</a>

Analise RemoveAll_tamanho

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/ebeb69e9-4fff-446c-85e2-ba61f9858eaf" target="_blank">
  <img src="https://github.com/user-attachments/assets/ebeb69e9-4fff-446c-85e2-ba61f9858eaf" alt="Remove_tamanho" width="100%" />
</a>

Analise colisoes getAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/0a089fa5-16fe-4294-8d08-82cfd8440be2" target="_blank">
  <img src="https://github.com/user-attachments/assets/0a089fa5-16fe-4294-8d08-82cfd8440be2" alt="Colisoes_Get" width="100%" />
</a>

Analise colisoes putAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/e3755ba6-d47c-43ee-87dc-3a4c1234dc08" target="_blank">
  <img src="https://github.com/user-attachments/assets/e3755ba6-d47c-43ee-87dc-3a4c1234dc08" alt="Colisoes_Put" width="100%" />
</a>

Analise colisoes removeAll

*⚠️ Clique na imagem para dar zoom e conseguir ler os detalhes.*
<a href="https://github.com/user-attachments/assets/751daa0b-ca14-48fa-b850-9f848c43a8df" target="_blank">
  <img src="https://github.com/user-attachments/assets/751daa0b-ca14-48fa-b850-9f848c43a8df" alt="Colisoes_Remove" width="100%" />
</a>

Ou uma analise Geral dos graficos de Int

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
