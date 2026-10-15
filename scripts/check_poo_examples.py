#!/usr/bin/env python3
"""Compile selected actual slide snippets and check their Java 21 behavior.

Requires javac/java 21+. Uses a temporary directory; never edits course sources.
Not a claim that every fragment or the JUnit examples were compiled.
"""
import argparse
from html.parser import HTMLParser
from pathlib import Path
import subprocess
import tempfile


class Snippets(HTMLParser):
    def __init__(self):
        super().__init__(convert_charrefs=True)
        self.slide = None
        self.in_pre = False
        self.blocks = {}

    def handle_starttag(self, tag, attrs):
        values = dict(attrs)
        if tag == "section" and "slide" in values.get("class", "").split():
            self.slide = values.get("id")
        if tag == "pre" and self.slide:
            self.in_pre = True
            self.blocks.setdefault(self.slide, []).append("")

    def handle_endtag(self, tag):
        if tag == "pre":
            self.in_pre = False
        if tag == "section":
            self.slide = None

    def handle_data(self, data):
        if self.in_pre and self.slide:
            self.blocks[self.slide][-1] += data

    def matching(self, number, predicate):
        matches = [block for block in self.blocks.get(f"slide-{number}", []) if predicate(block)]
        if not matches:
            raise ValueError(f"slide-{number}: expected code fragment not found")
        return "\n".join(matches)


def main():
    arguments = argparse.ArgumentParser(description=__doc__)
    arguments.add_argument("deck", nargs="?", type=Path, default=Path("lessons/java-poo.html"))
    args = arguments.parse_args()
    snippets = Snippets()
    snippets.feed(args.deck.read_text(encoding="utf-8"))
    has_class = lambda text: "class " in text or "interface " in text
    declarations = lambda number: snippets.matching(number, has_class)
    usage = lambda number: snippets.matching(number, lambda text: not has_class(text))

    cases = [
        ("objects-and-aliases", declarations(10),
         usage(10) + "\n" + usage(11) + "\ncheck(primero.unidades == 4); check(segundo.unidades == 1); check(alias == primero);", ""),
        ("inherited-method", declarations(23),
         "check(new EntregaExpress().diasEstimados() == 5);", ""),
        ("overridden-method", declarations(24),
         "Entrega normal = new Entrega(); Entrega express = new EntregaExpress(); check(normal.diasEstimados() == 5); check(express.diasEstimados() == 1);", ""),
        ("overloads", "class Calculo {\n" + snippets.matching(31, lambda text: "totalConEnvio" in text) + "\n}",
         "Calculo c = new Calculo(); check(c.totalConEnvio(10) == 15); check(c.totalConEnvio(10, true) == 19); check(c.totalConEnvio(10, false) == 15);", ""),
        ("value-equality", declarations(44),
         "CodigoProducto a = new CodigoProducto(10); CodigoProducto b = new CodigoProducto(10); check(a != b); check(a.equals(b)); check(a.hashCode() == b.hashCode()); Set<CodigoProducto> s = new HashSet<>(); s.add(a); s.add(b); check(s.size() == 1);", ""),
        ("inventory-validation", declarations(48),
         """Inventario valido = new Inventario(); valido.retirar(2); check(valido.consultarStock() == 3);
Inventario completo = new Inventario(); completo.retirar(5); check(completo.consultarStock() == 0);
Inventario rechazado = new Inventario();
try { rechazado.retirar(8); throw new AssertionError("missing stock exception"); }
catch (StockInsuficienteException expected) { check(rechazado.consultarStock() == 5); }
for (int n : new int[]{0, -1}) {
  try { rechazado.retirar(n); throw new AssertionError("missing argument exception"); }
  catch (IllegalArgumentException expected) { check(rechazado.consultarStock() == 5); }
}""", ""),
        ("generic-box", declarations(44) + "\n" + declarations(55),
         usage(55) + "\ncheck(codigo.equals(new CodigoProducto(10))); check(Util.identidad(codigo) == codigo);", ""),
        ("factory-delayed-send", declarations(67),
         'Notificador consola = FabricaAvisos.crear(true); Notificador correo = FabricaAvisos.crear(false); consola.enviar("A-17"); correo.enviar("A-17");',
         "A-17\nCorreo simulado: A-17\n"),
        ("strategy", declarations(69),
         "check(new Cotizador(new Estandar()).cotizar() == 5); check(new Cotizador(new Express()).cotizar() == 9);", ""),
        ("observer-subscribe-unsubscribe", declarations(71),
         """Pedido pedido = new Pedido(); List<String> eventos = new ArrayList<>();
ObservadorPedido registro = estado -> eventos.add("registro:" + estado);
ObservadorPedido cliente = estado -> eventos.add("cliente:" + estado);
pedido.suscribir(registro); pedido.suscribir(cliente); pedido.avisarConfirmacion();
check(eventos.equals(List.of("registro:Confirmado", "cliente:Confirmado")));
eventos.clear(); pedido.retirar(registro); pedido.avisarConfirmacion();
check(eventos.equals(List.of("cliente:Confirmado")));""", ""),
    ]
    with tempfile.TemporaryDirectory(prefix="poo-slide-examples-") as directory:
        root = Path(directory)
        for name, definitions, body, expected in cases:
            folder = root / name
            folder.mkdir()
            source = folder / "SlideExampleTest.java"
            source.write_text("import java.util.*;\n" + definitions + "\npublic class SlideExampleTest {\n"
                              "static void check(boolean condition) { if (!condition) throw new AssertionError(); }\n"
                              "public static void main(String[] args) {\n" + body + "\n}\n}\n", encoding="utf-8")
            subprocess.run(["javac", "--release", "21", "-d", str(folder), str(source)], check=True, timeout=30)
            result = subprocess.run(["java", "-ea", "-cp", str(folder), "SlideExampleTest"],
                                    check=True, capture_output=True, text=True, timeout=15)
            if result.stdout != expected:
                raise AssertionError(f"{name}: expected {expected!r}, got {result.stdout!r}")
            print(f"PASS: {name}")
    print(f"Verified {len(cases)} Java 21 snippet scenarios from {args.deck}")


if __name__ == "__main__":
    main()
