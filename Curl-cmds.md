### Comandos Básicos

# Realizar uma requisição GET
curl -X GET "http://example.com"

# Realizar uma requisição POST com dados
curl -X POST "http://example.com" -d "param1=value1&param2=value2"

# Realizar uma requisição PUT com dados
curl -X PUT "http://example.com/resource/1" -d "param1=value1&param2=value2"

# Realizar uma requisição DELETE
curl -X DELETE "http://example.com/resource/1"

### Cabeçalhos e Autenticação

# Adicionar cabeçalhos à requisição
curl -H "Content-Type: application/json" -H "Authorization: Bearer token" "http://example.com"

# Autenticação básica
curl -u username:password "http://example.com"

# Autenticação com token
curl -H "Authorization: Bearer token" "http://example.com"


### Envio de Dados
# Enviar dados em formato JSON
curl -X POST "http://example.com" -H "Content-Type: application/json" -d '{"key1":"value1", "key2":"value2"}'

# Enviar dados de um arquivo
curl -X POST "http://example.com" -H "Content-Type: application/json" -d @data.json

### Download de Arquivos
# Baixar um arquivo
curl -O "http://example.com/file.zip"

# Baixar um arquivo e salvar com um nome específico
curl -o newfile.zip "http://example.com/file.zip"


### Redirecionamentos e Verbosidade
# Seguir redirecionamentos
curl -L "http://example.com"

# Mostrar detalhes da requisição e resposta
curl -v "http://example.com"

### Manipulação de Cookies
# Salvar cookies em um arquivo
curl -c cookies.txt "http://example.com"

# Enviar cookies de um arquivo
curl -b cookies.txt "http://example.com"

### Upload de Arquivos
# Upload de um arquivo
curl -X POST "http://example.com/upload" -F "file=@/path/to/file"

### Proxy
# Usar um proxy
curl -x http://proxyserver:port "http://example.com"

### SSL/TLS
# Ignorar erros de certificado SSL
curl -k "https://example.com"

# Especificar um certificado cliente
curl --cert /path/to/cert.pem "https://example.com"

### Outras Opções
# Definir um tempo limite para a requisição
curl --max-time 5 "http://example.com"

# Definir um cabeçalho personalizado
curl -H "X-My-Header: value" "http://example.com"

# Definir um usuário e senha para autenticação básica 
curl -u username:password "http://example.com"

# Definir um cabeçalho de referência 
curl -e "http://referrer.com" "http://example.com"

# Definir um cabeçalho de agente do usuário 
curl -A "Mozilla/5.0" "http://example.com"

# Definir um cabeçalho de origem 
curl -H "Origin: example.com" "http://example.com"

# Definir um cabeçalho de tipo de conteúdo
curl -H "Content-Type: application/json" "http://example.com"

# Limitar a taxa de transferência
curl --limit-rate 100K "http://example.com"

# Especificar um tempo limite
curl --max-time 30 "http://example.com"

### Referências
# Documentação oficial do cURL: https://curl.se/docs/
# Tutorial do cURL: https://curl.se/docs/httpscripting.html
# Exemplos de uso do cURL: https://www.baeldung.com/curl-rest

### Elasticsearch cURL Commands

### Listeners and Advertised Listeners Configuration
- `localhost:9200`
- `127.0.0.1:9200`
- `[::1]:9200`
- `curl -X GET http://[::1]:5601` elasticsearch

### cURL Commands
1. Create a new index
```bash
curl -X PUT "localhost:9200/customer?pretty"
```
2. Index a document
```bash
curl -X PUT "localhost:9200/customer/_doc/1?pretty" -H 'Content-Type: application/json' -d'
{
  "name": "John Doe"
}
'
```
3. Get a document
```bash
curl -X GET "localhost:9200/customer/_doc/1?pretty"
```
4. Delete an index
```bash
curl -X DELETE "localhost:9200/customer?pretty"
```
### MORE CURLS COMMANDS
1. Retrieve all indices
```bash
curl -X GET "localhost:9200/_cat/indices?v"
```
2. Retrieve all documents
```bash
curl -X GET "localhost:9200/_search?pretty"
```
3. Retrieve all documents in a specific index
```bash
curl -X GET "localhost:9200/customer/_search?pretty"
```
4. Retrieve a specific document
```bash
curl -X GET "localhost:9200/customer/_doc/1?pretty"
```
5. Delete a specific document
```bash
curl -X DELETE "localhost:9200/customer/_doc/1?pretty"
```
6. Update a specific document
```bash
curl -X POST "localhost:9200/customer/_update/1?pretty" -H 'Content-Type: application/json' -d'
{
  "doc": {
    "name": "Jane Doe"
  }
}
'
```
7. Search for a document
```bash
curl -X GET "localhost:9200/customer/_search?q=name:Doe&pretty"
```
8. Search for a document using a query string
```bash
curl -X GET "localhost:9200/customer/_search?q=name:Doe&sort=name:desc&pretty"
```
9. Search for a document using a query DSL
```bash
curl -X GET "localhost:9200/customer/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "name": "Doe"
    }
  }
}
'
```
10. Search for a document using a query DSL with pagination
```bash
curl -X GET "localhost:9200/customer/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "name": "Doe"
    }
  },
  "from": 0,
  "size": 1
}
'
```
11. Search for a document using a query DSL with sorting
```bash
curl -X GET "localhost:9200/customer/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "name": "Doe"
    }
  },
  "sort": [
    {
      "name": {
        "order": "desc"
      }
    }
  ]
}
'
```
12. Search for a document using a query DSL with filtering
```bash
curl -X GET "localhost:9200/customer/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "must": {
        "match": {
          "name": "Doe"
        }
      },
      "filter": {
        "range": {
          "age": {
            "gte": 18
          }
        }
      }
    }
  }
}
'
```
13. Search for a document using a query DSL with aggregations
```bash
curl -X GET "localhost:9200/customer/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "size": 0,
  "aggs": {
    "group_by_name": {
      "terms": {
        "field": "name.keyword"
      }
    }
  }
}

```
