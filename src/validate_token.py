import requests
import jwt
from jwt.algorithms import RSAAlgorithm

BASE_URL = "http://localhost:8080"

def generate_token():
    payload = {
        "idTransacional": "startup-123",
        "codigoProduto": ["PRODUTO-1", "PRODUTO-2"]
    }
    response = requests.post(f"{BASE_URL}/token/generate", json=payload)
    response.raise_for_status()
    return response.text

def get_public_key(kid):
    response = requests.get(f"{BASE_URL}/.well-known/jwks.json")
    response.raise_for_status()
    jwks = response.json()

    for key in jwks["keys"]:
        if key["kid"] == kid:
            return RSAAlgorithm.from_jwk(key)

    raise Exception(f"kid '{kid}' não encontrado no JWKS")

def validate_token(token):
    headers = jwt.get_unverified_header(token)
    kid = headers["kid"]
    public_key = get_public_key(kid)

    # Valida assinatura, ignora expiração
    decoded = jwt.decode(token, key=public_key, algorithms=["RS256"], options={"verify_exp": False})
    return decoded

if __name__ == "__main__":
    token = generate_token()
    print(f"Token gerado:\n{token}\n")

    try:
        payload = validate_token(token)
        print("Token válido!")
        print(payload)
    except Exception as e:
        print(f" Token inválido: {e}")