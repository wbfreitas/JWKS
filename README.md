# 🔐 JWKS Server - Spring Boot + Kotlin

Este projeto é um servidor JWKS (JSON Web Key Set) criado com Spring Boot 3.5.0 e Kotlin. Ele é responsável por:

- ✅ Gerar tokens JWT assinados com chave privada
- ✅ Expor chaves públicas via endpoint `.well-known/jwks.json` para validação por terceiros
- ✅ Permitir rotação de chaves com suporte a múltiplos `kid`
- ✅ Seguir boas práticas de autenticação com JWT RS256

---

## 📌 O que é JWKS?

JWKS (JSON Web Key Set) é um formato padrão para publicar **chaves públicas** utilizadas na **verificação de tokens JWT** assinados com criptografia assimétrica (ex: RS256).

Seu endpoint `/jwks.json` é exposto para que clientes/parceiros possam obter a chave correta (via `kid`) e validar tokens sem depender da chave privada.

---

## 📦 Endpoints disponíveis

### 🔹 Gerar um token

```http
POST /token/generate
Content-Type: application/json
```

#### Corpo da requisição:

```json
{
  "idTransacional": "abc123",
  "codigoProduto": ["PROD1", "PROD2"]
}
```

#### Resposta:

```text
<token JWT assinado>
```

---

### 🔹 Endpoint JWKS (chaves públicas)

```http
GET /.well-known/jwks.json
```

#### Exemplo de resposta:

```json
{
  "keys": [
    {
      "kty": "RSA",
      "use": "sig",
      "alg": "RS256",
      "kid": "kid1",
      "n": "...",
      "e": "AQAB"
    }
  ]
}
```

---

## 🔑 O que é o `kid`?

- `kid` (Key ID) é um identificador único da chave usada para assinar o JWT.
- Ele aparece no **header** do token:

```json
{
  "alg": "RS256",
  "kid": "kid1"
}
```

- O parceiro usa esse `kid` para buscar a chave correta no JWKS e validar a assinatura.

---

## 🚀 Como validar um token JWT gerado por este servidor

1. Faça o `GET /.well-known/jwks.json` para obter as chaves públicas.
2. Extraia o `kid` do header do JWT.
3. Localize a chave com esse `kid` no JWKS.
4. Use a chave pública (`n` + `e`) para validar a assinatura do token usando a lib de sua linguagem (ex: `jsonwebtoken` no Node.js, `pyjwt` no Python, `Nimbus JOSE` no Java/Kotlin).

---

## 🛠️ Configuração via `application.yml`

```yaml
token:
  expiration:
    days: 30

keys:
  signing-kid: kid1
  list:
    - kid: kid1
      public-path: /keys/public.pem
      private-path: /keys/private.pem
```

- Suporte a múltiplos `kid`
- Chave ativa definida por `signing-kid`

---

## 📁 Estrutura esperada dos arquivos PEM

Coloque os arquivos `.pem` em:

```
src/main/resources/keys/
├── public.pem
└── private.pem
```

---

## ✅ Boas práticas implementadas

- RS256 com rotação segura de chaves
- Assinatura e verificação compatíveis com o padrão OpenID
- `.well-known/jwks.json` para uso externo (autodescoberta de chaves)
- Uso de `kid` para múltiplas versões de chave pública
- Separação por camada (Clean Architecture)

---

## 🧪 Teste de validação local com Python

O projeto inclui um script `validate_token.py` que simula o papel de um parceiro externo consumindo o token:

- Gera um token usando o endpoint `/token/generate`
- Busca a chave pública no endpoint `/jwks.json`
- Extrai o `kid` do JWT
- Valida a assinatura do token localmente usando `pyjwt` e `cryptography`

### 📄 Requisitos

```bash
pip install requests pyjwt cryptography
```

### ▶️ Executar o script

```bash
python validate_token.py
```

### ✅ Resultado esperado

- Token válido? true
- Payload decodificado exibido no terminal

Esse teste confirma que o endpoint `.well-known/jwks.json` está funcionando corretamente e que os tokens assinados são válidos para consumo por sistemas externos.