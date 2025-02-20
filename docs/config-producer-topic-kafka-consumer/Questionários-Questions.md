### Português
##### 1- Se eu quiser ter uma confiança extremamente alta de que os líderes e réplicas têm os meus dados, devo usar
`Resposta`: acks=All, replication factor=3, min.insync.replicas=2

2. Para evitar duplicatas no Kafka introduzidas pela rede, devo usar.
**Resposta**: enable.idempotence=true

3. A compressão de mensagens por padrão está habilitada
**Resposta**: Não

4. Quando eu comprimo as minhas mensagens do lado do produtor.
**Resposta**: O consumidor tem que descomprimir elas.

5. Para melhorar a compressão, posso aumentar as hipóteses de agrupamento usando
**Resposta**: 20
A espera permite aguardar um pouco antes de enviar mensagens e aumenta as chances de agrupamento.

6. A regra "mesma chave vai para a mesma partição" é verdadeira, a menos que...
**Resposta**: O número de partições mude.


### English
1. If I want to have extremely high confidence that leaders and replicas have my data, I should use
**Answer**: acks=All, replication factor=3, min.insync.replicas=2

2. To prevent duplicates in Kafka introduced by the network, I should use
**Answer**: enable.idempotence=true

3. Message compression by default is enabled
**Answer**: No

4. When I compress my messages from the producer side
**Answer**: The consumer has to decompress them.

5. To enhance compression, I can increase the chances of batching by using
**Answer**: 20
Lingering allows waiting a bit of time before sending messages and increases chances of batching.

6. The rule "same key goes to the same partition" is true unless...
**Answer**: The number of partitions changes.

