#!/usr/bin/env bash
# Comprobación de endpoints REST de pedidos-hexagonal.
#
# Prerrequisitos:
#   - curl instalado
#   - La aplicación empaquetada y en ejecución:
#       ./mvnw package -DskipTests
#       java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar
#     Ejemplos con otros perfiles:
#       java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=inmemory,culqi
#       java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar --spring.profiles.active=postgres
#
# Uso:
#   ./scripts/check_api.sh
#   BASE_URL=http://localhost:8080 ./scripts/check_api.sh
#   ./scripts/check_api.sh http://localhost:8080

set -euo pipefail

BASE_URL="${1:-${BASE_URL:-http://localhost:8080}}"
BASE_URL="${BASE_URL%/}"

if ! command -v curl >/dev/null 2>&1; then
  echo "ERROR: se requiere curl para ejecutar este script." >&2
  exit 1
fi

RUN_ID="$(date +%s)-$$"
ID_ALTO="CHK-ALTO-${RUN_ID}"
ID_BAJO="CHK-BAJO-${RUN_ID}"
ID_ENTREGADO="CHK-ENT-${RUN_ID}"
ID_AUSENTE="CHK-999-${RUN_ID}"

TMP_BODY="$(mktemp)"
trap 'rm -f "$TMP_BODY"' EXIT

PASSED=0

assert_request() {
  local name="$1"
  local method="$2"
  local path="$3"
  local expected_status="$4"
  local payload="$5"
  shift 5

  local status
  if [ -n "$payload" ]; then
    status="$(curl -sS -o "$TMP_BODY" -w "%{http_code}" \
      -X "$method" \
      -H "Content-Type: application/json" \
      -d "$payload" \
      "${BASE_URL}${path}")"
  else
    status="$(curl -sS -o "$TMP_BODY" -w "%{http_code}" \
      -X "$method" \
      "${BASE_URL}${path}")"
  fi

  local body
  body="$(cat "$TMP_BODY")"

  if [ "$status" != "$expected_status" ]; then
    echo "FAIL [$name]: esperado HTTP $expected_status, recibido HTTP $status" >&2
    echo "  Respuesta: $body" >&2
    exit 1
  fi

  local expected_substring
  for expected_substring in "$@"; do
    if [ -n "$expected_substring" ] && ! printf '%s' "$body" | grep -Fq "$expected_substring"; then
      echo "FAIL [$name]: la respuesta no contiene '$expected_substring'" >&2
      echo "  Respuesta: $body" >&2
      exit 1
    fi
  done

  PASSED=$((PASSED + 1))
  echo "PASS [$name]: HTTP $status"
}

echo "Verificando API en ${BASE_URL} (lote ${RUN_ID})..."

assert_request \
  "Crear pedido con monto alto (>= S/ 100)" \
  "POST" "/api/v1/pedidos" "201" \
  "{\"id\":\"${ID_ALTO}\",\"totalSoles\":120}" \
  "\"id\":\"${ID_ALTO}\"" "\"estado\":\"EN_PREPARACION\"" "\"reembolsado\":false"

assert_request \
  "Rechazar ID duplicado" \
  "POST" "/api/v1/pedidos" "409" \
  "{\"id\":\"${ID_ALTO}\",\"totalSoles\":120}" \
  "\"status\":409"

assert_request \
  "Rechazar monto inválido (<= 0)" \
  "POST" "/api/v1/pedidos" "400" \
  "{\"id\":\"CHK-BAD-${RUN_ID}\",\"totalSoles\":0}" \
  "\"status\":400"

assert_request \
  "Consultar pedido por ID" \
  "GET" "/api/v1/pedidos/${ID_ALTO}" "200" "" \
  "\"id\":\"${ID_ALTO}\"" "\"estado\":\"EN_PREPARACION\""

assert_request \
  "Consultar pedido inexistente devuelve 404" \
  "GET" "/api/v1/pedidos/${ID_AUSENTE}" "404" "" \
  "\"status\":404"

assert_request \
  "Listar pedidos incluye el pedido creado" \
  "GET" "/api/v1/pedidos" "200" "" \
  "\"id\":\"${ID_ALTO}\""

assert_request \
  "Cancelar pedido >= S/ 100 aplica reembolso" \
  "PATCH" "/api/v1/pedidos/${ID_ALTO}/cancelar" "200" "" \
  "\"estado\":\"CANCELADO\"" "\"reembolsado\":true"

assert_request \
  "Rechazar doble cancelación" \
  "PATCH" "/api/v1/pedidos/${ID_ALTO}/cancelar" "409" "" \
  "\"status\":409"

assert_request \
  "Crear pedido con monto bajo (< S/ 100)" \
  "POST" "/api/v1/pedidos" "201" \
  "{\"id\":\"${ID_BAJO}\",\"totalSoles\":50}" \
  "\"id\":\"${ID_BAJO}\"" "\"estado\":\"EN_PREPARACION\""

assert_request \
  "Cancelar pedido < S/ 100 no aplica reembolso" \
  "PATCH" "/api/v1/pedidos/${ID_BAJO}/cancelar" "200" "" \
  "\"estado\":\"CANCELADO\"" "\"reembolsado\":false"

assert_request \
  "Crear pedido para entregar" \
  "POST" "/api/v1/pedidos" "201" \
  "{\"id\":\"${ID_ENTREGADO}\",\"totalSoles\":85}" \
  "\"id\":\"${ID_ENTREGADO}\""

assert_request \
  "Entregar pedido en preparación" \
  "PATCH" "/api/v1/pedidos/${ID_ENTREGADO}/entregar" "200" "" \
  "\"estado\":\"ENTREGADO\""

assert_request \
  "Rechazar entrega repetida" \
  "PATCH" "/api/v1/pedidos/${ID_ENTREGADO}/entregar" "409" "" \
  "\"status\":409"

assert_request \
  "Rechazar cancelación de pedido entregado" \
  "PATCH" "/api/v1/pedidos/${ID_ENTREGADO}/cancelar" "409" "" \
  "\"status\":409"

assert_request \
  "Cancelar pedido inexistente devuelve 404" \
  "PATCH" "/api/v1/pedidos/${ID_AUSENTE}/cancelar" "404" "" \
  "\"status\":404"

echo "OK: ${PASSED} comprobaciones superadas contra ${BASE_URL}."
